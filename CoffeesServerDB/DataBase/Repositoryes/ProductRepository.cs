using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Threading.Tasks;
using CoffeesServerDB.DataBase.Entity.Products;

namespace CoffeesServerDB.DataBase.Repositoryes
{
    public class ProductRepository : IRepository<ProductContext>
    {
        public ProductContext Context;
        
        public void Dispose()
        {
            throw new NotImplementedException();
        }

        public IEnumerable<ProductContext> GetItemList()
        {
            throw new NotImplementedException();
        }

        public ProductContext GetItemById()
        {
            throw new NotImplementedException();
        }

        public ProductContext Get(Expression<Func<ProductContext, bool>> filter = null, Func<IQueryable<ProductContext>, IOrderedQueryable<ProductContext>> orderBy = null, string includeProperties = "")
        {
            throw new NotImplementedException();
        }

        public void Create(ProductContext item)
        {
            throw new NotImplementedException();
        }

        public void Update(ProductContext item)
        {
            throw new NotImplementedException();
        }

        public void Delete(ProductContext item)
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