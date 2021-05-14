using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated
{
    public partial class Cafe
    {
        public Cafe()
        {
            CafeEquipments = new HashSet<CafeEquipment>();
            Schedules = new HashSet<Schedule>();
            Workers = new HashSet<Worker>();
        }

        public int Id { get; set; }
        public string Address { get; set; }
        public int CityId { get; set; }

        public virtual City City { get; set; }
        public virtual ICollection<CafeEquipment> CafeEquipments { get; set; }
        public virtual ICollection<Schedule> Schedules { get; set; }
        public virtual ICollection<Worker> Workers { get; set; }
    }
}
