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
        public int Post_id { get; set; }
        public int Cafe_id { get; set; }
    }
    

    public static partial class DTOHelper
    {
        public static readonly Expression<Func<Worker, WorkerDTO>> AsWorkerDTO = x => new WorkerDTO
        {
            Id = x.Id,
            Fullname = x.Fullname,
            Cafe_id = x.Cafe.Id,
            Post = x.Post.Name,
            Post_id = x.PostId,
            Salary = x.Salary
        };

        public static WorkerDTO ToWorkerDTO(this Worker x) =>
            new WorkerDTO
            {
                Id = x.Id,
                Fullname = x.Fullname,
                Cafe_id = x.Cafe.Id,
                Post = x.Post.Name,
                Post_id = x.PostId,
                Salary = x.Salary
            };
    } 
}