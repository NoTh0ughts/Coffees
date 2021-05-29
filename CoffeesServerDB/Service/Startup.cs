using System;
using System.Collections.Generic;
using CoffeesServerDB.DataBase.Entity;
using CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated;
using CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated;
using CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated;
using CoffeesServerDB.DataBase.Entity.UserStuff;
using CoffeesServerDB.DataBase.Repositoryes;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Options;
using Microsoft.OpenApi.Models;
using Category = CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated.Category;
using Component = CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated.Component;
using Ingredient = CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated.Ingredient;
using Menu = CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated.Menu;
using Product = CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated.Product;
using Subcategory = CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated.Subcategory;


namespace CoffeesServerDB.Service
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }
        
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddControllers();
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1",
                    new OpenApiInfo
                    {
                        Title = "Coffees",
                        Version = "v1.00",
                        Contact = new OpenApiContact()
                        {
                            Name = "Repository",
                            Email = "",
                            Url = new Uri("https://github.com/TakeMe100Points/Coffees")
                        }
                    });
            });

            //Mongo
            services.Configure<MongoConfig>(Configuration.GetSection(nameof(MongoConfig)));
            services.AddSingleton<IMongoConfig>(isp => isp.GetRequiredService<IOptions<MongoConfig>>().Value);
            services.AddSingleton<UserService>();
            
            //Postgres
            services.AddEntityFrameworkNpgsql()
                .AddDbContext<CafeContext>(options => options.UseNpgsql(ConfigLoader.PostgresUrl))
                .AddUnitOfWork<CafeContext>()
                .AddCustomRepository< Brand,         GenericRepository<Brand>>()
                .AddCustomRepository< Cafe,          GenericRepository<Cafe>>()
                .AddCustomRepository< CafeEquipment, GenericRepository<CafeEquipment>>()
                .AddCustomRepository< City,          GenericRepository<City>>()
                .AddCustomRepository< EqType,        GenericRepository<EqType>>()
                .AddCustomRepository< Equipment,     GenericRepository<Equipment>>()
                .AddCustomRepository< Post,          GenericRepository<Post>>()
                .AddCustomRepository< Schedule,      GenericRepository<Schedule>>()
                .AddCustomRepository< Worker,        GenericRepository<Worker>>();
            

            //Mssql
            services.AddEntityFrameworkSqlServer()
                .AddDbContext<ProductSqlServerContext>(options => options.UseSqlServer(ConfigLoader.MssqlUrl))
                .AddUnitOfWork<ProductSqlServerContext>()
                .AddCustomRepository<Menu, GenericRepository<Menu>>()
                .AddCustomRepository<Product, GenericRepository<Product>>()
                .AddCustomRepository<Subcategory, GenericRepository<Subcategory>>()
                .AddCustomRepository<Category, GenericRepository<Category>>();


            //Maria
            services.AddEntityFrameworkMySql()
                .AddDbContext<ProductMariaContext>(options => options.UseMySql(ConfigLoader.MariaURL,
                    ServerVersion.AutoDetect(ConfigLoader.MariaURL)))
                .AddCustomRepository<Component, GenericRepository<Component>>()
                .AddCustomRepository<Ingredient, GenericRepository<Ingredient>>();
        }

      
        
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseSwagger();
                app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "Srv v1"));
            }

            app.UseHttpsRedirection();

            app.UseRouting();

            app.UseAuthorization();

            app.UseEndpoints(endpoints => { endpoints.MapControllers(); });
        }
    }
    public static class ServiceExtensions 
    {
        public static IServiceCollection AddUnitOfWork<TContext>(this IServiceCollection services)
            where TContext : DbContext
        {
            services.AddScoped<IRepositoryFactory, UnitOfWork<TContext>>();
            services.AddScoped<IUnitOfWork, UnitOfWork<TContext>>();
            services.AddScoped<IUnitOfWork<TContext>, UnitOfWork<TContext>>();
            
            return services;
        }

        public static IServiceCollection AddCustomRepository<TEntity, TRepo>(this IServiceCollection services)
        where TEntity: class
        where TRepo : class, IGenericRepository<TEntity>
        {
            services.AddScoped<IGenericRepository<TEntity>, TRepo>();

            return services;
        }
    }
}