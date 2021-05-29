using System;
using System.Linq.Expressions;
using CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated;

namespace CoffeesServerDB.DataBase.DTO
{
    public class WorkerDTO
    {
        public int Id { get; set; }
        public string Fullname { get; set; }
        public int Salary { get; set; }
        public string Post { get; set; }
        public string Address { get; set; }
        public int cafe_id { get; set; }
    }
    

    public static partial class DTOHelper
    {
        public static readonly Expression<Func<Worker, WorkerDTO>> AsWorkerDTO = x => new WorkerDTO
        {
            Id = x.Id,
            Address = x.Cafe.Address,
            Fullname = x.Fullname,
            cafe_id = x.Cafe.Id,
            Post = x.Post.Name,
            Salary = x.Salary
        };
    } 
}