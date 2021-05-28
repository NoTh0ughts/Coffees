using MongoDB.Bson.Serialization.Attributes;

namespace CoffeesServerDB.DataBase.Entity.UserStuff
{
    public class ProductItem
    {
        [BsonElement("count")]
        public int Count { get; set; }
        [BsonElement("product_id")]
        public int Product_id { get; set; }
    }
}