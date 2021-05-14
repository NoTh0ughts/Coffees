#nullable disable

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated
{
    public partial class CafeEquipment
    {
        public int CafeId { get; set; }
        public int EquipmentId { get; set; }
        public int Count { get; set; }

        public virtual Cafe Cafe { get; set; }
        public virtual Equipment Equipment { get; set; }
    }
}
