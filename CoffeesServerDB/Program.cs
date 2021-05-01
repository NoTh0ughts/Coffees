using System.IO;


namespace CoffeesServerDB
{
    class Program
    {
        static void Main(string[] args)
        {
            var root = Directory.GetCurrentDirectory();
            var dotenv = Path.Combine(root, ".env");
            ConfigLoader.Load(dotenv);

            
            //MongoDb Init
            var atlas = ConfigLoader.ConfigureURLMongoDBFromEnviroment();
            MongoDbConnection.Instanse.Initialize(atlas);

            
            //MariaDb Init
            MariaDBConnection.MariaDbConnection.Execute("select * from category");
        }
    }
}
