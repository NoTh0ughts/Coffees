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
        public coffees_cafesContext Context;

        public void Dispose()
        {
            throw new NotImplementedException();
        }

        public IEnumerable<coffees_cafesContext> GetItemList()
        {
            throw new NotImplementedException();
        }

        public coffees_cafesContext GetItemById()
        {
            throw new NotImplementedException();
        }

        public coffees_cafesContext Get(
            Expression<Func<coffees_cafesContext, bool>> filter = null,
            Func<IQueryable<coffees_cafesContext>, IOrderedQueryable<coffees_cafesContext>> orderBy = null,
            string includeProperties = "")
        {
            throw new NotImplementedException();
        }

        public void Create(coffees_cafesContext item)
        {
            throw new NotImplementedException();
        }

        public void Update(coffees_cafesContext item)
        {
            throw new NotImplementedException();
        }

        public void Delete(coffees_cafesContext item)
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