using System.Collections.Generic;
using System.Linq;
using CoffeesServerDB.DataBase.Entity;
using CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated;
using CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated;
using CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated;
using CoffeesServerDB.DataBase.Entity.UserStuff;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace CoffeesServerDB.DataBase.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class ApiController : ControllerBase
    {
        private readonly ILogger<ApiController> _logger;
        
        private readonly UserService _service;
        private readonly IUnitOfWork<CafeContext> _unitCafe;
        private readonly IUnitOfWork<ProductSqlServerContext> _unitProductSql;
        private readonly IUnitOfWork<ProductMariaContext> _unitProductMaria;
        
        
        public ApiController(ILogger<ApiController> logger,
            UserService service,
            IUnitOfWork<CafeContext> unitCafe,
            IUnitOfWork<ProductSqlServerContext> unitProductSqlServer,
            IUnitOfWork<ProductMariaContext> unitProductMaria)
        {
            _logger = _logger;
            
            _service = service;
            _unitCafe = unitCafe;
            _unitProductMaria = unitProductMaria;
            _unitProductSql = unitProductSqlServer;
        }


        [HttpGet("get_orders")]
        public IEnumerable<Order> get_orders() => _service.GetOrders();

        [HttpGet("get_orders_by_user")]
        public ICollection<Order> getOrdersByUser([FromQuery(Name = "userId")]int userId) => _service.GetOrdersByUser(userId);

        [HttpGet("get_user_by_order")]
        public User getUserByOrder([FromQuery(Name = "orderId")] int orderId) => _service.GetUserByOrder(orderId);

        [HttpGet("get_card_by_user")]
        public Card getCardByUser(int userId) => _service.GetCardByUser(userId);

        [HttpGet("get_order_status")]
        public string getOrderStatus(int orderId) => _service.GetOrderStatus(orderId);

        [HttpGet("get_citys")]
        public IEnumerable<City> getCities()
        {
            var result = _unitCafe.GetRepository<City>().GettAll();
            foreach (var city in result)
            {
                city.Caves.ToList();
            }
            return result;
        }

        [HttpGet("get_users")]
        public IEnumerable<User> getUsers() => _service.GetUsers();

        [HttpGet("getAllAddress")]
        public IEnumerable<string> getAllAddress() => _unitCafe.GetRepository<Cafe>().GettAll().Select(x => x.Address);
        
        //comment
    }
}