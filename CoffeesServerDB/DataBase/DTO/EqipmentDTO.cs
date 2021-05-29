using System;
using System.Linq.Expressions;
using CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated;

namespace CoffeesServerDB.DataBase.DTO
{
    public class EqipmentDTO
    {
        public int Id { get; set; }
        public string Model { get; set; }
        public string BrandName { get; set; }
        public string EqType { get; set; }
    }
    
    public partial class DTOHelper
    {
        public static readonly Expression<Func<Equipment, EqipmentDTO>> AsEqipmentDTO = x => new EqipmentDTO
        {
            BrandName = x.Brand.Name,
            EqType = x.EqType.Name,
            Id = x.Id,
            Model = x.Model
        };
    }
}