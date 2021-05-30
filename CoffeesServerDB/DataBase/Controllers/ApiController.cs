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
        public IEnumerable<Order> GetOrders() => _service.GetOrders();

        [HttpGet("get_orders_by_user")]
        public ICollection<Order> GetOrdersByUser([FromQuery(Name = "userId")]int userId) => _service.GetOrdersByUser(userId);

        [HttpGet("get_user_by_order")]
        public User GetUserByOrder([FromQuery(Name = "orderId")] int orderId) => _service.GetUserByOrder(orderId);

        [HttpGet("get_card_byUser")]
        public Card GetCardByUser(int userId) => _service.GetCardByUser(userId);

        [HttpGet("get_order_status")]
        public string GetOrderStatus(int orderId) => _service.GetOrderStatus(orderId);

        [HttpGet("get_citys")]
        public IEnumerable<City> GetCities() => _unitCafe.GetRepository<City>().GetAll();

        [HttpGet("get_users")]
        public IEnumerable<User> GetUsers() => _service.GetUsers();

        [HttpGet("getAllAddress")]
        public IEnumerable<string> GetAllAddress() => _unitCafe.GetRepository<Cafe>().GetAll().Select(x => x.Address);
        
        [HttpGet("get_dto_cafe")]
        public IEnumerable<CafeDTO> GetDtoCafe() => _unitCafe.DbContext.Cafes.Include(x => x.City).Select(DTOHelper.AsCafeDTO);

        [HttpGet("get_workersDTO_ByCafeId")]
        public IEnumerable<WorkerDTO> GetWorkerDtoByCafeId([FromQuery(Name = "cafeId")] int cafeId) => _unitCafe
            .GetRepository<Worker>()
            .GetAll()
            .AsQueryable()
            .Include(x => x.Cafe)
            .Where(x => x.CafeId == cafeId)
            .Select(DTOHelper.AsWorkerDTO)
            .AsEnumerable();

        [HttpGet("get_shedule_ByCafeId")]
        public IEnumerable<Schedule> GetSheduleByCafeId([FromQuery(Name = "cafeId")] int cafeId) =>
            _unitCafe.GetRepository<Schedule>()
                .GetAll()
                .Where(x => x.CafeId == cafeId);
        
        [HttpPost("post_add_worker")]
        public WorkerDTO PostAddWorker([Bind("Id, Fullname, Salary, Post, cafe_id")] WorkerDTO worker)
        {
            if (_unitCafe.GetRepository<Post>().GetAll().Select(x => x.Name == worker.Post).ToList().Any())
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

        [HttpDelete("delete_worker")]
        public ActionResult DeleteWorker(Worker worker)
        {
            if (_unitCafe.GetRepository<Worker>().GetById(worker.Id) == null)
            {
                return BadRequest();
            }

            _unitCafe.GetRepository<Worker>().Remove(worker);
            return Ok();
        }

        [HttpPut("update_worker")]
        public ActionResult UpdateWorker(Worker newItem)
        {
            if (_unitCafe.GetRepository<Worker>().GetById(newItem.Id) == null)
            {
                return BadRequest();
            }

            _unitCafe.GetRepository<Worker>().Update(newItem);
            return Ok();
        }

        [HttpPut("update_schedule")]
        public ActionResult UpdateSchedule(Schedule newItem)
        {
            if (_unitCafe.GetRepository<Schedule>().GetById(newItem.Id) == null)
            {
                return BadRequest();
            }

            _unitCafe.GetRepository<Schedule>().Update(newItem);
            return Ok();
        }
        
        
        
    }
}