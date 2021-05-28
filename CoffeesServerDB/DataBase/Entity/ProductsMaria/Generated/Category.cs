using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated
{
    public partial class Category
    {
        public Category()
        {
            Subcategories = new HashSet<ProductsSqlServer.Generated.Subcategory>();
        }

        public int Id { get; set; }
        public string Name { get; set; }
        public int MenuId { get; set; }

        public virtual ProductsSqlServer.Generated.Menu Menu { get; set; }
        public virtual ICollection<ProductsSqlServer.Generated.Subcategory> Subcategories { get; set; }
    }
}
