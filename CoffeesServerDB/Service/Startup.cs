using System;
using CoffeesServerDB.DataBase.Entity.CoffeHouseStuff.Generated;
using CoffeesServerDB.DataBase.Entity.Products;
using CoffeesServerDB.DataBase.Entity.UserStuff;
using CoffeesServerDB.DataBase.Repositoryes;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Connections;
using Microsoft.AspNetCore.Hosting;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
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
            
            //Postgres
            services.AddEntityFrameworkNpgsql()
                .AddDbContext<coffees_cafesContext>(options => options.UseNpgsql(ConfigLoader.PostgresUrl));
            services.AddScoped<CoffeesRepository>();
            services.AddScoped<coffees_cafesContext>();
            

            //Mongo
            //services.AddEntityFrameworkMongoDb()
            //    .AddDbContext<UserContext>(options => options.UseMongoDb(ConfigLoader.MongoURL));
            //services.AddScoped<UserRepository>();
            //services.AddScoped<UserContext>();
          
            ////Mssql
            //services.AddEntityFrameworkSqlServer()
            //    .AddDbContext<ProductContext>(options => options.UseSqlServer(ConfigLoader.MssqlUrl));
            //services.AddScoped<ProductContext>();
            //services.AddScoped<ProductRepository>();
            //
            //Maria
            
            services.AddEntityFrameworkMySql()
                .AddDbContext<ProductContext>(options => options.UseMySql(ConfigLoader.MariaURL,
                    ServerVersion.AutoDetect(ConfigLoader.MariaURL)));
            
            services.AddScoped<ProductContext>();
            services.AddScoped<ProductContext>();


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