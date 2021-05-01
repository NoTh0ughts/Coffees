using System;

namespace CoffeesServerDB.Entity.UserStuff
{
    public class Order : BaseEntity
    {
        public Guid Cafe_id { get; set; }
        public string stuff { get; set; }
        public DateTime Date_order { get; set; }
        public Guid User_id { get; set; }
        public Guid Status_id { get; set; }
    }
}