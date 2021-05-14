using Microsoft.EntityFrameworkCore;

namespace CoffeesServerDB.DataBase.Entity.Products
{
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