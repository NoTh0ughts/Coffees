using System;

namespace CoffeesServerDB.DataBase.Entity.Products
{
    [Obsolete("Not used any more", true)]
    public class Subcategory : BaseEntity
    {
        public string Name { get; set; }
        public Guid Category_id { get; set; }
    }
}