using System;
using System.Collections.Generic;

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.LastStuff
{
    [Obsolete("Not used any more", true)]
    public class Cafe : BaseEntity
    {
        public string Address { get; set; }
        public int City_id { get; set; }

        public virtual City City { get; set; }
        public virtual ICollection<Shedule> Shedules { get; set; }
        public virtual ICollection<Worker> Workers { get; set; }
        public virtual ICollection<Equipment> Equipments { get; set; }
        public virtual ICollection<Generated.CafeEquipment> CafeEquipments { get; set; }
    }
}