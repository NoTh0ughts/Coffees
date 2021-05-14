using System;
using Microsoft.EntityFrameworkCore;

namespace CoffeesServerDB.DataBase.Entity.Products
{
    [Obsolete("Not used any more", true)]
    public class ProductContext : DbContext
    {
        public ProductContext(DbContextOptions<ProductContext> opt) : base(opt)
        {
            
        }
        
        public DbSet<Category> Categories;
        public DbSet<Component> Components;
        public DbSet<Ingredient> Ingredients;
        public DbSet<Menu> Menus;
        public DbSet<Product> Products;
        public DbSet<Subcategory> Subcategories;
    }
}