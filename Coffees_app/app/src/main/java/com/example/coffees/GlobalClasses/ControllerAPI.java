package com.example.coffees.GlobalClasses;


import android.util.JsonReader;
import android.util.Log;

import com.example.coffees.BasketClasses.BasketCafe;
import com.example.coffees.BasketClasses.BasketCity;
import com.example.coffees.BasketClasses.BasketReceiver;
import com.example.coffees.FavoritesClasses.FavoritesFragmentModel;
import com.example.coffees.MenuClasses.CategoryFragmentProduct;
import com.example.coffees.MenuClasses.CategoryFragmentSubcategory;
import com.example.coffees.MenuClasses.MenuFragmentCategory;
import com.example.coffees.MenuClasses.MenuFragmentMenuPos;
import com.example.coffees.MenuClasses.NutritionInfo;
import com.example.coffees.MenuClasses.ProductFragmentModel;
import com.example.coffees.ProfileClasses.ProfileFragmentModel;
import com.example.coffees.ProfileClasses.ProfileFragmentOrderInfo;
import com.example.coffees.ProfileClasses.ProfileFragmentOrderProduct;
import com.example.coffees.ProfileClasses.UserOrdersFragmentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ControllerAPI {

    public void getUserInfo(){
        UserInfo.id = 4;
        UserInfo.username = "Username";
        UserInfo.since = "01.01.2001";
        UserInfo.cardId = 1;
        UserInfo.cardDiscount = 10;
        UserInfo.countInBasket = 37;
//        UserInfo.favorites = new ArrayList<>();
//        UserInfo.favorites.add(1);
//        UserInfo.favorites.add(62);
    }

    /*
    !!! GET !!!
     */
    public HttpURLConnection connectionGet(String putUrl) throws IOException {
        URL url = new URL(putUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestMethod("GET");
        connection.connect();

        return connection;
    }

    public LinkedHashMap<MenuFragmentMenuPos, ArrayList<MenuFragmentCategory>>
    menuFragment(HttpURLConnection connection) throws IOException {
        LinkedHashMap<MenuFragmentMenuPos, ArrayList<MenuFragmentCategory>> menu = new LinkedHashMap<>();


        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            InputStreamReader reader = new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8);

            Integer id = null;
            String name = null;
            Integer categoriesId = null;
            String categoriesName = null;
            Integer categoriesProductId = null;


            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.beginArray();
            while (jsonReader.hasNext()){
                ArrayList<MenuFragmentCategory> categories = new ArrayList<>();
                jsonReader.beginObject();
                while (jsonReader.hasNext()){
                    String key = jsonReader.nextName();
                    switch (key) {
                        case "id":
                            id = jsonReader.nextInt();
                            Log.d("id", String.valueOf(id));
                            break;
                        case "name":
                            name = jsonReader.nextString();
                            Log.d("name", name);
                            break;
                        case "categories":
                            jsonReader.beginArray();
                            while (jsonReader.hasNext()) {
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()){
                                    String keyCategories = jsonReader.nextName();
                                    switch (keyCategories){
                                        case "id":
                                            categoriesId = jsonReader.nextInt();
                                            Log.d("categoriesId", String.valueOf(categoriesId));
                                            break;
                                        case "name":
                                            categoriesName = jsonReader.nextString();
                                            Log.d("categoriesName", categoriesName);
                                            break;
                                        case "productId":
                                            categoriesProductId = jsonReader.nextInt();
                                            Log.d("categoriesProductId", String.valueOf(categoriesProductId));
                                            break;
                                        default:
                                            jsonReader.skipValue();
                                            break;
                                    }
                                }
                                categories.add(new MenuFragmentCategory(categoriesId,
                                        categoriesName, categoriesProductId));
                                jsonReader.endObject();
                            }
                            jsonReader.endArray();
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                jsonReader.endObject();
                menu.put(new MenuFragmentMenuPos(id, name),categories);
            }
            jsonReader.endArray();

            jsonReader.close();
            connection.disconnect();
        }

        return menu;
    }

    public LinkedHashMap<CategoryFragmentSubcategory, ArrayList<CategoryFragmentProduct>>
    categoryFragment(HttpURLConnection connection) throws IOException {
        LinkedHashMap<CategoryFragmentSubcategory, ArrayList<CategoryFragmentProduct>> subcategories = new LinkedHashMap<>();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStreamReader reader = new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8);

            Integer subcategoryId = null;
            String subcategoryName = null;
            Integer productId = null;
            String productName = null;


            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    ArrayList<CategoryFragmentProduct> products = new ArrayList<>();
                    String key = jsonReader.nextName();
                    switch (key) {
                        case "id":
                            subcategoryId = jsonReader.nextInt();
                            Log.d("subcategoryId", String.valueOf(subcategoryId));
                            break;
                        case "name":
                            subcategoryName = jsonReader.nextString();
                            Log.d("subcategoryName", subcategoryName);
                            break;
                        case "products":
                            jsonReader.beginArray();
                            while (jsonReader.hasNext()) {
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()) {
                                    String keyProducts = jsonReader.nextName();
                                    switch (keyProducts) {
                                        case "id":
                                            productId = jsonReader.nextInt();
                                            Log.d("productId", String.valueOf(productId));
                                            break;
                                        case "name":
                                            productName = jsonReader.nextString();
                                            Log.d("productName", productName);
                                            break;
                                        default:
                                            jsonReader.skipValue();
                                            break;
                                    }
                                }
                                products.add(new CategoryFragmentProduct(productId,
                                        productName));
                                jsonReader.endObject();
                            }
                            subcategories.put(new CategoryFragmentSubcategory(
                                    subcategoryId, subcategoryName),products);
                            jsonReader.endArray();
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                jsonReader.endObject();
            }
            jsonReader.endArray();

            jsonReader.close();
            connection.disconnect();
        }
        return subcategories;
    }

    public ProductFragmentModel productFragment(HttpURLConnection connection) throws IOException{
        ProductFragmentModel productFragmentModel = null;
        ArrayList<NutritionInfo> nutritionInfo = new ArrayList<>();
        ArrayList<String> ingredients = new ArrayList<>();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStreamReader reader = new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8);

            Integer productId = null;
            String productName = null;
            Integer calories = null;
            String ingredient = null;
            String nutritionName = null;
            Double nutritionDose = null;


            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.beginArray();
            while (jsonReader.hasNext()){
                jsonReader.beginObject();
                while (jsonReader.hasNext()){
                    String key = jsonReader.nextName();
                    switch (key){
                        case "id":
                            productId = jsonReader.nextInt();
                            break;
                        case "name":
                            productName = jsonReader.nextString();
                            break;
                        case "calories":
                            calories = jsonReader.nextInt();
                            break;
                        case "ingredients":
                            jsonReader.beginArray();
                            while (jsonReader.hasNext()){
                                ingredient = jsonReader.nextString();
                                ingredients.add(ingredient);
                            }
                            jsonReader.endArray();
                            break;
                        case "components":
                            jsonReader.beginArray();
                            while (jsonReader.hasNext()){
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()){
                                    String nutritionKey = jsonReader.nextName();
                                    switch (nutritionKey){
                                        case "name":
                                            nutritionName = jsonReader.nextString();
                                            break;
                                        case "dose":
                                            nutritionDose = jsonReader.nextDouble();
                                            break;
                                    }
                                }
                                nutritionInfo.add(new NutritionInfo(nutritionName, nutritionDose));
                                jsonReader.endObject();
                            }
                            jsonReader.endArray();
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                jsonReader.endObject();
            }
            productFragmentModel = new ProductFragmentModel(productId, productName, calories,
                    nutritionInfo,ingredients);
            jsonReader.endArray();

            jsonReader.close();
            connection.disconnect();
        }

        return productFragmentModel;
    }

    public ArrayList<FavoritesFragmentModel> favoritesFragment(HttpURLConnection connection) throws IOException{
        ArrayList<FavoritesFragmentModel> favoritesProducts = new ArrayList<>();


        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            InputStreamReader reader = new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8);

            Integer productId;
            String productName;

            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.beginObject();
            while (jsonReader.hasNext()){
                productId = null;
                productName = null;
                try{
                    productId = Integer.parseInt(jsonReader.nextName());
                    productName = jsonReader.nextString();
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }
                if (productId != null && productName != null){
                    favoritesProducts.add(new FavoritesFragmentModel(productId, productName));
                }
            }
            jsonReader.endObject();

            jsonReader.close();
            connection.disconnect();
        }



        return favoritesProducts;
    }

    public ArrayList<ProfileFragmentOrderInfo> profileOrders(HttpURLConnection connection) throws IOException {
        ArrayList<ProfileFragmentOrderInfo> profileOrders = new ArrayList<>();


        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            InputStreamReader reader = new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8);

            Integer orderId;
            ArrayList<ProfileFragmentOrderProduct> productsId;
            String orderAddress;
            String orderDate;

            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.beginArray();
            while (jsonReader.hasNext()){
                orderId = null;
                productsId = null;
                orderAddress = null;
                orderDate = null;
                jsonReader.beginObject();
                while (jsonReader.hasNext()){
                    String orderKey = jsonReader.nextName();
                    switch (orderKey){
                        case "id":
                            orderId = jsonReader.nextInt();
                            break;
                        case "products":
                            productsId = new ArrayList<>();
                            jsonReader.beginArray();
                            while (jsonReader.hasNext()){
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()){
                                    String productKey = jsonReader.nextName();
                                    if ("productId".equals(productKey)) {
                                        productsId.add(new ProfileFragmentOrderProduct(jsonReader.nextInt()));
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.endObject();
                            }
                            jsonReader.endArray();
                            break;
                        case "address":
                            orderAddress = jsonReader.nextString();
                            break;
                        case "dateOrder":
                            orderDate = jsonReader.nextString();
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                jsonReader.endObject();

                if(orderId != null && productsId != null)
                    profileOrders.add(new ProfileFragmentOrderInfo(orderId, productsId,
                            orderAddress, orderDate));

            }
            jsonReader.endArray();

            jsonReader.close();
            connection.disconnect();
        }



        return profileOrders;
    }

    public ArrayList<UserOrdersFragmentModel> userOrdersFragment(HttpURLConnection connection) throws IOException{
        ArrayList<UserOrdersFragmentModel> userOrders = new ArrayList<>();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            InputStreamReader reader = new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8);

            Integer productsId;
            String productName;
            Integer productCount;


            JsonReader jsonReader = new JsonReader(reader);
            productsId = null;
            productName = null;
            productCount = null;
            jsonReader.beginObject();
            while (jsonReader.hasNext()){
                String orderKey = jsonReader.nextName();
                if ("products".equals(orderKey)) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            String productKey = jsonReader.nextName();
                            switch (productKey) {
                                case "product_id":
                                    productsId = jsonReader.nextInt();
                                    break;
                                case "name":
                                    productName = jsonReader.nextString();
                                    break;
                                case "count":
                                    productCount = jsonReader.nextInt();
                                    break;
                                default:
                                    jsonReader.skipValue();
                                    break;
                            }
                        }
                        jsonReader.endObject();
                        userOrders.add(new UserOrdersFragmentModel(productsId, productName,
                                productCount));
                    }
                    jsonReader.endArray();
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

            jsonReader.close();
            connection.disconnect();
        }

        return userOrders;
    }

    public boolean loginActivity(HttpURLConnection connection) throws IOException{
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            InputStreamReader reader = new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8);

            Integer id = null;
            String username = null;
            String since = null;
            Integer cardId = null;
            Integer cardDiscount = null;
            Integer countInBasket = null;
            ArrayList<Integer> favorites = null;

            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.beginObject();
            while (jsonReader.hasNext()){
                String key = jsonReader.nextName();
                switch (key){
                    case "id":
                        id = jsonReader.nextInt();
                        break;
                    case "username":
                        username = jsonReader.nextString();
                        break;
                    case "registrationDate":
                        since = jsonReader.nextString();
                        break;
                    case "cardId":
                        cardId = jsonReader.nextInt();
                        break;
                    case "discount":
                        cardDiscount = jsonReader.nextInt();
                        break;
                    case "basketCount":
                        countInBasket = jsonReader.nextInt();
                        break;
                    case "favorites":
                        favorites = new ArrayList<>();
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()){
                            favorites.add(jsonReader.nextInt());
                        }
                        jsonReader.endArray();
                        break;
                    default:
                        jsonReader.skipValue();
                        break;
                }
            }
            jsonReader.endObject();

            jsonReader.close();
            connection.disconnect();

            if (id != null && username != null && since != null
                    && cardId != null
                    && cardDiscount != null
                    && countInBasket != null
                    && favorites != null){
                UserInfo.id = id;
                UserInfo.username = username;
                UserInfo.since = since;
                UserInfo.cardId = cardId;
                UserInfo.cardDiscount = cardDiscount;
                UserInfo.countInBasket = countInBasket;
                UserInfo.favorites = favorites;

                return true;
            }
        }

        return false;
    }

    public ArrayList<BasketReceiver> basketProducts(HttpURLConnection connection) throws IOException{
        ArrayList<BasketReceiver> basket = new ArrayList<>();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            InputStreamReader reader = new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8);

            Integer productId;
            Integer productCount;
            String productName;

            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.beginArray();
            while (jsonReader.hasNext()){
                jsonReader.beginObject();
                productId = null;
                productCount = null;
                productName = null;
                while (jsonReader.hasNext()){
                    String key = jsonReader.nextName();
                    switch (key){
                        case "product_id":
                            productId = jsonReader.nextInt();
                            break;
                        case "count":
                            productCount = jsonReader.nextInt();
                            break;
                        case "name":
                            productName = jsonReader.nextString();
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                if (productId != null && productName != null && productCount != null){
                    basket.add(new BasketReceiver(productId, productName, productCount));
                }
                jsonReader.endObject();
            }
            jsonReader.endArray();

            jsonReader.close();
            connection.disconnect();
        }

        return basket;
    }

    public ArrayList<BasketCity> basketCities(HttpURLConnection connection) throws IOException{
        ArrayList<BasketCity> cities = new ArrayList<>();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            InputStreamReader reader = new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8);

            Integer cityId;
            String cityName;

            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.beginArray();
            while (jsonReader.hasNext()){
                jsonReader.beginObject();
                cityId = null;
                cityName = null;
                while (jsonReader.hasNext()){
                    String key = jsonReader.nextName();
                    switch (key){
                        case "id":
                            cityId = jsonReader.nextInt();
                            break;
                        case "name":
                            cityName = jsonReader.nextString();
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                if (cityId != null && cityName != null)
                    cities.add(new BasketCity(cityId, cityName));
                jsonReader.endObject();
            }
            jsonReader.endArray();

            jsonReader.close();
            connection.disconnect();
        }

        return cities;
    }

    public ArrayList<BasketCafe> basketCafes(HttpURLConnection connection) throws IOException{
        ArrayList<BasketCafe> cafes = new ArrayList<>();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            InputStreamReader reader = new InputStreamReader(connection.getInputStream(),
                    StandardCharsets.UTF_8);

            Integer cafeId;
            String cafeName;

            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.beginArray();
            while (jsonReader.hasNext()){
                jsonReader.beginObject();
                cafeId = null;
                cafeName = null;
                while (jsonReader.hasNext()){
                    String key = jsonReader.nextName();
                    switch (key){
                        case "id":
                            cafeId = jsonReader.nextInt();
                            break;
                        case "address":
                            cafeName = jsonReader.nextString();
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                if (cafeId != null && cafeName != null)
                    cafes.add(new BasketCafe(cafeId, cafeName));
                jsonReader.endObject();
            }
            jsonReader.endArray();

            jsonReader.close();
            connection.disconnect();
        }

        return cafes;
    }

    /*
    !!! POST !!!
     */
    public HttpURLConnection connectionPost(String putUrl) throws IOException {
        URL url = new URL(putUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();

        return connection;
    }

    public HttpURLConnection addOrder(String putUrl, JSONArray orderProductsArray) throws IOException{
        URL url = new URL(putUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setConnectTimeout(2000);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(orderProductsArray.toString().getBytes(StandardCharsets.UTF_8));
        wr.flush();
        wr.close();
        connection.connect();

        return connection;
    }

    /*
    !!! PUT !!!
     */
    public HttpURLConnection connectionPut(String putUrl) throws IOException {
        URL url = new URL(putUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestMethod("PUT");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();


        return connection;
    }

    public HttpURLConnection updateBasket(String putUrl, JSONArray basketArray) throws IOException {
        URL url = new URL(putUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setConnectTimeout(2000);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(basketArray.toString().getBytes(StandardCharsets.UTF_8));
        wr.flush();
        wr.close();
        connection.connect();


        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine = null;
            if ((responseLine = br.readLine()) != null){
                try {
                    int count = Integer.parseInt(responseLine.trim());
                    if (count == -1)
                        UserInfo.countInBasket = 0;
                    else
                        UserInfo.countInBasket = count;
                } catch (NumberFormatException e){
                    UserInfo.countInBasket = 0;
                    e.printStackTrace();
                }
            }
        }


        return connection;
    }

    /*
    !!! DELETE !!!
     */
    public HttpURLConnection connectionDelete(String putUrl) throws IOException {
        URL url = new URL(putUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestProperty("X-HTTP-Method-Override", "DELETE");
        connection.setRequestMethod("DELETE");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();

        return connection;
    }
}
