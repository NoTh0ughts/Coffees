using System;
using System.Collections.Generic;
using System.Linq;
using CoffeesServerDB.Service;
using MongoDB.Driver;
using MongoDB.Driver.Linq;

namespace CoffeesServerDB.DataBase.Entity.UserStuff
{
    public interface IMongoConfig { string ConnectionString { get; set; } }
    public class MongoConfig : IMongoConfig { public string ConnectionString { get; set; } }
    
    public class UserService
    {
        private readonly IMongoCollection<Order> _orders;
        private readonly IMongoCollection<User> _users;
        private readonly IMongoCollection<Card> _cards;
        private readonly IMongoCollection<Status> _status;
        
        public UserService(IMongoConfig config)
        {
            var client = new MongoClient(ConfigLoader.MongoURL);
            var db = client.GetDatabase("database");

            _orders = db.GetCollection<Order>("order");
            _users = db.GetCollection<User>("user");
            _cards = db.GetCollection<Card>("card");
            _status = db.GetCollection<Status>("status");
        }
        
        public ICollection<Order> Get() => IAsyncCursorSourceExtensions.ToList((from o in _orders.AsQueryable() select o));

        public ICollection<Order> GetOrdersByUser(int userId) =>
            IAsyncCursorSourceExtensions.ToList((from o in _orders.AsQueryable() where o.User_id == userId select o));

        public User GetUserByOrder(int order_id) => (from u in _users.AsQueryable().AsEnumerable() 
                    join o in _orders.AsQueryable() on u.Id equals o.User_id  
                where o.Id == order_id 
                select u)
            .First();
        
        public Card GetCardByUser(int userId) => IAsyncCursorSourceExtensions.First((from u in _users.AsQueryable()
            join c in _cards.AsQueryable() on u.Card_id equals c.Id
            where u.Id == userId
            select c));

        public string GetOrderStatus(int order_id) => IAsyncCursorSourceExtensions.First((from s in _status.AsQueryable()
            join o in _orders.AsQueryable() on s.Id equals o.Status_id
            where o.Id == order_id
            select new {s.Title})).ToString();
        
        #region CRUD User
        public void RemoveUser(int userId)
        {
            _users.DeleteOne(u => u.Id == userId);
        }

        public void UpdateUser(int userId, User newUser)
        {
            _users.ReplaceOne(u => u.Id == userId, newUser);
        }

        public User AddUser(User newUser)
        {
            _users.InsertOne(newUser);
            return newUser;
        }
        #endregion

        #region CRUD Order

        public void RemoveOrder(int orderId)
        {
            _orders.DeleteOne(o => o.Id == orderId);
        }

        public void UpdateOrder(int orderId, Order order)
        {
            _orders.ReplaceOne(o => o.Id == orderId, order);
        }

        public Order AddOrder(Order newOrder)
        {
            _orders.InsertOne(newOrder);
            return newOrder;
        }

        #endregion
    }
}