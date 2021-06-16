using System;
using System.IO;
using Microsoft.Extensions.Configuration;

namespace CoffeesServerDB.Service
{
    public static class ConfigLoader
    {
        public static string MongoURL => _mongoURL       ??= ConfigureURLMongoDBFromEnviroment();
        public static string MariaURL => _mariaURL       ??= ConfigureURLMariaDBFromEnviroment();
        public static string PostgresUrl => _postgresURL ??= ConfigureURLPostgresFromEnviroment();
        public static string MssqlUrl => _mssqlURL       ??= ConfigureURLMSSQLFromEnviroment();

        private static string _mongoURL;
        private static string _mariaURL;
        private static string _postgresURL;
        private static string _mssqlURL;        
        
        
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

                if (parts.Length == 2) Environment.SetEnvironmentVariable(parts[0], parts[1]);
            }

            new ConfigurationBuilder().AddEnvironmentVariables().Build();
        }

        private static string ConfigureURLMongoDBFromEnviroment()
        {
            var address = Environment.GetEnvironmentVariable("MONGO_SERVER")
                          + ":"  + Environment.GetEnvironmentVariable("MONGO_PORT");

            var username = Environment.GetEnvironmentVariable("MONGO_INITDB_ROOT_USERNAME");
            var password = Environment.GetEnvironmentVariable("MONGO_INITDB_ROOT_PASSWORD");
            var result = @$"mongodb://{username}:{password}@{address}/?authSource=admin";

            return _mongoURL = result;
        }

        private static string ConfigureURLMariaDBFromEnviroment()
        {
            var address = Environment.GetEnvironmentVariable("MYSQL_SERVER");
            var port = Environment.GetEnvironmentVariable("MYSQL_PORT");
            var userId = "root";
            var password = Environment.GetEnvironmentVariable("MYSQL_ROOT_PASSWORD");
            var databaseName = Environment.GetEnvironmentVariable("MYSQL_DATABASE");

            var result = $@"Server={address};Port={port};Database={databaseName};Uid={userId};Pwd={password};";

            return _mariaURL = result;
        }

        private static string ConfigureURLPostgresFromEnviroment()
        {
            var server = Environment.GetEnvironmentVariable("POSTGRES_SERVER");
            var port = Environment.GetEnvironmentVariable("POSTGRES_PORT");
            var user = Environment.GetEnvironmentVariable("POSTGRES_USER");
            var password = Environment.GetEnvironmentVariable("POSTGRES_PASSWORD");
            var dbName = Environment.GetEnvironmentVariable("POSTGRES_DB");
            
            var result = $@"Server={server}; Port={port}; Database={dbName}; User Id={user}; Password={password}";
            return _postgresURL = result;
        }

        private static string ConfigureURLMSSQLFromEnviroment()
        {
            var server = Environment.GetEnvironmentVariable("MSSQL_SERVER");
            var port = Environment.GetEnvironmentVariable("MSSQL_PORT");
            var password = Environment.GetEnvironmentVariable("SA_PASSWORD");
            var dbName = Environment.GetEnvironmentVariable("MSSQL_DB");
            
            var result = $@"Server={server};Database={dbName};User Id=sa;Password={password};";

            Console.WriteLine(result);
            return _mssqlURL = result;
        }
    }
}
