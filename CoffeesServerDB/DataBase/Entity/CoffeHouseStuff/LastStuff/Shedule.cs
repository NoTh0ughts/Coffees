using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.LastStuff
{
    [Obsolete("Not used any more", true)]
    public class Shedule : BaseEntity
    {
        public int Date_start { get; set; }
        public int Date_end { get; set; }
        
        [DataType(DataType.Time)]
        public DateTime Start_time { get; set; }
        [DataType(DataType.Time)]
        public DateTime End_time { get; set; }
        
        [ForeignKey("Cafe")]
        public int Cafe_id { get; set; }
        public virtual Cafe Cafe { get; set; }
    }
}