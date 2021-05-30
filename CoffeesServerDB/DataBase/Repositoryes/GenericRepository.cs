using System.Collections.Generic;
using System.Linq;
using Microsoft.EntityFrameworkCore;

namespace CoffeesServerDB.DataBase.Repositoryes
{
    public class GenericRepository<TEntity> : IGenericRepository<TEntity> where TEntity : class
    {
        internal DbContext context;
        internal DbSet<TEntity> dbSet;

        public GenericRepository(DbContext context)
        {
            this.context = context;
            this.dbSet = context.Set<TEntity>();
        }

        public GenericRepository()
        {
            
        }


        public void Save() => context.SaveChanges();
        public void SaveAsync() => context.SaveChangesAsync();
        public void Dispose() => context.SaveChanges();
        public ICollection<TEntity> GetAll() => dbSet.ToList();
        public TEntity GetById(int id) => dbSet.Find(id);
        public void Remove(TEntity item)
        {
            dbSet.Remove(item);
            context.SaveChanges();
        }

        public TEntity Create(TEntity newItem)
        {
            var newEntity = dbSet.Add(newItem).Entity;
            context.SaveChanges();
            return newEntity;
        }

        public void Update(TEntity replacedItem)
        {
            context.Entry(replacedItem).State = EntityState.Modified;
            context.SaveChanges();
        }
    }
}