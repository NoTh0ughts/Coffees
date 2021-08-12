using System;
using System.Collections.Generic;
using System.Linq;
using CoffeesServerDB.DataBase.DTO;
using CoffeesServerDB.DataBase.Entity;
using CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated;
using CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated;
using CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated;
using CoffeesServerDB.DataBase.Entity.UserStuff;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Product = CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated.Product;


namespace CoffeesServerDB.DataBase.Controllers
{
    /// <summary>
    /// Главный контроллер.
    /// Монолитно контролирует все REST запросы к серверу
    /// Все данные разделены на три категории, каждая категория находиться в соотв. СУБД
    /// Данные Product разделенны между SQLServer  & MariaDB
    /// 
    /// User, Order ... - MongoDb
    /// CafeContext - PostgreSQL
    /// Product - MariaDB
    /// Product - SQL Server
    /// </summary>
    [ApiController, Route("[controller]")]
    public class ApiController : ControllerBase
    {
        private readonly ILogger<ApiController> _logger;

        /// <summary> Сервис для работы с MongoDB  </summary>
        private readonly UserService _service;
        
        /// <summary> UOW для работы с PostgreSQL баззой данный </summary>
        private readonly IUnitOfWork<CafeContext> _unitCafe;
        
        /// <summary> UOW для работы с SQL server </summary>
        private readonly IUnitOfWork<ProductSqlServerContext> _unitProductSql;
        
        /// <summary> UOW для работы с MariaDB </summary>
        private readonly IUnitOfWork<ProductMariaContext> _unitProductMaria;

        /// <summary> Конструктор API и полученные данные создаются при помощи Dependency injection, в классе Startup </summary>
        public ApiController(ILogger<ApiController> logger,
            UserService service,
            IUnitOfWork<CafeContext> unitCafe,
            IUnitOfWork<ProductSqlServerContext> unitProductSqlServer,
            IUnitOfWork<ProductMariaContext> unitProductMaria)
        {
            _logger = logger;

            _service = service;
            _unitCafe = unitCafe;
            _unitProductMaria = unitProductMaria;
            _unitProductSql = unitProductSqlServer;
        }


        #region UserStuff

        [HttpGet("User/Orders")]
        public IEnumerable<Order> GetOrders() => _service.GetOrders();

        [HttpGet("User/{userId}/Orders/ByUser")]
        public ICollection<Order> GetOrdersByUser([FromQuery(Name = "order")] int userId) =>
            _service.GetOrdersByUser(userId);

        [HttpGet("User/{userId}/OrdersIncludeProducts")]
        public dynamic GetOrdersIncludeProducts(int orderid)
        {
            var orders = _service.GetOrders(orderid);
            //Создаем словарь имен и цены по идентификатору, чтобы добавить в возвращаемый результат
            var productNames = _unitProductMaria.DbContext.Products
                .Select(x => new
                {
                    x.Id,
                    x.Name,
                    x.Cost
                }).ToDictionary(x => x.Id);

            return (from order in orders
                select new
                {
                    order.Id,
                    Products = order.stuff
                        .Select(
                            x => new {x.Count, 
                                x.Product_id, 
                                Name=productNames[x.Product_id].Name, 
                                Cost=productNames[x.Product_id].Cost})
                }).First();
        }

        [HttpPost("User/AddUser")]
        public ActionResult AddUser(string email, string username, string password, string photo, int card_id)
        {
            // При наличии такогоже email или короткого пароля возвращать ошибку
            if (_service.GetUsers().Any(x => x.Email == email) || username.Length < 3)
            {
                return BadRequest();
            }
            
            // Если не указанно фото, то добавить изображение по умолчанию
            if (string.IsNullOrEmpty(photo))
                photo = "www.coffees.net/users/photo/ref1";

            _service.AddUser(new User
            {
                Basket = Array.Empty<ProductItem>(), 
                Card_id = card_id, 
                Email = email, 
                Password = password, 
                Favorites = Array.Empty<int>(), 
                Photo = photo,
                RegistrationDate = DateTime.Now, 
                Username = username
            });
            return Ok();
        }
        

        [HttpGet("User/Login")]
        public dynamic GetUserByLogin(string username, string password)
        {
            if (_service.GetUsers().Any(x => x.Username == username && x.Password == password))
            {
                var user = _service.GetUsers().First(x => x.Username == username && x.Password == password);
                var cards = _service.GetCardByUser(user.Id);
                return new
                {
                    user.Id,
                    cards.Discount,
                    cardId = cards.Id,
                    user.Email,
                    user.Photo,
                    user.Password,
                    user.Username,
                    user.RegistrationDate,
                    basketCount = user.Basket.Sum(x => x.Count),
                    user.Favorites,
                };
            }

            Response.StatusCode = 400;
            return new User {Id = -1, Username = "ERROR"};
        }

        [HttpGet("User/{userId}/Card")]
        public Card GetCardByUser(int userId) => _service.GetCardByUser(userId);

        [HttpGet("User/OrderStatus/{orderId}")]
        public string GetOrderStatus(int orderId) => _service.GetOrderStatus(orderId);

        [HttpGet("User/{id}")]
        public User GetUserById(int userId) => _service.GetUsers(userId).First();
        
        [HttpGet("User/all")]
        public IEnumerable<User> GetUsers() => _service.GetUsers();

        [HttpGet("User/LargestOrder")]
        public Order GetLargestOrder()
        {
            return _service.GetOrders().OrderByDescending(x => x.stuff.Sum(s => s.Count)).First();
        }

        [HttpGet("User/OldestUser")]
        public User GetOldestUser()
        {
            return _service.GetUsers().OrderByDescending(x => DateTime.Now - x.RegistrationDate).First();
        }

        [HttpGet("User/{id}/Favorites")]
        public dynamic GetFavoritesForUserById(int userId)
        {
            // Получение избранного пользователя
            var favorites = _service.GetUsers(userId).Select(x => x.Favorites).First();
            // Получение всех возможных продуктов
            var products = _unitProductMaria.DbContext.Products.AsEnumerable()
                .Select(x => new {Id= x.Id, Name=x.Name}).ToList();

            var result = new Dictionary<int, string>();

            // Сопоставление идентификатора продукта в избранном и его данных
            for (var i = 0; i < products.Count(); i++)
                if (favorites.Contains(products[i].Id))
                    result.Add(products[i].Id, products[i].Name);

            return result.AsEnumerable();
        }

        [HttpGet("User/{userId}/CompletedOrders")]
        public dynamic GetCompletedOrdersByUserId(int userId)
        {
            // Получаем все завершенные заказы пользователя, а так же кафе и продукты
            var completedOrders = _service.GetOrders()
                .Where(x => x.User_id == userId).AsEnumerable();
            var cafes = _unitCafe.DbContext.Cafes.ToList();
            var products = _unitProductSql.DbContext.Products.ToList();

            
            return (from o in completedOrders
                join cafe in cafes on o.Cafe_id equals cafe.Id
                where o.Status_id == 4
                select new
                {
                    Id = o.Id,
                    Products= o.stuff.Select(x => new
                    {
                        Count=x.Count,
                        ProductId=x.Product_id,
                        ProductName=products.Where(y => y.Id == x.Product_id).Select(x => x.Name).First()
                    }),
                    Address= cafe.Address,
                    DateOrder= o.Date_order
                }).OrderByDescending(x => x.DateOrder);
        }

        [HttpGet("User/ByEmail/{userMail}")]
        public int GetUserIdByEmail(string userMail)
        {
            if (_service.GetUsers().Any(x => x.Email == userMail))
            {
                return _service.GetUsers().First(x => x.Email == userMail).Id;
            }

            Response.StatusCode = 400;
            return -1;
        }
        
        [HttpGet("User/ByUsername/{username}")]
        public int GetUserIdByUsername(string username)
        {
            if (_service.GetUsers().Any(x => x.Username == username))
            {
                return _service.GetUsers().First(x => x.Username == username).Id;
            }

            Response.StatusCode = 400;
            return -1;
        }

        [HttpGet("User/{userId}/ProductsFromBasket")]
        public dynamic GetAllProductsFromUserBasket(int userId)
        {
            var products = _service.GetUsers(userId).First().Basket;
            var productsName = _unitProductMaria.DbContext.Products.ToList();
            
            return from product in products
                join names in productsName on product.Product_id equals names.Id
                select new
                {
                    product.Product_id,
                    product.Count,
                    names.Name
                };
        }

        [HttpPost("User/AddProductToBasket")]
        public ActionResult AddProductToBasket(int userId, int productId, int count = 1)
        {
            // Проверка, что существует такой продукт
            if (!_service.GetUsers(userId).Any() || !_unitProductMaria.DbContext.Products.Any(x => x.Id == productId))
            {
                Response.StatusCode = 400;
                return BadRequest();
            }

            var user = _service.GetUsers(userId).First();
            // Если такой продукт уже есть, то просто увеличиваем количество, иначе добавляем его в корзину
            if (user.Basket.Any(x => x.Product_id == productId))
            {
                foreach (var t in user.Basket)
                {
                    if (t.Product_id == productId)
                        t.Count += count;
                }
            }
            else
            {
                user.Basket = user.Basket
                    .Concat( 
                        new List<ProductItem>
                        {
                            new()
                            {
                                Count = count, Product_id = productId
                            }
                        }).ToArray();
            }
            
            _service.UpdateUser(userId, user);
            return Ok();
        }
        
        
        
        [HttpGet("User/OrdersByCafe/{cafeId}")]
        public IEnumerable<OrderDTO> GetOrdersByCafe(int cafeId)
        {
            var orders = _service.GetOrders()
                .Where(x => x.Cafe_id == cafeId);
            var ordersDto = (from order in orders
                let user = _service.GetUsers()
                    .First(x => x.Id == order.User_id)
                let status = _service.GetStatuses()
                    .First(x => x.Id == order.Status_id)
                select order.ToDto(user,
                    status)).ToList();

            return ordersDto;
        }
        
        [HttpPut("User/AddProductToUserFavorites")]
        public ActionResult AddProductToUserFavorites(int userId, int productId)
        {
            var user = _service.GetUsers(userId).First();
            
            // Продукт уже в избранном или не существует такого продукта
            if (user.Favorites.Contains(productId) || 
                _unitProductMaria.DbContext.Products.Max(x => x.Id) < productId)
            {
                return BadRequest();
            }

            user.Favorites = user.Favorites.Concat(new []{productId}).ToArray();
            _service.UpdateUser(user.Id, user);
            return Ok();
        }


        [HttpPut("User/UpdateOrder")]
        public ActionResult UpdateOrder(Order order)
        {
            if (_service.GetOrders().All(x => x.Id != order.Id))
            {
                return BadRequest();
            }

            _service.UpdateOrder(order.Id, order);
            return Ok();
        }

        [HttpDelete("User/ClearBasket")]
        public ActionResult ClearBasket(int userId)
        {
            if (_service.GetUsers(userId).Any())
            {
                var user = _service.GetUsers(userId).First();

                user.Basket = new ProductItem[0];
                
                _service.UpdateUser(userId, user);
                return Ok();
            }

            Response.StatusCode = 400;
            return BadRequest();
        }
        
        [HttpPost("User/AddOrder")]
        public Order AddOrder(int cafeId, ProductItem[] products,  int userId, int statusId = 1)
        {
            var order = new Order
            {
                Cafe_id = cafeId,
                Date_order = DateTime.Now,
                stuff = products,
                User_id = userId,
                Status_id = statusId
            };
            
            if (order.Status_id < 1 || order.Status_id > 4) order.Status_id = 1;
        
            return _service.AddOrder(order);
        }

        [HttpPut("User/UpdateUserBasket")]
        public int UpdateUserBasket(int userId, [FromBody] ProductItem[] data)
        {
            // Нет такого пользователя
            if (!_service.GetUsers(userId).Any())
            {
                Response.StatusCode = 400;
                BadRequest();
                return -1;
            }
            
            var products = _unitProductMaria.DbContext.Products.AsEnumerable();
            var newBasket = data.Where(t => products.Any(x => x.Id == t.Product_id) && t.Count >= 1).ToList();

            if (newBasket.Count > 0)
            {
                var user = _service.GetUsers(userId).First();
                user.Basket = newBasket.ToArray();
                _service.UpdateUser(userId, user);
                Ok();
                return user.Basket.Sum(x => x.Count);
            }

            BadRequest();
            return -1;
        }
        
        
        [HttpDelete("User/DeleteProductFromBasket")]
        public ActionResult DeleteProductFromUserBasket(int userId, int productId)
        {
            // Нет такого пользователя или продукта
            if (!_service.GetUsers(userId).Any() || !_unitProductMaria.DbContext.Products.Any(x => x.Id == productId))
            {
                Response.StatusCode = 400;
                return BadRequest();
            }
            
            
            var user = _service.GetUsers(userId).First();
            if (user.Basket.Any(x => x.Product_id == productId))
            {
                user.Basket = user.Basket.Where(x => x.Product_id != productId).ToArray();
            }
            else
            {
                return Ok();
            }

            _service.UpdateUser(userId, user);
            return Ok();
        }
        
        [HttpDelete("User/DeleteProductFromFavorites")]
        public ActionResult DeleteProductFromUserFavorites(int userId, int productId)
        {
            var user = _service.GetUsers(userId).First();

            if (user.Favorites.Contains(productId))
            {
                user.Favorites = user.Favorites.Where(x => x != productId).ToArray();
                _service.UpdateUser(user.Id, user);
                return Ok();
            }

            return BadRequest();
        }
        
        #endregion

        #region Cafes

        [HttpGet("Cafe/Cities")]
        public IEnumerable<City> GetCities() => _unitCafe.GetRepository<City>().GetAll();

        
        [HttpGet("Cafe/AddressForAllCafes")]
        public IEnumerable<string> GetAddressForAllCafes() => _unitCafe.GetRepository<Cafe>().GetAll().Select(x => x.Address);
        

        [HttpGet("Cafe/AllCafeDTO")]
        public IEnumerable<CafeDTO> GetAllCafeDTO() =>
            _unitCafe.DbContext.Cafes.Include(x => x.City).Select(DTOHelper.AsCafeDTO);

        [HttpGet("Cafe/WorkersByCafe/{cafeId}")]
        public IEnumerable<WorkerDTO> GetWorkerDtoByCafeId(int cafeId) => _unitCafe
            .DbContext.Workers
            .Include(x => x.Cafe)
            .Where(x => x.CafeId == cafeId)
            .Select(DTOHelper.AsWorkerDTO)
            .AsEnumerable();

        [HttpGet("Cafe/CafesInCity/{cityId}")]
        public dynamic GetCafesInCity(int cityId)
        {
            return _unitCafe.DbContext.Cafes.Where(x => x.CityId == cityId).Select(x => new
            {
                Id = x.Id,
                Address = x.Address
            }).AsEnumerable();
        }
        
        [HttpGet("Cafe/{cafeId}/Schedule")]
        public IEnumerable<Schedule> GetScheduleByCafeId(int cafeId) =>
            _unitCafe.GetRepository<Schedule>()
                .GetAll()
                .Where(x => x.CafeId == cafeId);

        [HttpPost("Cafe/AddWorker")]
        public WorkerDTO AddWorker([Bind("Id, Fullname, Salary, Post, cafe_id")]
            WorkerDTO worker)
        {
            //Такой должности нет
            if (!_unitCafe.GetRepository<Post>().GetAll().Select(x => x.Name == worker.Post).ToList().Any())
            {
                var newPost = _unitCafe.GetRepository<Post>().Create(new Post {Name = worker.Post});
                worker.Post_id = newPost.Id;
            }

            return _unitCafe.GetRepository<Worker>().Create(new Worker
            {
                Fullname = worker.Fullname,
                CafeId = worker.Cafe_id,
                PostId = worker.Post_id,
                Salary = worker.Salary
            }).ToWorkerDTO();
        }

        [HttpDelete("Cafe/DeleteWorker")]
        public ActionResult DeleteWorker(Worker worker)
        {
            if (_unitCafe.GetRepository<Worker>().GetById(worker.Id) == null)
            {
                return BadRequest();
            }

            _unitCafe.GetRepository<Worker>().Remove(worker);
            return Ok();
        }

        [HttpPut("Cafe/UpdateWorker")]
        public ActionResult UpdateWorker(Worker newItem)
        {
            if (_unitCafe.GetRepository<Worker>().GetById(newItem.Id) == null)
            {
                return BadRequest();
            }

            _unitCafe.GetRepository<Worker>().Update(newItem);
            return Ok();
        }

        [HttpPut("Cafe/UpdateSchedule")]
        public ActionResult UpdateSchedule(Schedule newItem)
        {
            if (_unitCafe.GetRepository<Schedule>().GetById(newItem.Id) == null)
            {
                return BadRequest();
            }

            _unitCafe.GetRepository<Schedule>().Update(newItem);
            
            return Ok();
        }
        

        [HttpPut("Cafe/UpdateEquipment")]
        public ActionResult UpdateEquipment(EqipmentDTO equipment)
        {
            if (_unitCafe.GetRepository<Equipment>().GetAll().Any(x => x.Id == equipment.Id))
                return BadRequest();

            return Ok();
        }

        #endregion


        [HttpGet("Product/ProductsDTO")]
        public IEnumerable<ProductDTO> GetProductsDTO(int id = -1)
        {
            var products = _unitProductMaria.DbContext.Products;

            return (from product in products
                where product.Id == (id == -1 ? product.Id : id) 
                select new ProductDTO
                {
                    Id = product.Id, 
                    Name = product.Name,
                    Calories = product.Calories,
                    Subcategory = product.Subcategory.Name,
                    Category = product.Subcategory.Category.Name,
                    Menu = product.Subcategory.Category.Menu.Name,
                    Ingredients = product.ProductIngredients.Select(x => x.Ingredient.Name).ToArray(),
                    Components = product.ProductComponents.Select(s => new ComponentItem
                    {
                        Dose = s.Dose, Name = s.Component.Name
                    }).ToArray(),
                    Cost = product.Cost,
                    Picture = product.Picture
                }).AsEnumerable();
        }
        
        [HttpGet("Product/AllProducts")]
        public IEnumerable<Product> GetAllProducts() => _unitProductMaria.DbContext.Products;

        [HttpGet("Product/{productId}/ProductConsist")]
        public IngredientComponentItem GetProductConsist(int productId)
        {
            return _unitProductMaria.DbContext.Products.Where(x => x.Id == productId).Select(x =>
                new IngredientComponentItem
                {
                    Ingredients = x.ProductIngredients.Select(y => y.Ingredient.Name).ToArray(),
                    Components = x.ProductComponents.Select(y => new ComponentItem
                    {
                        Dose = y.Dose,
                        Name = y.Component.Name
                    }).ToArray()
                }).First();
        }

        [HttpGet("Product/AllSubcategoryIncludeProducts")]
        public dynamic GetAllSubcategoryIncludeProducts(int categoryId = -1)
        {
            return _unitProductMaria.DbContext.Subcategories
                        .Where(x => x.Products.Count > 0)
                        .Where(x => categoryId == -1 || x.CategoryId == categoryId)
                        .Include(x => x.Products)
                        .Select(x => new
                        {
                            Id=x.Id,
                            Name=x.Name,
                            Products=x.Products
                                .Select(y => new {y.Id, y.Name})
                                .OrderBy(z => z.Name)
                                .ToArray()
                        }).AsEnumerable()
                        .OrderBy(x => x.Name);
        }


        [HttpGet("Product/AllMenuPositionsIncludeCategory")]
        public dynamic GetAllMenuPositionsIncludeCategory()
        {
            return _unitProductMaria.DbContext.Menus
                .Select(x => new
                {
                    Id = x.Id,
                    Name = x.Name,
                    Categories = x.Categories.Select(y => new
                    {
                        y.Id,
                        y.Name,
                        productId = y.Subcategories.First().Products.Select(x => x.Id).First()
                    }).OrderBy(y => y.Name).ToArray()
                })
                .Where(x => x.Categories.Select(x => x.productId).Any())
                .AsEnumerable()
                .OrderBy(x => x.Name);
        }

        [HttpGet("Product/AvgCostProduct")]
        public double GetAvgCostProduct() => _unitProductMaria.DbContext.Products.Average(x => x.Cost);

        [HttpGet("Product/CheapestBreakfast")]
        public Product GetCheapestBreakfast() => _unitProductMaria.DbContext.Products
            .Where(x => x.Subcategory.Name.Contains("Breakfast"))
            .Aggregate((curMin, x) => (curMin == null) || x.Cost < curMin.Cost ? x : curMin);


        [HttpGet("Cafe/CityWithGreatestCafeCount")]
        public string GetCityWithGreatestCafeCount() => _unitCafe.DbContext.Cities
            .Aggregate((curMax, x) => (curMax == null) || x.Cafes.Count > curMax.Cafes.Count ? x : curMax).Name;

        [HttpGet("Cafe/AvgSalaryForWorkers")]
        public double GetAvgSalaryForWorkers() => _unitCafe.DbContext.Workers.Average(x => x.Salary);

    }
} 