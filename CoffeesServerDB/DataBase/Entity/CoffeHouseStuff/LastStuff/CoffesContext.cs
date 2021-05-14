using System;
using Microsoft.EntityFrameworkCore;

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.LastStuff
{
    [Obsolete("Not used any more", true)]
    public class CoffesContext : DbContext
    {
        public CoffesContext(DbContextOptions<CoffesContext> opt) : base(opt)
        {
            
        }

        public CoffesContext()
        {
            
        }
        
        
        public DbSet<Generated.Brand> Brands;
        public DbSet<Generated.Cafe> Cafes;
        public DbSet<Generated.City> Citys;
        public DbSet<Generated.Equipment> Eqipments;
        public DbSet<Generated.EqType> EqTypes;
        public DbSet<Generated.Post> Posts;
        public DbSet<Shedule> Shedules;
        public DbSet<Generated.Worker> Workers;

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseNpgsql(
                @"Server=192.168.231.3; Port=5432; Database=coffees_cafes; User Id=postgres; Password=ThisIsIVT361");
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            /*modelBuilder
                .Entity<Cafe>()
                .HasMany(c => c.Equipments)
                .WithMany(e => e.Cafes)
                .UsingEntity<CafeEquipment>(
                    j => j
                        .HasOne(pt => pt.Equipment)
                        .WithMany(t => t.CafeEquipments)
                        .HasForeignKey(pt => pt.Equipment_id),  //Foreign key Equipment
                    j => j
                        .HasOne(pt => pt.Cafe)
                        .WithMany(p => p.CafeEquipments)
                        .HasForeignKey(p => p.Cafe_id),         //Foreign key Cafe
                    j =>
                    {
                        j.Property(pt => pt.Count).HasDefaultValue(0); // Add field Count to new table
                        j.HasKey(t => new {t.Cafe_id, t.Equipment_id});
                        j.ToTable("CafeEquipment");
                    });*/
        }
    }
}