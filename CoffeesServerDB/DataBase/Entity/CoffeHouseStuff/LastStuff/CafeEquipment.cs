using System;

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.LastStuff
{
    [Obsolete("Not used any more", true)]
    public class CafeEquipment 
    {
        public int Cafe_id { get; set; }
        public virtual Generated.Cafe Cafe { get; set; }
        
        public int Equipment_id { get; set; }
        public virtual Generated.Equipment Equipment { get; set; }
        
        public int Count { get; set; }
    }
}