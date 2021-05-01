using System;

namespace CoffeesServerDB.Entity.CoffeHouseStuff
{
    public class Shedule : BaseEntity
    {
        public int Date_start { get; set; }
        public int Date_end { get; set; }
        public TimeSpan Start_time { get; set; }
        public TimeSpan End_time { get; set; }
        public Guid Cafe_id { get; set; }
    }
}