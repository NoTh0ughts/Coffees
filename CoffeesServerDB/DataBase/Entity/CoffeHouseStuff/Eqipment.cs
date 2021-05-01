using System;

namespace CoffeesServerDB.Entity.CoffeHouseStuff
{
    public class Eqipment : BaseEntity
    {
        public string Model { get; set; }
        public Guid Brand_id { get; set; }
        public Guid Eq_type_id { get; set; }
    }
}