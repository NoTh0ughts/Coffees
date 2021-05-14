using System.ComponentModel.DataAnnotations;

namespace CoffeesServerDB.DataBase.Entity
{
    public abstract class BaseEntity
    {
        [Key]
        public int Id { get; set; }
    }
}