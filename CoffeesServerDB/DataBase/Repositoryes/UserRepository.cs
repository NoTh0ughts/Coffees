using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Threading.Tasks;
using CoffeesServerDB.DataBase.Entity.UserStuff;

namespace CoffeesServerDB.DataBase.Repositoryes
{
    public class UserRepository : IRepository<UserContext>
    {
        public void Dispose()
        {
            throw new NotImplementedException();
        }

        public IEnumerable<UserContext> GetItemList()
        {
            throw new NotImplementedException();
        }

        public UserContext GetItemById()
        {
            throw new NotImplementedException();
        }

        public UserContext Get(
            Expression<Func<UserContext, bool>> filter = null,
            Func<IQueryable<UserContext>, 
                IOrderedQueryable<UserContext>> orderBy = null, 
            string includeProperties = "")
        {
            throw new NotImplementedException();
        }

        public void Create(UserContext item)
        {
            throw new NotImplementedException();
        }

        public void Update(UserContext item)
        {
            throw new NotImplementedException();
        }

        public void Delete(UserContext item)
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