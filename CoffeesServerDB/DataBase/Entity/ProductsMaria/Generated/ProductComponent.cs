#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated
{
    public partial class ProductComponent
    {
        public int ProductId { get; set; }
        public int ComponentId { get; set; }
        public float Dose { get; set; }

        public virtual ProductsSqlServer.Generated.Component Component { get; set; }
        public virtual ProductsSqlServer.Generated.Product Product { get; set; }
    }
}
