using System;

namespace CoffeesServerDB.Entity.UserStuff
{
    public class User : BaseEntity
    {
        public string Email { get; set; }
        public string Password { get; set; }
        public string Username { get; set; }
        public string Photo { get; set; }
        public Guid Card_id { get; set; }
    }
}