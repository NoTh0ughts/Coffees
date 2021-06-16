using CoffeesServerDB.DataBase.Entity.ProductsMaria.Generated;

namespace CoffeesServerDB.DataBase.DTO
{
    public class IngredientComponentItem
    {
        public string[] Ingredients { get; set; }
        public ComponentItem[] Components { get; set; }
    }
}