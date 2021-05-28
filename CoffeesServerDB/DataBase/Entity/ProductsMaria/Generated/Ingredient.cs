using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated
{
    public partial class Ingredient
    {
        public Ingredient()
        {
            ProductIngredients = new HashSet<ProductsSqlServer.Generated.ProductIngredient>();
        }

        public int Id { get; set; }
        public string Name { get; set; }

        public virtual ICollection<ProductsSqlServer.Generated.ProductIngredient> ProductIngredients { get; set; }
    }
}
