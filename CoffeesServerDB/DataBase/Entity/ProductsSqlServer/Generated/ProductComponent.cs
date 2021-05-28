#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated
{
    public partial class ProductComponent
    {
        public int ProductId { get; set; }
        public int ComponentId { get; set; }
        public int Dose { get; set; }

        public virtual Component Component { get; set; }
        public virtual Product Product { get; set; }
    }
}
