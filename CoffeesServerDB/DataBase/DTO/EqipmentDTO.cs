using System;
using System.Linq;
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
        public int CafeId { get; set; }
        public int BrandId { get; set; }
        public int EqTypeId { get; set; }
    }
    
    public partial class DTOHelper
    {
        /*public static readonly Expression<Func<Equipment, EqipmentDTO>> AsEqipmentDTO = (x, cafeId) => new EqipmentDTO
        {
            BrandName = x.Brand.Name,
            EqType = x.EqType.Name,
            Id = x.Id,
            Model = x.Model,
            Count = x.CafeEquipments.First(a => (a.Cafe.Id == cafeId && a.Equipment.Id == x.Id)).Count
        };*/

        public static EqipmentDTO ToEqipmentDto(this Equipment eq, int cafeId)
        {
            var dto = new EqipmentDTO
            {
                BrandName = eq.Brand.Name,
                BrandId = eq.BrandId,
                EqType = eq.EqType.Name,
                EqTypeId = eq.EqTypeId,
                CafeId = cafeId,
                Id = eq.Id,
                Model = eq.Model
            };
            return dto;
        }
    }
}