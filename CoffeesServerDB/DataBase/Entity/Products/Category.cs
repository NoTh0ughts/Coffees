using System;

namespace CoffeesServerDB.DataBase.Entity.Products
{
    public class Category : BaseEntity
    {
        public string Name { get; set; }
        public Guid Menu_id { get; set; } 
    }
}