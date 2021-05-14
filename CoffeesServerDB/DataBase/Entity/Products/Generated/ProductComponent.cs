using System;
using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB
{
    public partial class ProductComponent
    {
        public int ProductId { get; set; }
        public int ComponentId { get; set; }
        public float Dose { get; set; }

        public virtual Component Component { get; set; }
        public virtual Product Product { get; set; }
    }
}
