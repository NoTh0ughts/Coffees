using System;

namespace CoffeesServerDB.DataBase.Entity.Products
{
    [Obsolete("Not used any more", true)]
    public class Product : BaseEntity
    {
        public string Name { get; set; }
        public int Calories { get; set; }
        public int Cost { get; set; }
        public string Picture { get; set; }
        public Guid Subcategory_id { get; set; }
    }
}