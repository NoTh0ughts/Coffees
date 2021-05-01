using System;

namespace CoffeesServerDB.Entity.CoffeHouseStuff
{
    public class Cafe : BaseEntity
    {
        public string Address { get; set; }
        public Guid City_id { get; set; }
    }
}