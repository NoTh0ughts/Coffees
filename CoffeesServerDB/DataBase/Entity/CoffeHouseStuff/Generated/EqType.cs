using System.Collections.Generic;

#nullable disable

namespace CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated
{
    public partial class EqType
    {
        public EqType()
        {
            Equipment = new HashSet<Equipment>();
        }

        public int Id { get; set; }
        public string Name { get; set; }

        public virtual ICollection<Equipment> Equipment { get; set; }
    }
}
