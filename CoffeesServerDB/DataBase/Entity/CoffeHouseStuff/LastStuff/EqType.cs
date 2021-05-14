using System;
using System.Collections.Generic;

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.LastStuff
{
    [Obsolete("Not used any more", true)]
    public class EqType : BaseEntity
    {
        public string Value { get; set; }

        public virtual ICollection<Equipment> Equipments { get; set; }
    }
}