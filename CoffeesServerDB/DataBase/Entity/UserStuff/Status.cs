using MongoDB.Bson.Serialization.Attributes;

namespace CoffeesServerDB.DataBase.Entity.UserStuff
{
    public class Status : BaseEntityMongo
    {
        [BsonElement("status")]
        public string Title { get; set; }
    }
}