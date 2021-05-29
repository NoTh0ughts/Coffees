using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Threading.Tasks;
using CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated;

namespace CoffeesServerDB.DataBase.Repositoryes
{
    public class CoffeesRepository 
    {
        public CafeContext Context;

        public void Dispose()
        {
            throw new NotImplementedException();
        }

        public IEnumerable<CafeContext> GetItemList()
        {
            throw new NotImplementedException();
        }

        public CafeContext GetItemById()
        {
            throw new NotImplementedException();
        }

        public CafeContext Get(
            Expression<Func<CafeContext, bool>> filter = null,
            Func<IQueryable<CafeContext>, IOrderedQueryable<CafeContext>> orderBy = null,
            string includeProperties = "")
        {
            throw new NotImplementedException();
        }

        public void Create(CafeContext item)
        {
            throw new NotImplementedException();
        }

        public void Update(CafeContext item)
        {
            throw new NotImplementedException();
        }

        public void Delete(CafeContext item)
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