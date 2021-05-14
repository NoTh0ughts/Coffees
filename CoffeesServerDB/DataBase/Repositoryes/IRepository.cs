using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Threading.Tasks;

namespace CoffeesServerDB.DataBase.Repositoryes
{
    public interface IRepository<T> : IDisposable 
                    where T : class
    {
        IEnumerable<T> GetItemList();
        T GetItemById();

        T Get(Expression<Func<T, bool>> filter = null,
            Func<IQueryable<T>, IOrderedQueryable<T>> orderBy = null,
            string includeProperties = "");
        
        
        void Create(T item);
        void Update(T item);
        void Delete(T item);
        void Save();
        Task SaveAsync();
    }
}