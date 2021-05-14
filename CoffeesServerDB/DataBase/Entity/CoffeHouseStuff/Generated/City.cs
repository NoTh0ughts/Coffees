﻿using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated
{
    public partial class City
    {
        public City()
        {
            Caves = new HashSet<Cafe>();
        }

        public int Id { get; set; }
        public string Name { get; set; }

        public virtual ICollection<Cafe> Caves { get; set; }
    }
}