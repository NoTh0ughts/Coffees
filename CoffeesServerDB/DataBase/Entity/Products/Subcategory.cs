using System;

namespace CoffeesServerDB.Entity
{
    public class Subcategory : BaseEntity
    {
        public string Name { get; set; }
        public Guid Category_id { get; set; }
    }
}