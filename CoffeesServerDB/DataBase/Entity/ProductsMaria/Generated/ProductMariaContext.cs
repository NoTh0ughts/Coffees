using CoffeesServerDB.Service;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated
{
    public partial class ProductMariaContext : DbContext
    {
        public ProductMariaContext()
        {
        }

        public ProductMariaContext(DbContextOptions<ProductMariaContext> options)
            : base(options)
        {
        }

        public virtual DbSet<ProductsSqlServer.Generated.Category> Categories { get; set; }
        public virtual DbSet<ProductsSqlServer.Generated.Component> Components { get; set; }
        public virtual DbSet<ProductsSqlServer.Generated.Ingredient> Ingredients { get; set; }
        public virtual DbSet<ProductsSqlServer.Generated.Menu> Menus { get; set; }
        public virtual DbSet<ProductsSqlServer.Generated.Product> Products { get; set; }
        public virtual DbSet<ProductsSqlServer.Generated.ProductComponent> ProductComponents { get; set; }
        public virtual DbSet<ProductsSqlServer.Generated.ProductIngredient> ProductIngredients { get; set; }
        public virtual DbSet<ProductsSqlServer.Generated.Subcategory> Subcategories { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseMySql(ConfigLoader.MariaURL + "treattinyasboolean=true",
                    ServerVersion.Parse("10.5.9-mariadb"));
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.HasCharSet("utf8mb4")
                .UseCollation("utf8mb4_general_ci");

            modelBuilder.Entity<ProductsSqlServer.Generated.Category>(entity =>
            {
                entity.ToTable("category");

                entity.HasIndex(e => e.MenuId, "fk_menu_id1");

                entity.Property(e => e.Id)
                    .HasColumnType("int(11)")
                    .HasColumnName("id");

                entity.Property(e => e.MenuId)
                    .HasColumnType("int(11)")
                    .HasColumnName("menu_id");

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(50)
                    .HasColumnName("name");

                entity.HasOne(d => d.Menu)
                    .WithMany(p => p.Categories)
                    .HasForeignKey(d => d.MenuId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_menu_id1");
            });

            modelBuilder.Entity<ProductsSqlServer.Generated.Component>(entity =>
            {
                entity.ToTable("component");

                entity.Property(e => e.Id)
                    .HasColumnType("int(11)")
                    .HasColumnName("id");

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(100)
                    .HasColumnName("name");
            });

            modelBuilder.Entity<ProductsSqlServer.Generated.Ingredient>(entity =>
            {
                entity.ToTable("ingredient");

                entity.Property(e => e.Id)
                    .HasColumnType("int(11)")
                    .HasColumnName("id");

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(100)
                    .HasColumnName("name");
            });

            modelBuilder.Entity<ProductsSqlServer.Generated.Menu>(entity =>
            {
                entity.ToTable("menu");

                entity.Property(e => e.Id)
                    .HasColumnType("int(11)")
                    .HasColumnName("id");

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(50)
                    .HasColumnName("name");
            });

            modelBuilder.Entity<ProductsSqlServer.Generated.Product>(entity =>
            {
                entity.ToTable("product");

                entity.HasIndex(e => e.SubcategoryId, "fk_subcategory_id_1");

                entity.Property(e => e.Id)
                    .HasColumnType("int(11)")
                    .HasColumnName("id");

                entity.Property(e => e.Calories)
                    .HasColumnType("int(11)")
                    .HasColumnName("calories");

                entity.Property(e => e.Cost)
                    .HasColumnType("int(11)")
                    .HasColumnName("cost");

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(100)
                    .HasColumnName("name");

                entity.Property(e => e.Picture)
                    .IsRequired()
                    .HasMaxLength(255)
                    .HasColumnName("picture");

                entity.Property(e => e.SubcategoryId)
                    .HasColumnType("int(11)")
                    .HasColumnName("subcategory_id");

                RelationalForeignKeyBuilderExtensions.HasConstraintName((ReferenceCollectionBuilder) entity.HasOne(d => d.Subcategory)
                    .WithMany(p => p.Products)
                    .HasForeignKey(d => d.SubcategoryId), "fk_subcategory_id_1");
            });

            modelBuilder.Entity<ProductsSqlServer.Generated.ProductComponent>(entity =>
            {
                entity.HasKey(e => new { e.ProductId, e.ComponentId })
                    .HasName("PRIMARY")
                    .HasAnnotation("MySql:IndexPrefixLength", new[] { 0, 0 });

                entity.ToTable("product_component");

                entity.HasIndex(e => e.ComponentId, "fk_fact_type_id_1");

                entity.Property(e => e.ProductId)
                    .HasColumnType("int(11)")
                    .HasColumnName("product_id");

                entity.Property(e => e.ComponentId)
                    .HasColumnType("int(11)")
                    .HasColumnName("component_id");

                entity.Property(e => e.Dose).HasColumnName("dose");

                RelationalForeignKeyBuilderExtensions.HasConstraintName((ReferenceCollectionBuilder) entity.HasOne(d => d.Component)
                    .WithMany(p => p.ProductComponents)
                    .HasForeignKey(d => d.ComponentId)
                    .OnDelete(DeleteBehavior.ClientSetNull), "fk_fact_type_id_1");

                RelationalForeignKeyBuilderExtensions.HasConstraintName((ReferenceCollectionBuilder) entity.HasOne(d => d.Product)
                    .WithMany(p => p.ProductComponents)
                    .HasForeignKey(d => d.ProductId)
                    .OnDelete(DeleteBehavior.ClientSetNull), "fk_product_id_2");
            });

            modelBuilder.Entity<ProductsSqlServer.Generated.ProductIngredient>(entity =>
            {
                entity.HasKey(e => new { e.IngredientId, e.ProductId })
                    .HasName("PRIMARY")
                    .HasAnnotation("MySql:IndexPrefixLength", new[] { 0, 0 });

                entity.ToTable("product_ingredient");

                entity.HasIndex(e => e.ProductId, "fk_product_id_1");

                entity.Property(e => e.IngredientId)
                    .HasColumnType("int(11)")
                    .HasColumnName("ingredient_id");

                entity.Property(e => e.ProductId)
                    .HasColumnType("int(11)")
                    .HasColumnName("product_id");

                RelationalForeignKeyBuilderExtensions.HasConstraintName((ReferenceCollectionBuilder) entity.HasOne(d => d.Ingredient)
                    .WithMany(p => p.ProductIngredients)
                    .HasForeignKey(d => d.IngredientId)
                    .OnDelete(DeleteBehavior.ClientSetNull), "fk_ingredient_id_1");

                RelationalForeignKeyBuilderExtensions.HasConstraintName((ReferenceCollectionBuilder) entity.HasOne(d => d.Product)
                    .WithMany(p => p.ProductIngredients)
                    .HasForeignKey(d => d.ProductId)
                    .OnDelete(DeleteBehavior.ClientSetNull), "fk_product_id_1");
            });

            modelBuilder.Entity<ProductsSqlServer.Generated.Subcategory>(entity =>
            {
                entity.ToTable("subcategory");

                entity.HasIndex(e => e.CategoryId, "fk_category_id_1");

                entity.Property(e => e.Id)
                    .HasColumnType("int(11)")
                    .HasColumnName("id");

                entity.Property(e => e.CategoryId)
                    .HasColumnType("int(11)")
                    .HasColumnName("category_id");

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(50)
                    .HasColumnName("name");

                RelationalForeignKeyBuilderExtensions.HasConstraintName((ReferenceCollectionBuilder) entity.HasOne(d => d.Category)
                    .WithMany(p => p.Subcategories)
                    .HasForeignKey(d => d.CategoryId)
                    .OnDelete(DeleteBehavior.ClientSetNull), "fk_category_id_1");
            });

            OnModelCreatingPartial(modelBuilder);
        }
        
        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
