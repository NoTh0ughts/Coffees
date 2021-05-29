using System;
using System.Linq.Expressions;
using CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated;

namespace CoffeesServerDB.DataBase.DTO
{
    public class CafeDTO
    {
        public int Id { get; set; }
        public string Address { get; set; }
        public int CityId { get; set; }
        public string CityName { get; set; }
    }


    public static partial class DTOHelper
    {
        public static readonly Expression<Func<Cafe, CafeDTO>> AsCafeDTO = x => new CafeDTO
        {
            Id = x.Id,
            Address = x.Address,
            CityId = x.Id,
            CityName = x.City.Name
        };
    }
}