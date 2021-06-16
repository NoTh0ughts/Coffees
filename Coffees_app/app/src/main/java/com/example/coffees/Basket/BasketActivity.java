package com.example.coffees.Basket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffees.BasketClasses.BasketCafe;
import com.example.coffees.BasketClasses.BasketCity;
import com.example.coffees.BasketClasses.BasketReceiver;
import com.example.coffees.BasketClasses.BasketSender;
import com.example.coffees.GlobalClasses.ControllerAPI;
import com.example.coffees.GlobalClasses.ProductPictures;
import com.example.coffees.GlobalClasses.Toodles;
import com.example.coffees.GlobalClasses.UserInfo;
import com.example.coffees.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BasketActivity extends AppCompatActivity {

    private int margin;
    private int margin_dp;

    private TextInputLayout basketCity_textInputLayout;
    private AutoCompleteTextView basketCity_autoCompleteTextView;
    private TextInputLayout basketCafe_textInputLayout;
    private AutoCompleteTextView basketCafe_autoCompleteTextView;

    private ArrayList<BasketCity> cities;
    private ArrayAdapter<BasketCity> citiesAdapter;
    private ArrayList<BasketCafe> cafes;
    private ArrayAdapter<BasketCafe> cafesAdapter;


    private LinearLayout basketOrderLayout;
    private LinearLayout basketProductLayout;
    private CardView basketProductCardView;
    private ImageView basketProductImage;
    private TextView basketProductName;
    private LinearLayout basketActionLayout;
    private Button btn_basketAdd;
    private TextView value_countBasketProduct;
    private Button btn_basketMinus;

    private Button btn_confirmOrder;

    private boolean cityPicked = false;
    private boolean cafePicked = false;
    private boolean basketEmpty = true;

    private ScrollView basket_scrollView;
    private ProgressBar basketActivityProgressBar;

    private ArrayList<BasketReceiver> basketProducts = new ArrayList<>();
    private boolean isUpdated = false;
    private boolean isConnected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        Toolbar custom_toolbar = findViewById(R.id.custom_toolbar_id);
        custom_toolbar.setTitle("Review order");
        custom_toolbar.setBackgroundColor(getColor(R.color.greenApp));
        custom_toolbar.setNavigationIcon(ResourcesCompat.getDrawable(
                getResources(), R.drawable.ic_arrow_left, null));
        custom_toolbar.setNavigationOnClickListener(v -> onBackPressed());


        basketCity_textInputLayout = findViewById(R.id.basketCity_textInputLayout);
        basketCity_autoCompleteTextView = findViewById(R.id.basketCity_autoCompleteTextView);
//        basketCity_autoCompleteTextView.setOnClickListener(v ->
//                basketCity_textInputLayout.setErrorEnabled(false));

        basketCafe_textInputLayout = findViewById(R.id.basketCafe_textInputLayout);
        basketCafe_autoCompleteTextView = findViewById(R.id.basketCafe_autoCompleteTextView);
//        basketCafe_autoCompleteTextView.setOnClickListener(v ->
//                basketCafe_textInputLayout.setErrorEnabled(false));

        thread_getCities();


        btn_confirmOrder = findViewById(R.id.btn_confirmOrder);
        btn_confirmOrder.setVisibility(View.GONE);
        basketOrderLayout = findViewById(R.id.basketOrderLayout);
        basketOrderLayout.setVisibility(View.GONE);

        basket_scrollView = findViewById(R.id.basket_scrollView);
        basket_scrollView.setVisibility(View.VISIBLE);
        basketActivityProgressBar = findViewById(R.id.basketActivityProgressBar);
        basketActivityProgressBar.setVisibility(View.VISIBLE);
        thread_getProducts();
    }

    private JSONArray updateBasket() throws JSONException {
        JSONArray basketArr = new JSONArray();
        for (BasketReceiver product: basketProducts){
            JSONObject productObj = new JSONObject();

            productObj.put("count", product.count);
            productObj.put("product_id", product.id);

            basketArr.put(productObj);
        }

        return basketArr;
    }

    private void thread_updateBasket() throws JSONException {
        JSONArray basketArr = updateBasket();

        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}",
                    "http://web-schedule.zapto.org:5000/Api/User/UpdateUserBasket",
                    "userId", UserInfo.id);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    //connection = controllerAPI.connectionPost(url);
                    connection = controllerAPI.updateBasket(url, basketArr);
                    connection.getResponseCode();

                } catch (IOException e){
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    if (isConnected){
                        try{
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                Log.i("Basket update", "Successfully basket update");
                                finish();
                            }
                            else {
                                alert_failedUpdateBasket("Bad request");
                            }
                        } catch (IOException e){
                            alert_failedUpdateBasket("Can't update basket content due wrong " +
                                    "response code, continue?");
                            e.printStackTrace();
                        }


                    }
                    else {
                        alert_failed("No internet connection");
                    }
                });
            }
        }).start();
    }

    private void thread_getProducts(){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            //ArrayList<BasketReceiver> basketProducts;
            String url = MessageFormat.format("{0}?{1}={2}",
                    "http://web-schedule.zapto.org:5000/Api/User/GetAllProductsFromUserBasket",
                    "userId", UserInfo.id);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionGet(url);
                    if (connection != null
                            && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        basketProducts = controllerAPI.basketProducts(connection);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    basketActivityProgressBar.setVisibility(View.GONE);
                    if (isConnected){
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                //createPageContent(basketProducts);
                                createPageContent();
                            }
                            else {
                                alert_failed("Bad request");
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                            alert_failed("Failed get data from server");
                        }
                    }
                    else {
                        alert_failed("No internet connection");
                    }
                });
            }
        }).start();
    }

    private void thread_getCities(){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}",
                    "http://web-schedule.zapto.org:5000/Api/Cafe/GetAllCities");

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionGet(url);
                    if (connection != null
                            && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        cities = controllerAPI.basketCities(connection);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    if (isConnected){
                        try{
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                citiesAdapter = new ArrayAdapter<>(getApplicationContext(),
                                        R.layout.dropdown_item, cities);
                                basketCity_autoCompleteTextView.setAdapter(citiesAdapter);
                                chooseCity();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),
                                        "Bad request",Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Failed to get cities",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "No internet connection",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void chooseCity(){
        basketCity_autoCompleteTextView.setOnItemClickListener((adapterCity, viewCity,
                                                                positionCity, idCity) -> {
            BasketCity city = (BasketCity) adapterCity.getItemAtPosition(positionCity);
            if (city != null){
                cityPicked = true;
                if (basketCity_textInputLayout.isErrorEnabled())
                    basketCity_textInputLayout.setErrorEnabled(false);
                basketCity_textInputLayout.clearFocus();
                /*
                Отправить запрос на получение всех кафе по указанному id города
                 */
                thread_getCafes(city.cityId);
            }
        });
    }

    private void thread_getCafes(Integer cityId){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}",
                    "http://web-schedule.zapto.org:5000/Api/Cafe/GetCafesInCity",
                    "cityId", cityId);

            @Override
            public void run() {
                try{
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionGet(url);
                    if (connection != null
                            && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        cafes = controllerAPI.basketCafes(connection);
                    }

                } catch (IOException e){
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    if (isConnected){
                        try{
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                cafesAdapter = new ArrayAdapter<>(getApplicationContext(),
                                        R.layout.dropdown_item, cafes);
                                basketCafe_autoCompleteTextView.setAdapter(cafesAdapter);
                                chooseCafe();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),
                                        "Bad request",Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Failed to get cafes",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "No internet connection",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void chooseCafe(){
        basketCafe_autoCompleteTextView.setOnItemClickListener((adapterCafe, viewCafe,
                                                                positionCafe, idCafe) -> {
            BasketCafe cafe = (BasketCafe) adapterCafe.getItemAtPosition(positionCafe);
            if (cafe != null){
                cafePicked = true;
                if (basketCafe_textInputLayout.isErrorEnabled())
                    basketCafe_textInputLayout.setErrorEnabled(false);
                basketCafe_textInputLayout.clearFocus();
                BasketSender.cafeId = cafe.cafeId;
            }
        });
    }

    private void setAction_confirmOrder(){
        btn_confirmOrder.setOnClickListener(v -> {
            if (cityPicked && cafePicked && !basketEmpty)
            {
                for (BasketReceiver product : basketProducts){
                    BasketSender.products.put(product.id, product.count);
                }

                /*
                Отправить запрос на добавление заказа
                 */
                alert_confirmAddOrder();
            }
            else {
                if (!cityPicked){
                    basketCity_textInputLayout.setError("The city is a requirement");
                }
                if (!cafePicked){
                    basketCafe_textInputLayout.setError("The cafe is a requirement");
                }
            }

        });
    }

    private JSONArray addOrder() throws JSONException {
        JSONArray basketSender = new JSONArray();
        LinkedHashMap<Integer, Integer> products = BasketSender.products;
        for (Map.Entry<Integer, Integer> pair : products.entrySet()){
            JSONObject productObj = new JSONObject();

            productObj.put("product_id", pair.getKey());
            productObj.put("count", pair.getValue());

            basketSender.put(productObj);
        }

        return basketSender;
    }

    private void thread_addOrder() throws JSONException {
        JSONArray orderProductsArray = addOrder();

        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}&{3}={4}",
                    "http://web-schedule.zapto.org:5000/Api/User/AddOrder",
                    "cafeId", BasketSender.cafeId,
                    "userId", UserInfo.id);


            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.addOrder(url, orderProductsArray);
                    connection.getResponseCode();
                } catch (IOException e){
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    if (isConnected){
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                Toast.makeText(getApplicationContext(),
                                        "Order added successfully",
                                        Toast.LENGTH_SHORT).show();
                                thread_clearBasket();
                            }
                            else {
                                alert_failed("An error occurred while adding an order");
                                basket_scrollView.setVisibility(View.VISIBLE);
                                basketActivityProgressBar.setVisibility(View.GONE);
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                            alert_failed("Cant get response code");
                            basket_scrollView.setVisibility(View.VISIBLE);
                            basketActivityProgressBar.setVisibility(View.GONE);
                        }
                    }
                    else {
                        alert_failed("No internet connection, please try again later");
                        basket_scrollView.setVisibility(View.VISIBLE);
                        basketActivityProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }

    private void thread_clearBasket(){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}&{3}={4}",
                    "http://web-schedule.zapto.org:5000/Api/User/ClearBasket",
                    "userId", UserInfo.id);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionDelete(url);
                    connection.getResponseCode();
                } catch (IOException e){
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    basketActivityProgressBar.setVisibility(View.GONE);
                    if (isConnected){
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                UserInfo.countInBasket = 0;
                                finish();
                            }
                            else {
                                basket_scrollView.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),
                                        "An error occurred while emptying the trash",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                            basket_scrollView.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"Failed to empty the basket",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        basket_scrollView.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),
                                "No internet connection, the basket has not been emptied",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void setAction_btnAdd(BasketReceiver product, Integer valueField){
        if (product.count < 10000){
            product.count++;
            TextView textField = findViewById(valueField);
            textField.setText(String.valueOf(product.count));
        }
        else {
            Toast.makeText(getApplicationContext(), "You can't take more products",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setAction_btnMinus(BasketReceiver product,
                                    Integer valueField, LinearLayout parentLayout,
                                    Integer layoutToDelete){
        if (product.count > 1){
            product.count--;
            TextView textField = findViewById(valueField);
            textField.setText(String.valueOf(product.count));
        }
        else {
            /*
            Запрос на удаление товара из корзины пользователя
             */
            thread_deleteProduct(product, parentLayout, layoutToDelete);
        }
    }

    private void thread_deleteProduct(BasketReceiver product,
                                      LinearLayout parentLayout, Integer layoutToDelete){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}&{3}={4}",
                    "http://web-schedule.zapto.org:5000/Api/User/DeleteProductFromUserBasket",
                    "userId", UserInfo.id,
                    "productId", product.id);


            @Override
            public void run() {
                try{
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionDelete(url);
                    connection.getResponseCode();
                } catch (IOException e){
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    if (isConnected){
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                basketProducts.remove(product);
                                parentLayout.removeView(findViewById(layoutToDelete));
                                if (basketProducts.size() == 0){
                                    emptyBasket();
                                }

                                Toast.makeText(getApplicationContext(), product.name
                                        + " was removed",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),
                                        "Bad request",Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                            alert_failed("Failed to delete product from basket");
                        }
                    }
                    else {
                        alert_failed("No internet connection");
                    }
                });
            }
        }).start();
    }

    private void createPageContent(){
        margin = 5;
        margin_dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin,
                getResources().getDisplayMetrics());


        if (basketProducts.size() == 0){
            basketEmpty = true;
            emptyBasket();
        }
        else {
            basketEmpty = false;

            basketOrderLayout.setVisibility(View.VISIBLE);
            btn_confirmOrder.setVisibility(View.VISIBLE);


            //массив id лейаутов для дальнейшего поиска конкретного id и удаления его со страницы
            ArrayList<Integer> basketProductLayoutId = new ArrayList<>();
            //массив id textview для дальнейшего поиска конкретного id и увеличения или уменьшения значения
            ArrayList<Integer> value_countBasketProductId = new ArrayList<>();
            int iter = 0;
            for (BasketReceiver product : basketProducts){
                final int finalIter = iter;


                basketProductLayout = new LinearLayout(getApplicationContext());
                basketProductLayout.setId(View.generateViewId());
                basketProductLayoutId.add(basketProductLayout.getId());
                basketProductLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams basketProductLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                basketProductLayoutParams.bottomMargin = 2 * margin_dp;
                basketProductLayout.setLayoutParams(basketProductLayoutParams);
                basketOrderLayout.addView(basketProductLayout);


                basketProductCardView = new CardView(getApplicationContext());
                int cardView_size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        90, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams basketProductCardViewParams = new LinearLayout.LayoutParams(
                        cardView_size, cardView_size
                );
                basketProductCardViewParams.rightMargin = margin_dp;
                basketProductCardView.setLayoutParams(basketProductCardViewParams);
                basketProductCardView.setRadius((float) cardView_size/2);
                basketProductLayout.addView(basketProductCardView);


                basketProductImage = new ImageView(getApplicationContext());
                basketProductImage.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));
                Integer drawId = ProductPictures.productPictures.get(product.id);
                if (drawId == null){
                    drawId = ProductPictures.defaultPicture;
                }
                Bitmap result_basketProductImage = Toodles.cropImage(drawId, 0.65f,
                        getResources());
                basketProductImage.setImageBitmap(result_basketProductImage);
                basketProductCardView.addView(basketProductImage);


                basketProductName = new TextView(getApplicationContext());
                LinearLayout.LayoutParams basketProductNameParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                );
                basketProductNameParams.rightMargin = margin_dp;
                basketProductName.setLayoutParams(basketProductNameParams);
                basketProductName.setMaxLines(3);
                basketProductName.setEllipsize(TextUtils.TruncateAt.END);
                basketProductName.setTextColor(Color.BLACK);
                basketProductName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                basketProductName.setTypeface(basketProductName.getTypeface(), Typeface.BOLD);
                basketProductName.setText(product.name);
                basketProductLayout.addView(basketProductName);


                basketActionLayout = new LinearLayout(getApplicationContext());
                basketActionLayout.setOrientation(LinearLayout.VERTICAL);
                int basketActionLayout_size = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                basketActionLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        basketActionLayout_size,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));
                basketActionLayout.setGravity(Gravity.CENTER);
                basketProductLayout.addView(basketActionLayout);


                value_countBasketProduct = new TextView(getApplicationContext());
                value_countBasketProduct.setId(View.generateViewId());
                value_countBasketProductId.add(value_countBasketProduct.getId());
                value_countBasketProduct.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                value_countBasketProduct.setGravity(Gravity.CENTER);
                value_countBasketProduct.setMaxLines(1);
                value_countBasketProduct.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                value_countBasketProduct.setTextColor(Color.BLACK);
                value_countBasketProduct.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                value_countBasketProduct.setTypeface(value_countBasketProduct.getTypeface(),
                        Typeface.BOLD);
                value_countBasketProduct.setText(String.valueOf(product.count));


                btn_basketAdd = new Button(getApplicationContext());
                int actionButton_size = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                btn_basketAdd.setLayoutParams(new LinearLayout.LayoutParams(
                        actionButton_size, actionButton_size
                ));
                btn_basketAdd.setPadding(0, 0, 0, 0);
                btn_basketAdd.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.round_button, null));
                btn_basketAdd.setTextColor(Color.WHITE);
                btn_basketAdd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                btn_basketAdd.setTypeface(btn_basketAdd.getTypeface(), Typeface.BOLD);
                btn_basketAdd.setText("+");
                btn_basketAdd.setOnClickListener(v -> setAction_btnAdd(product,
                        value_countBasketProductId.get(finalIter)));


                btn_basketMinus = new Button(getApplicationContext());
                btn_basketMinus.setLayoutParams(new LinearLayout.LayoutParams(
                        actionButton_size, actionButton_size
                ));
                btn_basketMinus.setPadding(0, 0, 0, 0);
                btn_basketMinus.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.round_button, null));
                btn_basketMinus.setTextColor(Color.WHITE);
                btn_basketMinus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                btn_basketMinus.setTypeface(btn_basketMinus.getTypeface(), Typeface.BOLD);
                btn_basketMinus.setText("-");
                btn_basketMinus.setOnClickListener(v -> setAction_btnMinus(
                        product,
                        value_countBasketProductId.get(finalIter),
                        basketOrderLayout,
                        basketProductLayoutId.get(finalIter)));


                basketActionLayout.addView(btn_basketAdd);
                basketActionLayout.addView(value_countBasketProduct);
                basketActionLayout.addView(btn_basketMinus);


                iter++;
            }

            //Установить порядок действий для кнопки подтверждения
            setAction_confirmOrder();
        }

    }

    private void emptyBasket(){
        basketOrderLayout.setVisibility(View.GONE);
        btn_confirmOrder.setVisibility(View.GONE);
        LinearLayout basket_scrollLayout = findViewById(R.id.basket_scrollLayout);


        TextView text_emptyBasket = new TextView(getApplicationContext());
        text_emptyBasket.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        text_emptyBasket.setPadding(2 * margin_dp, 2 * margin_dp,
                2 * margin_dp, 2 * margin_dp);
        text_emptyBasket.setGravity(Gravity.CENTER);
        text_emptyBasket.setTextColor(Color.BLACK);
        text_emptyBasket.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        text_emptyBasket.setText("Basket is empty");

        basket_scrollLayout.addView(text_emptyBasket);
    }

    //Очистить фокус с поля ввода по нажатии на пустое место экрана
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) { //Нажатие пальцем по экрану
            View v = getCurrentFocus(); //Получить текущий фокус
            if ( v instanceof AutoCompleteTextView) {   //Установлен ли фокус в поле EditText
                Rect outRect = new Rect();  //Создание экзампляра класса прямоугольника
                v.getGlobalVisibleRect(outRect);    //Получить координаты углов прямоугольника поля EditText
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {    //Если коориднаты места нажатия не находятся в координатах прямоугольника EditText, то
                    v.clearFocus(); //Очисть фокус с поля EditText
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private void alert_failed(String message){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", ((dialog1, which) -> {}))
                .show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ResourcesCompat.getColor(
                getResources(), R.color.greenApp, null));
        dialog.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(
                0xFF000000, Color.WHITE));

        TextView text_dialog = dialog.findViewById(android.R.id.message);
        assert text_dialog != null;
        text_dialog.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        text_dialog.setTextColor(Color.BLACK);
    }

    @Override
    public void onBackPressed() {
        /*
        Запрос обновления кол-во товаров в корзине пользователя при выходе из активности
         */
        try {
            thread_updateBasket();
        } catch (JSONException e) {
            alert_failedUpdateBasket("Can't update basket content, continue?");
            e.printStackTrace();
        }
    }

    private void alert_failedUpdateBasket(String message){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", ((dialog1, which) -> finish()))
                .setNegativeButton("Cancel", (((dialog1, which) -> {})))
                .show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ResourcesCompat.getColor(
                getResources(), R.color.greenApp, null));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ResourcesCompat.getColor(
                getResources(), R.color.greenApp, null));
        dialog.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(
                0xFF000000, Color.WHITE));

        TextView text_dialog = dialog.findViewById(android.R.id.message);
        assert text_dialog != null;
        text_dialog.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        text_dialog.setTextColor(Color.BLACK);
    }

    private void alert_confirmAddOrder(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to make an order?")
                .setPositiveButton("Yes", ((dialog1, which) -> {
                    try {
                        basketActivityProgressBar.setVisibility(View.VISIBLE);
                        basket_scrollView.setVisibility(View.GONE);
                        thread_addOrder();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }))
                .setNegativeButton("Cancel", (((dialog1, which) -> {})))
                .show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ResourcesCompat.getColor(
                getResources(), R.color.greenApp, null));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ResourcesCompat.getColor(
                getResources(), R.color.greenApp, null));
        dialog.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(
                0xFF000000, Color.WHITE));

        TextView text_dialog = dialog.findViewById(android.R.id.message);
        assert text_dialog != null;
        text_dialog.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        text_dialog.setTextColor(Color.BLACK);
    }
}