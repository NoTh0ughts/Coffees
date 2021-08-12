using System;
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
using SCategory = CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated.Category;
using SComponent = CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated.Component;
using SIngredient = CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated.Ingredient;
using SMenu = CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated.Menu;
using SProduct = CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated.Product;
using SProductComponent = CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated.ProductComponent;
using SProductIngredient = CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated.ProductIngredient;
using SSubcategory = CoffeesServerDB.DataBase.Entity.ProductsSqlServer.Generated.Subcategory;
using MCategory = CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated.Category;
using MComponent = CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated.Component;
using MIngredient = CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated.Ingredient;
using MMenu = CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated.Menu;
using MProduct = CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated.Product;
using MProductComponent = CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated.ProductComponent;
using MProductIngredient = CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated.ProductIngredient;
using MSubcategory = CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated.Subcategory;


namespace CoffeesServerDB.Service
{
    /// <summary>
    /// Выполняет настройку компонентов к DInj
    /// </summary>
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
            
            //Добавдяет конфигурацию Swagger (Автодокументация API)
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1",
                    new OpenApiInfo
                    {
                        Title = "Coffees",
                        Version = "v1.00",
                        Description = "1. HTTP GET: для запроса на получение ресурса по этому URI.\n" +
                                      "2. HTTP DELETE: для запроса на удаление ресурса по этому URI. \n" +
                                      "3. HTTP POST: для запроса загрузки и сохранения загружаемых данных в данные. " +
                                      "Затем сервер сохраняет объект и предоставляет новый URI для этого ресурса.\n" +
                                      "4. HTTP PUT: то же самое, что и POST, но с условием, что он проверяет, " +
                                      "сохранены ли уже эти ресурсы. Если этот ресурс доступен, он просто обновляется.\n",
                        Contact = new OpenApiContact()
                        {
                            Name = "Repository",
                            Email = "",
                            Url = new Uri("https://github.com/TakeMe100Points/Coffees"),
                        }
                    });
            });
            
            
            ///Настройка конфигурации работы с БД
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
                .AddCustomRepository<SProductComponent,  GenericRepository<SProductComponent>>()
                .AddCustomRepository<SProductIngredient, GenericRepository<SProductIngredient>>()
                .AddCustomRepository<SComponent,         GenericRepository<SComponent>>()
                .AddCustomRepository<SIngredient,        GenericRepository<SIngredient>>()
                .AddCustomRepository<SMenu,              GenericRepository<SMenu>>()
                .AddCustomRepository<SProduct,           GenericRepository<SProduct>>()
                .AddCustomRepository<SSubcategory,       GenericRepository<SSubcategory>>()
                .AddCustomRepository<SCategory,          GenericRepository<SCategory>>();


            //Maria
            Console.WriteLine(ConfigLoader.MariaURL);
            services.AddEntityFrameworkMySql()
                .AddDbContext<ProductMariaContext>(options => options.UseMySql(ConfigLoader.MariaURL,
                    ServerVersion.AutoDetect(ConfigLoader.MariaURL)))
                .AddUnitOfWork<ProductMariaContext>()
                .AddCustomRepository<MProductComponent,  GenericRepository<MProductComponent>>()
                .AddCustomRepository<MProductIngredient, GenericRepository<MProductIngredient>>()
                .AddCustomRepository<MComponent,         GenericRepository<MComponent>>()
                .AddCustomRepository<MIngredient,        GenericRepository<MIngredient>>()
                .AddCustomRepository<MMenu,              GenericRepository<MMenu>>()
                .AddCustomRepository<MProduct,           GenericRepository<MProduct>>()
                .AddCustomRepository<MSubcategory,       GenericRepository<MSubcategory>>()
                .AddCustomRepository<MCategory,          GenericRepository<MCategory>>();
        }

      
        
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseSwagger();
                app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "Srv v1"));
            }
            app.UseRouting();

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllerRoute(name: "default", pattern: "{controller=Home}/{action=Index}/{id?}"); 
            });
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