using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Threading.Tasks;
using CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated;

namespace CoffeesServerDB.DataBase.Repositoryes
{
    public class ProductRepository : IRepository<databaseContext>
    {
        public databaseContext Context;


        public void Dispose()
        {
            throw new NotImplementedException();
        }

        public IEnumerable<databaseContext> GetItemList()
        {
            throw new NotImplementedException();
        }

        public databaseContext GetItemById()
        {
            throw new NotImplementedException();
        }

        public databaseContext Get(Expression<Func<databaseContext, bool>> filter = null, Func<IQueryable<databaseContext>, IOrderedQueryable<databaseContext>> orderBy = null, string includeProperties = "")
        {
            throw new NotImplementedException();
        }

        public void Create(databaseContext item)
        {
            throw new NotImplementedException();
        }

        public void Update(databaseContext item)
        {
            throw new NotImplementedException();
        }

        public void Delete(databaseContext item)
        {
            throw new NotImplementedException();
        }

        public void Save()
        {
            throw new NotImplementedException();
        }

        public Task SaveAsync()
        {
            throw new NotImplementedException();
        }
    }
}