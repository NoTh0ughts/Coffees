using MongoDB.Bson.Serialization.Attributes;

namespace CoffeesServerDB.DataBase.Entity.UserStuff
{
    public class User : BaseEntityMongo
    {
        [BsonElement("email")]
        public string Email { get; set; }
        [BsonElement("password")]
        public string Password { get; set; }
        [BsonElement("username")]
        public string Username { get; set; }
        [BsonElement("photo")]
        public string Photo { get; set; }
        [BsonElement("card_id")]
        public int Card_id { get; set; }
        [BsonElement("favorites")]
        public int[] Favorites { get; set; }
    }
}