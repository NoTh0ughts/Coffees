using System;

namespace CoffeesServerDB.DataBase.Entity.Products
{
    [Obsolete("Not used any more", true)]
    public class Ingredient : BaseEntity
    {
        public string Name { get; set; }
    }
}