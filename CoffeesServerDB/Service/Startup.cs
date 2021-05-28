using System;
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
                .AddDbContext<coffees_cafesContext>(options => options.UseNpgsql(ConfigLoader.PostgresUrl));
            services.AddScoped<CoffeesRepository>();
            services.AddScoped<coffees_cafesContext>();
            
            //Mssql
            services.AddEntityFrameworkSqlServer()
                .AddDbContext<coffeeContext>(options => options.UseSqlServer(ConfigLoader.MssqlUrl));
            services.AddScoped<coffeeContext>();
            services.AddScoped<ProductRepository>();
            
            
            
            //Maria
            services.AddEntityFrameworkMySql()
                .AddDbContext<databaseContext>(options => options.UseMySql(ConfigLoader.MariaURL,
                    ServerVersion.AutoDetect(ConfigLoader.MariaURL)));
            
            services.AddScoped<databaseContext>();
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
}