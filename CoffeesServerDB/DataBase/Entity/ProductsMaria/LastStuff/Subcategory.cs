using System;

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.LastStuff
{
    [Obsolete("Not used any more", true)]
    public class Subcategory : BaseEntity
    {
        public string Name { get; set; }
        public Guid Category_id { get; set; }
    }
}