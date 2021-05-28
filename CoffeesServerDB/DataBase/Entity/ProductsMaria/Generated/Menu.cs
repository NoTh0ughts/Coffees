using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated
{
    public partial class Menu
    {
        public Menu()
        {
            Categories = new HashSet<ProductsSqlServer.Generated.Category>();
        }

        public int Id { get; set; }
        public string Name { get; set; }

        public virtual ICollection<ProductsSqlServer.Generated.Category> Categories { get; set; }
    }
}
