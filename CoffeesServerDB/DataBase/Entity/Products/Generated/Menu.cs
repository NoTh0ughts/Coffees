using System;
using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB
{
    public partial class Menu
    {
        public Menu()
        {
            Categories = new HashSet<Category>();
        }

        public int Id { get; set; }
        public string Name { get; set; }

        public virtual ICollection<Category> Categories { get; set; }
    }
}
