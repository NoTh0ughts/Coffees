using System;
using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB
{
    public partial class ProductIngredient
    {
        public int IngredientId { get; set; }
        public int ProductId { get; set; }

        public virtual Ingredient Ingredient { get; set; }
        public virtual Product Product { get; set; }
    }
}
