namespace CoffeesServerDB.DataBase.Repositoryes
{
    public interface IRepositoryFactory
    {
       public IGenericRepository<TEntity> GetRepository<TEntity>(bool hasCustomRepository = false) where TEntity : class; 
    }
}