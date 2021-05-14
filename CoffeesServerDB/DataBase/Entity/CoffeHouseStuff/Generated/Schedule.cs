using System;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated
{
    public partial class Schedule
    {
        public int Id { get; set; }
        public int DateStart { get; set; }
        public int DateEnd { get; set; }
        public TimeSpan TimeStart { get; set; }
        public TimeSpan TimeEnd { get; set; }
        public int CafeId { get; set; }

        public virtual Cafe Cafe { get; set; }
    }
}
