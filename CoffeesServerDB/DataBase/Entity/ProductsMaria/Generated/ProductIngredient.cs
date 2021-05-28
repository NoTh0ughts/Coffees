#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated
{
    public partial class ProductIngredient
    {
        public int IngredientId { get; set; }
        public int ProductId { get; set; }

        public virtual ProductsSqlServer.Generated.Ingredient Ingredient { get; set; }
        public virtual ProductsSqlServer.Generated.Product Product { get; set; }
    }
}
