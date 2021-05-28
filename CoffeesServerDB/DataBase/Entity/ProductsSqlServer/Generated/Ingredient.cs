using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated
{
    public partial class Ingredient
    {
        public Ingredient()
        {
            ProductIngredients = new HashSet<ProductIngredient>();
        }

        public int Id { get; set; }
        public string Name { get; set; }

        public virtual ICollection<ProductIngredient> ProductIngredients { get; set; }
    }
}
