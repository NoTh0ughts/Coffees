#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated
{
    public partial class ProductIngredient
    {
        public int ProductId { get; set; }
        public int IngredientId { get; set; }

        public virtual Ingredient Ingredient { get; set; }
        public virtual Product Product { get; set; }
    }
}
