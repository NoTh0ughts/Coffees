﻿using System;
using System.Collections.Generic;

namespace CoffeesServerDB.DataBase.Repositoryes
{
    public interface IGenericRepository<T> : IRepository<T> where T : class
    {

    }

    public interface IRepository<T> : IDisposable where T : class
    {
        public ICollection<T> GetAll();
        public T GetById(int id);
        public void Remove(T item);
        public T Create(T newItem);
        public void Update(T replacedItem);
        public void Save();
        public void SaveAsync();
    }
}