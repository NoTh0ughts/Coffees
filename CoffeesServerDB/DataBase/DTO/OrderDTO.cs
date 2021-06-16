using System;
using CoffeesServerDB.DataBase.Entity.UserStuff;

namespace CoffeesServerDB.DataBase.DTO
{
    public class OrderDTO
    {
        public int Id { get; set; }
        public int Cafe_id { get; set; }
        public ProductItem[] stuff { get; set; }
        public DateTime Date_order { get; set; }
        public int User_id { get; set; }
        public int Status_id { get; set; }
        public string Status { get; set; }
        public string Username { get; set; }
    }

    public static partial class DTOHelper
    {
        public static OrderDTO ToDto(this Order order, User user, Status status)
        {
            return new OrderDTO
            {
                Id = order.Id,
                Cafe_id = order.Cafe_id,
                Date_order = order.Date_order,
                Status = status.Title,
                Status_id = status.Id,
                stuff = order.stuff,
                User_id = user.Id,
                Username = user.Username
            };
        }

        public static Order ToOrder(this OrderDTO orderDto)
        {
            return new Order
            {
                
            };
        }
    }
}