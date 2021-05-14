using Microsoft.EntityFrameworkCore;

namespace CoffeesServerDB.DataBase.Entity.UserStuff
{
    public class UserContext : DbContext
    {
        public UserContext(DbContextOptions<UserContext> opt) : base(opt)
        {
            
        }
        
        public DbSet<Card> Cards;
        public DbSet<Order> Orders;
        public DbSet<Status> Status;
        public DbSet<User> Users;

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
        }
    }
}