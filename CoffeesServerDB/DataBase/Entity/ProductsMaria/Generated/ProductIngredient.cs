using System.ComponentModel.DataAnnotations;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated
{
    public partial class ProductIngredient
    {
        [Key]
        public int IngredientId { get; set; }
        [Key]
        public int ProductId { get; set; }

        public virtual Ingredient Ingredient { get; set; }
        public virtual Product Product { get; set; }
    }
}
