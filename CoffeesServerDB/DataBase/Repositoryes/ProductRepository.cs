using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Threading.Tasks;
using CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated;

namespace CoffeesServerDB.DataBase.Repositoryes
{
    public class ProductRepository : IRepository<ProductMariaContext>
    {
        public ProductMariaContext Context;


        public void Dispose()
        {
            throw new NotImplementedException();
        }

        public IEnumerable<ProductMariaContext> GetItemList()
        {
            throw new NotImplementedException();
        }

        public ProductMariaContext GetItemById()
        {
            throw new NotImplementedException();
        }

        public ProductMariaContext Get(Expression<Func<ProductMariaContext, bool>> filter = null, Func<IQueryable<ProductMariaContext>, IOrderedQueryable<ProductMariaContext>> orderBy = null, string includeProperties = "")
        {
            throw new NotImplementedException();
        }

        public void Create(ProductMariaContext item)
        {
            throw new NotImplementedException();
        }

        public void Update(ProductMariaContext item)
        {
            throw new NotImplementedException();
        }

        public void Delete(ProductMariaContext item)
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