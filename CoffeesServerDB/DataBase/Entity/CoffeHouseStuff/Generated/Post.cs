using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated
{
    public partial class Post
    {
        public Post()
        {
            Workers = new HashSet<Worker>();
        }

        public int Id { get; set; }
        public string Name { get; set; }

        public virtual ICollection<Worker> Workers { get; set; }
    }
}
