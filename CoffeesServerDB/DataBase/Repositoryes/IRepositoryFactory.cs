namespace CoffeesServerDB.DataBase.Repositoryes
{
    /// <summary>
    /// Интефейс для реализации главного класса, управляющего репозиториями одного контекста
    /// </summary>
    public interface IRepositoryFactory
    {
       public IGenericRepository<TEntity> GetRepository<TEntity>(bool hasCustomRepository = false) where TEntity : class; 
    }
}