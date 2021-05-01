using Microsoft.Extensions.Configuration;
using System;
using System.IO;

namespace CoffeesServerDB
{
    class ConfigLoader
    {
        public static void Load(string filename)
        {
            if(!File.Exists(filename))
            {
                Console.WriteLine("File does not exists");
                return;
            }

            string[] lines = File.ReadAllLines(filename);
            
            foreach (var line in lines)
            {
                var parts = line.Split('=', StringSplitOptions.RemoveEmptyEntries);
                
                if (parts.Length != 2)
                    continue;

                Environment.SetEnvironmentVariable(parts[0], parts[1]);
            }

            new ConfigurationBuilder().AddEnvironmentVariables().Build();
        }

        public static string ConfigureURLMongoDBFromEnviroment()
        {
            
            var adress = Environment.GetEnvironmentVariable("MONGO_SERVER")
                             + ":"  + Environment.GetEnvironmentVariable("MONGO_PORT");

            var username = Environment.GetEnvironmentVariable("MONGO_INITDB_ROOT_USERNAME");
            var password = Environment.GetEnvironmentVariable("MONGO_INITDB_ROOT_PASSWORD");

            var result = @$"mongodb://{username}:{password}@{adress}/?authSource=admin";

            Console.WriteLine("Config URL : " + result);
            return result;
        }

        public static string ConfigureURLMariaDBFromEnviroment()
        {
            var address = Environment.GetEnvironmentVariable("MYSQL_SERVER") + ":" 
                             + Environment.GetEnvironmentVariable("MYSQL_PORT");
            var userId = "sa";
            var password = Environment.GetEnvironmentVariable("MYSQL_ROOT_PASSWORD");
            var databaseName = Environment.GetEnvironmentVariable("MYSQL_DATABASE");

            var result = $@"Server={address};User ID={userId};Password={password};Database={databaseName}";

            return result;
        }
    }
}
