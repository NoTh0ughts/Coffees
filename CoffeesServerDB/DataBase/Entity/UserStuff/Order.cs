using System;

namespace CoffeesServerDB.DataBase.Entity.UserStuff
{
    public class Order : BaseEntity
    {
        public int Cafe_id { get; set; }
        public string stuff { get; set; }
        public DateTime Date_order { get; set; }
        public int User_id { get; set; }
        public int Status_id { get; set; }
        
        
    }
}