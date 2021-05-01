using System;

namespace CoffeesServerDB.Entity.CoffeHouseStuff
{
    public class Worker : BaseEntity
    {
        public string Fullname { get; set; }
        public int Salary { get; set; }
        public Guid Post_id { get; set; }
        public Guid Cafe_id { get; set; }
    }
}