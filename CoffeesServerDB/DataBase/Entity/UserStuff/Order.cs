using System;
using MongoDB.Bson.Serialization.Attributes;

namespace CoffeesServerDB.DataBase.Entity.UserStuff
{
    public class Order : BaseEntityMongo
    {
        [BsonElement("cafe_id")]
        public int Cafe_id { get; set; }
        [BsonElement("stuff")]
        public ProductItem[] stuff { get; set; }
        [BsonElement("date_order")]
        public DateTime Date_order { get; set; }
        [BsonElement("user_id")]
        public int User_id { get; set; }
        [BsonElement("status_id")]
        public int Status_id { get; set; }
    }
}