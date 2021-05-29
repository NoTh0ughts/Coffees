﻿using System;
using System.IO;
using CoffeesServerDB.DataBase.Connections;
using CoffeesServerDB.Service;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Hosting;

namespace CoffeesServerDB
{
    class Program
    {
        public static void Main(string[] args)
        {
            var dotenv = Path.Combine(@"C:\Users\Hello\source\repos\CoffeesServerDB\CoffeesServerDB\", ".env");
            ConfigLoader.Load(dotenv);

            CreateHostBuilder(args)
                .Build()
                .Run();
	    Console.WriteLine("stopped");
        }

        private static IHostBuilder CreateHostBuilder(string[] args) =>
            Host.CreateDefaultBuilder(args)
                .ConfigureWebHostDefaults(webBuilder => webBuilder.UseStartup<Startup>());
    }
}
