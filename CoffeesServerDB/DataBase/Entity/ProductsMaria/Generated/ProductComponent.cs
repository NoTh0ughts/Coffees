using System.ComponentModel.DataAnnotations;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated
{
    public partial class ProductComponent
    {
        [Key]
        public int ProductId { get; set; }
        [Key]
        public int ComponentId { get; set; }
        public float Dose { get; set; }

        public virtual Component Component { get; set; }
        public virtual Product Product { get; set; }
    }
}
