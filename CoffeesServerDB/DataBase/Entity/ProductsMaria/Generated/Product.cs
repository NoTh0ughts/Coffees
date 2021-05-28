using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated
{
    public partial class Product
    {
        public Product()
        {
            ProductComponents = new HashSet<ProductsSqlServer.Generated.ProductComponent>();
            ProductIngredients = new HashSet<ProductsSqlServer.Generated.ProductIngredient>();
        }

        public int Id { get; set; }
        public string Name { get; set; }
        public int Calories { get; set; }
        public int Cost { get; set; }
        public string Picture { get; set; }
        public int? SubcategoryId { get; set; }

        public virtual ProductsSqlServer.Generated.Subcategory Subcategory { get; set; }
        public virtual ICollection<ProductsSqlServer.Generated.ProductComponent> ProductComponents { get; set; }
        public virtual ICollection<ProductsSqlServer.Generated.ProductIngredient> ProductIngredients { get; set; }
    }
}
