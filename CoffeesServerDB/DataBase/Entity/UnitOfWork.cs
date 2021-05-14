using System.Linq;
using System.Threading.Tasks;
using CoffeesServerDB.DataBase.Repositoryes;

namespace CoffeesServerDB.DataBase.Entity
{
    public class UnitOfWork
    {
        

        public void Commit()
        {
            
        }
        
        public void Dispose()
        {
            throw new System.NotImplementedException();
        }

        public GenericRepository<TEntity> GetRepository<TEntity>(bool hasCustomRepository = false) where TEntity : class
        {
            throw new System.NotImplementedException();
        }

        public int SaveChanges(bool ensureAutoHistory = false)
        {
            throw new System.NotImplementedException();
        }

        public Task<int> SaveChangesAsync(bool ensureAutoHistory = false)
        {
            throw new System.NotImplementedException();
        }

        public int ExecuteSqlCommand(string sql, params object[] parameters)
        {
            throw new System.NotImplementedException();
        }

        public IQueryable<TEntity> FromSql<TEntity>(string sql, params object[] parameters) where TEntity : class
        {
            throw new System.NotImplementedException();
        }

        
        
        
        public Task<int> SaveChangesAsync(bool ensureAutoHistory = false, params IUnitOfWork[] unitOfWorks)
        {
            throw new System.NotImplementedException();
        }
    }
}