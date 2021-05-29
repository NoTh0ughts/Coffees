using CoffeesServerDB.Service;
using Microsoft.EntityFrameworkCore;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated
{
    public partial class CafeContext : DbContext
    {
        public CafeContext()
        {
        }

        public CafeContext(DbContextOptions<CafeContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Brand> Brands { get; set; }
        public virtual DbSet<Cafe> Caves { get; set; }
        public virtual DbSet<CafeEquipment> CafeEquipments { get; set; }
        public virtual DbSet<City> Cities { get; set; }
        public virtual DbSet<EqType> EqTypes { get; set; }
        public virtual DbSet<Equipment> Equipment { get; set; }
        public virtual DbSet<Post> Posts { get; set; }
        public virtual DbSet<Schedule> Schedules { get; set; }
        public virtual DbSet<Worker> Workers { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseNpgsql(ConfigLoader.PostgresUrl);
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.HasAnnotation("Relational:Collation", "Russian_Russia.1251");

            modelBuilder.Entity<Brand>(entity =>
            {
                entity.ToTable("brand");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(100)
                    .HasColumnName("name");
            });

            modelBuilder.Entity<Cafe>(entity =>
            {
                entity.ToTable("cafe");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.Address)
                    .IsRequired()
                    .HasMaxLength(100)
                    .HasColumnName("address");

                entity.Property(e => e.CityId).HasColumnName("city_id");

                entity.HasOne(d => d.City)
                    .WithMany(p => p.Caves)
                    .HasForeignKey(d => d.CityId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("cafe_city_id_fkey");
            });

            modelBuilder.Entity<CafeEquipment>(entity =>
            {
                entity.HasKey(e => new { e.CafeId, e.EquipmentId })
                    .HasName("cafe_equipment_pkey");

                entity.ToTable("cafe_equipment");

                entity.Property(e => e.CafeId).HasColumnName("cafe_id");

                entity.Property(e => e.EquipmentId).HasColumnName("equipment_id");

                entity.Property(e => e.Count)
                    .HasColumnName("count")
                    .HasDefaultValueSql("1");

                entity.HasOne(d => d.Cafe)
                    .WithMany(p => p.CafeEquipments)
                    .HasForeignKey(d => d.CafeId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("cafe_equipment_cafe_id_fkey");

                entity.HasOne(d => d.Equipment)
                    .WithMany(p => p.CafeEquipments)
                    .HasForeignKey(d => d.EquipmentId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("cafe_equipment_equipment_id_fkey");
            });

            modelBuilder.Entity<City>(entity =>
            {
                entity.ToTable("city");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(100)
                    .HasColumnName("name");
            });

            modelBuilder.Entity<EqType>(entity =>
            {
                entity.ToTable("eq_type");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(100)
                    .HasColumnName("name");
            });

            modelBuilder.Entity<Equipment>(entity =>
            {
                entity.ToTable("equipment");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.BrandId).HasColumnName("brand_id");

                entity.Property(e => e.EqTypeId).HasColumnName("eq_type_id");

                entity.Property(e => e.Model)
                    .IsRequired()
                    .HasMaxLength(100)
                    .HasColumnName("model");

                entity.HasOne(d => d.Brand)
                    .WithMany(p => p.Equipment)
                    .HasForeignKey(d => d.BrandId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("equipment_brand_id_fkey");

                entity.HasOne(d => d.EqType)
                    .WithMany(p => p.Equipment)
                    .HasForeignKey(d => d.EqTypeId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("equipment_eq_type_id_fkey");
            });

            modelBuilder.Entity<Post>(entity =>
            {
                entity.ToTable("post");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.Name)
                    .HasMaxLength(50)
                    .HasColumnName("name");
            });

            modelBuilder.Entity<Schedule>(entity =>
            {
                entity.ToTable("schedule");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.CafeId).HasColumnName("cafe_id");

                entity.Property(e => e.DateEnd).HasColumnName("date_end");

                entity.Property(e => e.DateStart).HasColumnName("date_start");

                entity.Property(e => e.TimeEnd)
                    .HasColumnType("time without time zone")
                    .HasColumnName("time_end")
                    .HasDefaultValueSql("'12:00:00'::time without time zone");

                entity.Property(e => e.TimeStart)
                    .HasColumnType("time without time zone")
                    .HasColumnName("time_start")
                    .HasDefaultValueSql("'12:00:00'::time without time zone");

                entity.HasOne(d => d.Cafe)
                    .WithMany(p => p.Schedules)
                    .HasForeignKey(d => d.CafeId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("schedule_cafe_id_fkey");
            });

            modelBuilder.Entity<Worker>(entity =>
            {
                entity.ToTable("worker");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.CafeId).HasColumnName("cafe_id");

                entity.Property(e => e.Fullname)
                    .IsRequired()
                    .HasMaxLength(100)
                    .HasColumnName("fullname");

                entity.Property(e => e.PostId).HasColumnName("post_id");

                entity.Property(e => e.Salary).HasColumnName("salary");

                entity.HasOne(d => d.Cafe)
                    .WithMany(p => p.Workers)
                    .HasForeignKey(d => d.CafeId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("worker_cafe_id_fkey");

                entity.HasOne(d => d.Post)
                    .WithMany(p => p.Workers)
                    .HasForeignKey(d => d.PostId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("worker_post_id_fkey");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
