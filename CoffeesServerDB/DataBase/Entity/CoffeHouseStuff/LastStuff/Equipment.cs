using System;
using System.Collections.Generic;

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.LastStuff
{
    [Obsolete("Not used any more", true)]
    public class Equipment : BaseEntity
    {
        public string Model { get; set; }
        
        public int Brand_id { get; set; }
        public int Eq_type_id { get; set; }
        
        public virtual Brand Brand { get; set; }
        public virtual EqType EqType { get; set; }
        
        public virtual ICollection<Cafe> Cafes { get; set; }
        public virtual ICollection<Generated.CafeEquipment> CafeEquipments { get; set; }
    }
}