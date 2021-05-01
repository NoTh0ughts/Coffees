using System;

namespace CoffeesServerDB.Entity
{
    public class Product : BaseEntity
    {
        public string Name { get; set; }
        public int Calories { get; set; }
        public int Cost { get; set; }
        public string Picture { get; set; }
        public Guid Subcategory_id { get; set; }
    }
}