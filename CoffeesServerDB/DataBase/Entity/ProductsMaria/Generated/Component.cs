using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated
{
    public partial class Component
    {
        public Component()
        {
            ProductComponents = new HashSet<ProductsSqlServer.Generated.ProductComponent>();
        }

        public int Id { get; set; }
        public string Name { get; set; }

        public virtual ICollection<ProductsSqlServer.Generated.ProductComponent> ProductComponents { get; set; }
    }
}
