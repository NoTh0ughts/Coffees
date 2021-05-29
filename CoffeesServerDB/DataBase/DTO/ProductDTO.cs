using System;
using System.Linq.Expressions;
using CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated;

namespace CoffeesServerDB.DataBase.DTO
{
    public class ProductDTO
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public int Calories { get; set; }
        public int Cost { get; set; }
        public string Picture { get; set; }
        public string Subcategory { get; set; }
        public string Category { get; set; }
        public string Menu { get; set; }
    }
    
    public static partial class DTOHelper
    {
        public static readonly Expression<Func<Product, ProductDTO>> AsProductDTO = x => new ProductDTO
        {
            Id = x.Id,
            Name = x.Name,
            Calories = x.Calories,
            Category = x.Subcategory.Category.Name,
            Subcategory = x.Subcategory.Name,
            Cost = x.Cost,
            Menu = x.Subcategory.Category.Menu.Name,
            Picture = x.Picture
        };
    }
}