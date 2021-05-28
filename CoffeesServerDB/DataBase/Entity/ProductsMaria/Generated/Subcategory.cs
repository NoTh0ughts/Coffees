using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated
{
    public partial class Subcategory
    {
        public Subcategory()
        {
            Products = new HashSet<ProductsSqlServer.Generated.Product>();
        }

        public int Id { get; set; }
        public string Name { get; set; }
        public int CategoryId { get; set; }

        public virtual ProductsSqlServer.Generated.Category Category { get; set; }
        public virtual ICollection<ProductsSqlServer.Generated.Product> Products { get; set; }
    }
}
