using System;

namespace CoffeesServerDB.Entity
{
    public class Category : BaseEntity
    {
        public string Name { get; set; }
        public Guid Menu_id { get; set; } 
    }
}