using System;

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.LastStuff
{
    [Obsolete("Not used any more", true)]
    public class Worker : BaseEntity
    {
        public string Fullname { get; set; }
        public int Salary { get; set; }
        
        public int Post_id { get; set; }
        public int Cafe_id { get; set; }
        public virtual Post Post { get; set; } 
        public virtual Cafe Cafe { get; set; }
    }
        
}