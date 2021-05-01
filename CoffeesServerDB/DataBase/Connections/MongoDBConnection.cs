using MongoDB.Driver;

namespace CoffeesServerDB
{
    class MongoDbConnection 
    {
        public static MongoDbConnection Instanse 
        { 
            get 
            { 
                return _instance ??= new MongoDbConnection(); 
            } 
        }

        public IMongoDatabase DB;

        private static MongoDbConnection _instance;
        private MongoClient _mongoClient;
        private bool _isInitialized = false;


        public void Initialize(string atlasStr)
        {
            if(_isInitialized == true)
            {
                System.Console.WriteLine("MongoDB already inintialized");
                return;
            }


            _mongoClient = new MongoClient(atlasStr);

            DB = _mongoClient.GetDatabase(Constants.MongoDbDatabaseName);
        }
    }
}
