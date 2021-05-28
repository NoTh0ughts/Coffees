using System;

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.LastStuff
{
    [Obsolete("Not used any more", true)]
    public class Category : BaseEntity
    {
        public string Name { get; set; }
        public Guid Menu_id { get; set; } 
    }
}