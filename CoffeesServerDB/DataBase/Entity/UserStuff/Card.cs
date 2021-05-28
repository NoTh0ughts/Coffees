using MongoDB.Bson.Serialization.Attributes;

namespace CoffeesServerDB.DataBase.Entity.UserStuff
{
    public class Card : BaseEntityMongo
    {
        [BsonElement("discount")]
        public int Discount { get; set; }
    }
}