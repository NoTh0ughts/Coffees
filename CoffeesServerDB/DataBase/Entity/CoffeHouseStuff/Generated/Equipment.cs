using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated
{
    public partial class Equipment
    {
        public Equipment()
        {
            CafeEquipments = new HashSet<CafeEquipment>();
        }

        public int Id { get; set; }
        public string Model { get; set; }
        public int BrandId { get; set; }
        public int EqTypeId { get; set; }

        public virtual Brand Brand { get; set; }
        public virtual EqType EqType { get; set; }
        public virtual ICollection<CafeEquipment> CafeEquipments { get; set; }
    }
}
