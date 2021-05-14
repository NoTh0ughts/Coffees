using System;
using System.Collections.Generic;

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.LastStuff
{
    [Obsolete("Not used any more", true)]
    public class Brand : BaseEntity
    {
        public string Name { get; set; }

        public virtual ICollection<Equipment> Eqipments { get; set; }
    }
}