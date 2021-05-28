using System.Collections.Generic;
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
        
        public ApiController(ILogger<ApiController> logger, UserService service)
        {
            _logger = _logger;
            _service = service;
        }


        [HttpGet("get_orders")]
        public IEnumerable<Order> get_orders() => _service.Get();

        [HttpGet("get_orders_by_user")]
        public ICollection<Order> getOrdersByUser([FromQuery(Name = "userId")]int userId) => _service.GetOrdersByUser(userId);

        [HttpGet("get_user_by_order")]
        public User getUserByOrder([FromQuery(Name = "orderId")] int orderId) => _service.GetUserByOrder(orderId);

        [HttpGet("get_card_by_user")]
        public Card getCardByUser(int userId) => _service.GetCardByUser(userId);

        [HttpGet("get_order_status")]
        public string getOrderStatus(int orderId) => _service.GetOrderStatus(orderId);
        
        
    }
}