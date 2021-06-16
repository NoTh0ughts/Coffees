using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated
{
    public partial class Product
    {
        public Product()
        {
            ProductComponents = new HashSet<ProductComponent>();
            ProductIngredients = new HashSet<ProductIngredient>();
        }

        public int Id { get; set; }
        public string Name { get; set; }
        public int Calories { get; set; }
        public int Cost { get; set; }
        public string Picture { get; set; }
        public int? SubcategoryId { get; set; }

        public virtual Subcategory Subcategory { get; set; }
        public virtual ICollection<ProductComponent> ProductComponents { get; set; }
        public virtual ICollection<ProductIngredient> ProductIngredients { get; set; }
    }
}
