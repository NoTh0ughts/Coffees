using System.Collections.Generic;
using System.Linq;
using CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated;
using Microsoft.Extensions.Logging;
using Microsoft.AspNetCore.Mvc;

namespace CoffeesServerDB.DataBase.Controllers
{
    [ApiController, 
     Route("[controller]")]
    public class CoffeesCafesController : ControllerBase
    {
        private readonly ILogger<CoffeesCafesController> _logger;

        public CoffeesCafesController(ILogger<CoffeesCafesController> logger)
        {
            _logger = logger;
        }

        [HttpGet("get_brands")]
        public IEnumerable<Brand> Get()
        {
            using var ctx = new CafeContext();
                return ctx.Brands.ToList();
        }
    }
}