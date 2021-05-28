using System.ComponentModel.DataAnnotations;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace CoffeesServerDB.DataBase.Entity
{
    public abstract class BaseEntity
    {
        [Key]
        public int Id { get; set; }
    }

    public abstract class BaseEntityMongo
    {
        [BsonElement("_id"), BsonId, BsonRepresentation(BsonType.ObjectId)]
        public string _ID { get; set; }
        
        [BsonElement("id")]
        public int Id { get; set; }
    }
}