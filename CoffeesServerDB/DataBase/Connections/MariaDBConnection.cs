using System;
using MySqlConnector;

namespace CoffeesServerDB
{
    public class MariaDBConnection
    {
        public static MariaDBConnection MariaDbConnection
        {
            get { return _instance ??= new MariaDBConnection(); }
        }

        private static MariaDBConnection _instance;

        public void Execute(string request)
        {
            var config = ConfigLoader.ConfigureURLMariaDBFromEnviroment();
            
            using var connection = new MySqlConnection(config);
            using var command = new MySqlCommand(request, connection);
            using var reader = command.ExecuteReader();
            while (reader.Read()) 
                Console.WriteLine(reader.GetString(0));
        }
    }
}