using System.ComponentModel.DataAnnotations;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace CoffeesServerDB.DataBase.Entity
{
    /// <summary> Базовый класс сущности в MongoDB </summary>
    public abstract class BaseEntityMongo
    {
        [BsonElement("_id"), BsonId, BsonRepresentation(BsonType.ObjectId)]
        public string _ID { get; set; }
        
        [BsonElement("id")]
        public int Id { get; set; }
    }
}