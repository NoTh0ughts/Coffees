#nullable disable

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated
{
    public partial class Worker
    {
        public int Id { get; set; }
        public string Fullname { get; set; }
        public int Salary { get; set; }
        public int PostId { get; set; }
        public int CafeId { get; set; }

        public virtual Cafe Cafe { get; set; }
        public virtual Post Post { get; set; }
    }
}
