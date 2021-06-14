package com.example.coffees.Menu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffees.Authorization.LoginActivity;
import com.example.coffees.Basket.BasketActivity;
import com.example.coffees.GlobalClasses.ControllerAPI;
import com.example.coffees.GlobalClasses.ProductPictures;
import com.example.coffees.GlobalClasses.Toodles;
import com.example.coffees.GlobalClasses.UserInfo;
import com.example.coffees.MenuClasses.NutritionInfo;
import com.example.coffees.MenuClasses.ProductFragmentModel;
import com.example.coffees.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.MessageFormat;

public class ProductActivity extends AppCompatActivity {

    private int margin;
    private int margin_dp;

    private LinearLayout product_scrollLayout;
    private LinearLayout productBrieflyLayout;
    private CardView productImageCardView;
    private ImageView productImageBriefly;
    private TextView productNameBriefly;
    private TextView productCaloriesBriefly;
    private LinearLayout productInfoLayout;
    private LinearLayout nutritionLayout;
    private TextView title_nutrition;
    private LinearLayout content_nutritionLayout;
    private LinearLayout nutritionPositionLayout;
    private TextView title_nutritionPosition;
    private TextView value_nutritionPosition;
    private LinearLayout ingredientsLayout;
    private TextView title_ingredients;
    private TextView content_ingredients;

    private ConstraintLayout productBasketLayout;
    private TextView value_productBasket;

    private int receiveIntent_id;
    boolean isFavorite = false;

    private ScrollView product_scrollView;
    private ProgressBar productFragmentProgressBar;

    boolean isConnected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product);


        Intent receiveIntent = getIntent();
        receiveIntent_id = receiveIntent.getIntExtra("id", 0);
        if (receiveIntent_id != 0 ){
            Toolbar custom_toolbar = findViewById(R.id.custom_toolbar_id);
            setSupportActionBar(custom_toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            custom_toolbar.getBackground().setAlpha(0);
            custom_toolbar.setNavigationIcon(ResourcesCompat.getDrawable(
                    getResources(), R.drawable.ic_arrow_left, null));
            custom_toolbar.setNavigationOnClickListener(v -> onBackPressed());

            product_scrollView = findViewById(R.id.product_scrollView);
            product_scrollView.setVisibility(View.GONE);
            productFragmentProgressBar = findViewById(R.id.productFragmentProgressBar);
            productFragmentProgressBar.setVisibility(View.VISIBLE);
            productBasketLayout = findViewById(R.id.productBasketLayout);
            productBasketLayout.setVisibility(View.GONE);
            value_productBasket = findViewById(R.id.value_productBasket);

            thread_ProductActivity();
        }
        else{
            Log.d("Get intent", "Failed get intent from CategoryFragment");
            alert_failed("Failed get data from previous page");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("USERINFO_id", String.valueOf(UserInfo.id));
        Log.d("USERINFO_favs", String.valueOf(UserInfo.favorites.size()));
        invalidateOptionsMenu();
        if (UserInfo.countInBasket != null){
            if (UserInfo.countInBasket == 0){
                productBasketLayout.setVisibility(View.GONE);
            }
            value_productBasket.setText(String.valueOf(UserInfo.countInBasket));
        }
        else
            value_productBasket.setText("!!!");

    }

    public void thread_ProductActivity(){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ProductFragmentModel product = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}",
                    "http://web-schedule.zapto.org:5000/Api/Product/GetProductsDTO",
                    "id", receiveIntent_id);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionGet(url);
                    if (connection != null && connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                        product = controllerAPI.productFragment(connection);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    productFragmentProgressBar.setVisibility(View.GONE);
                    if (isConnected) {
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                product_scrollView.setVisibility(View.VISIBLE);
                                createPageContent(product);
                                setAction_addToOrder();
                            } else {
                                alert_failed("Failed get data from server");
                                product_scrollView.setVisibility(View.GONE);
                                productFragmentProgressBar.setVisibility(View.VISIBLE);
                            }
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), "Cant get response code",
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    else {
                        alert_failed("Failed to connect");
                    }
                });
            }
        }).start();
    }

    private void setAction_addToOrder(){
        productBasketLayout.setOnClickListener(v -> setTransition_BasketActivity());
        productBasketLayout.setVisibility(View.GONE);

        Button btn_addToOrder = findViewById(R.id.btn_addToOrder);
        btn_addToOrder.setOnClickListener(v -> {
            if(UserInfo.id != null){
                thread_addProductToBasket();
            }
            else{
                alert_needToLogin("To add a product to the order, you need to log in");
            }
        });
    }

    private void thread_addProductToBasket(){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}&{3}={4}&{5}={6}",
                    "http://web-schedule.zapto.org:5000/Api/User/AddProductToBasket",
                    "userId", UserInfo.id,
                    "productId", receiveIntent_id,
                    "count", 1);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionPost(url);
                    connection.getResponseCode();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    if (isConnected){
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                productBasketLayout.setVisibility(View.VISIBLE);
                                UserInfo.countInBasket++;
                                value_productBasket.setText(String.valueOf(UserInfo.countInBasket));

                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Product added successfully", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP , 0, 0);
                                toast.show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),
                                        "Failed to add item to basket",Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), "Cant get response code",
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No internet connection",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void createPageContent(ProductFragmentModel product){
        margin = 5;
        margin_dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin,
                getResources().getDisplayMetrics());


        product_scrollLayout = findViewById(R.id.product_scrollLayout);

        //Верхняя половина страницы
        //Необходимо первостепенно создать картинку, чтобы достать цвет фона для использования далее
        productImageBriefly = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams productImageBrieflyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        productImageBriefly.setLayoutParams(productImageBrieflyParams);
        Integer drawId = ProductPictures.productPictures.get(receiveIntent_id);
        if (drawId == null){
            drawId = ProductPictures.defaultPicture;
        }
        Bitmap result_productImageBriefly = Toodles.cropImage(drawId,
                0.65f, getResources());
        productImageBriefly.setImageBitmap(result_productImageBriefly);


        //Узнаём цвет фона
        int brieflyBackgroundColor;
        brieflyBackgroundColor = setBrieflyBackgroundColor(drawId);


        productBrieflyLayout = new LinearLayout(getApplicationContext());
        productBrieflyLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams productBrieflyLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        productBrieflyLayoutParams.bottomMargin = 2 * margin_dp;
        productBrieflyLayout.setLayoutParams(productBrieflyLayoutParams);
        productBrieflyLayout.setPadding(0, 2 * margin_dp, 0, 0);
        productBrieflyLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        productBrieflyLayout.setBackgroundColor(brieflyBackgroundColor);


        productImageCardView = new CardView(getApplicationContext());
        int side_length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                180, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams productImageCardViewParams = new LinearLayout.LayoutParams(
                side_length, side_length
        );
        productImageCardViewParams.bottomMargin = margin_dp;
        productImageCardView.setLayoutParams(productImageCardViewParams);
        float qwe = (float) side_length/2;
        productImageCardView.setRadius(qwe);
        productImageCardView.setCardBackgroundColor(brieflyBackgroundColor);
        productImageCardView.setElevation(0);


        productNameBriefly = new TextView(getApplicationContext());
        LinearLayout.LayoutParams productNameBrieflyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        productNameBrieflyParams.bottomMargin = margin_dp;
        productNameBriefly.setLayoutParams(productNameBrieflyParams);
        productNameBriefly.setGravity(Gravity.CENTER);
        productNameBriefly.setTextColor(Color.WHITE);
        productNameBriefly.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        productNameBriefly.setText(product.name);


        productCaloriesBriefly = new TextView(getApplicationContext());
        LinearLayout.LayoutParams productCaloriesBrieflyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        productCaloriesBrieflyParams.bottomMargin = 2 * margin_dp;
        productCaloriesBriefly.setLayoutParams(productCaloriesBrieflyParams);
        productCaloriesBriefly.setGravity(Gravity.CENTER);
        productCaloriesBriefly.setTextColor(Color.WHITE);
        productCaloriesBriefly.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        productCaloriesBriefly.setTypeface(productCaloriesBriefly.getTypeface(), Typeface.BOLD);
        productCaloriesBriefly.setText(MessageFormat.format("{0} {1}",
                String.valueOf(product.calories), "cal."));


        productImageCardView.addView(productImageBriefly);
        productBrieflyLayout.addView(productImageCardView);
        productBrieflyLayout.addView(productNameBriefly);
        productBrieflyLayout.addView(productCaloriesBriefly);


        //Нижняя половина страницы
        productInfoLayout = new LinearLayout(getApplicationContext());
        productInfoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams productInfoLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        productInfoLayoutParams.setMargins(2 * margin_dp, 0,
                2 * margin_dp, 2 * margin_dp);
        productInfoLayout.setLayoutParams(productInfoLayoutParams);


        nutritionLayout = new LinearLayout(getApplicationContext());
        nutritionLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams nutritionLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        nutritionLayout.setLayoutParams(nutritionLayoutParams);


        title_nutrition = new TextView(getApplicationContext());
        LinearLayout.LayoutParams title_nutritionParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        title_nutrition.setLayoutParams(title_nutritionParams);
        title_nutrition.setTextColor(Color.BLACK);
        title_nutrition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title_nutrition.setTypeface(title_nutrition.getTypeface(), Typeface.BOLD);
        title_nutrition.setText("Nutrition Information");


        content_nutritionLayout = new LinearLayout(getApplicationContext());
        content_nutritionLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams content_nutritionLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        content_nutritionLayoutParams.setMargins(2 * margin_dp, 2 * margin_dp,
                2 * margin_dp, 2 * margin_dp);
        content_nutritionLayout.setLayoutParams(content_nutritionLayoutParams);



        for (NutritionInfo currNutrition : product.nutritionInfo){
            nutritionPositionLayout = new LinearLayout(getApplicationContext());
            nutritionPositionLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams nutritionPositionLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            nutritionPositionLayoutParams.bottomMargin = margin_dp;
            nutritionPositionLayout.setLayoutParams(nutritionPositionLayoutParams);
            nutritionPositionLayout.setGravity(Gravity.CENTER_VERTICAL);


            title_nutritionPosition = new TextView(getApplicationContext());
            LinearLayout.LayoutParams title_nutritionPositionParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0
            );
            title_nutritionPositionParams.rightMargin = 2 * margin_dp;
            title_nutritionPosition.setLayoutParams(title_nutritionPositionParams);
            title_nutritionPosition.setTextColor(Color.BLACK);
            title_nutritionPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            title_nutritionPosition.setTypeface(title_nutritionPosition.getTypeface(), Typeface.BOLD);
            title_nutritionPosition.setText(currNutrition.name);


            value_nutritionPosition = new TextView(getApplicationContext());
            LinearLayout.LayoutParams value_nutritionPositionParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0
            );
            value_nutritionPosition.setLayoutParams(value_nutritionPositionParams);
            value_nutritionPosition.setTextColor(Color.DKGRAY);
            value_nutritionPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            value_nutritionPosition.setText(MessageFormat.format("{0} {1}", String.valueOf(currNutrition.value), "g"));


            nutritionPositionLayout.addView(title_nutritionPosition);
            nutritionPositionLayout.addView(value_nutritionPosition);
            content_nutritionLayout.addView(nutritionPositionLayout);
        }


        ingredientsLayout = new LinearLayout(getApplicationContext());
        ingredientsLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams ingredientsLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ingredientsLayoutParams.bottomMargin = 50 * margin_dp;
        ingredientsLayout.setLayoutParams(ingredientsLayoutParams);


        title_ingredients = new TextView(getApplicationContext());
        LinearLayout.LayoutParams title_ingredientsParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        title_ingredients.setLayoutParams(title_ingredientsParams);
        title_ingredients.setTextColor(Color.BLACK);
        title_ingredients.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title_ingredients.setTypeface(title_ingredients.getTypeface(), Typeface.BOLD);
        title_ingredients.setText("Ingredients");


        content_ingredients = new TextView(getApplicationContext());
        LinearLayout.LayoutParams content_ingredientsParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        content_ingredientsParams.setMargins(2 * margin_dp, 2 * margin_dp,
                2 * margin_dp, 2 * margin_dp);
        content_ingredients.setLayoutParams(content_ingredientsParams);
        content_ingredients.setTextColor(Color.DKGRAY);
        content_ingredients.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        String str_ingredients = "";
        for (int i = 0; i < product.ingredients.size(); i++){
            if (i == product.ingredients.size() - 1)
                str_ingredients += product.ingredients.get(i);
            else
                str_ingredients += MessageFormat.format("{0}, ", product.ingredients.get(i));
        }
        content_ingredients.setText(str_ingredients);


        nutritionLayout.addView(title_nutrition);
        nutritionLayout.addView(content_nutritionLayout);
        ingredientsLayout.addView(title_ingredients);
        ingredientsLayout.addView(content_ingredients);

        productInfoLayout.addView(nutritionLayout);
        productInfoLayout.addView(ingredientsLayout);

        product_scrollLayout.addView(productBrieflyLayout);
        product_scrollLayout.addView(productInfoLayout);


        Button btn_addToOrder = findViewById(R.id.btn_addToOrder);
        btn_addToOrder.setOnClickListener(v -> {});
    }

    private Integer setBrieflyBackgroundColor(Integer image){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(),
                image, null
        );
        Bitmap bitmap = bitmapDrawable.getBitmap();
        return bitmap.getPixel(1, bitmap.getHeight()/2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(false);
        MenuItem favorite = menu.findItem(R.id.action_favorite);
        favorite.setVisible(true);


        if (UserInfo.favorites != null){
            if (UserInfo.favorites.contains(receiveIntent_id)){
                favorite.setIcon(R.drawable.ic_favorite_yes);
                isFavorite = true; //Если пользователь авторизирован и данный товар в избранном
            }
        }

        favorite.setOnMenuItemClickListener(item -> {
            if (UserInfo.id != null){   //Пользователь авторизирован
                if (isFavorite){ //Продукт в избранном
                    thread_deleteProductFromUserFavorites(favorite);
                }
                else{    //Продукт не в избранном
                    thread_addProductToUserFavorites(favorite);
                }
                //isFavorite = !isFavorite;
            }
            else {  //Пользователь не авторизирован
                alert_needToLogin("To add a product to your favorites, you need to log in");
            }

           return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void thread_deleteProductFromUserFavorites(MenuItem favorite){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}&{3}={4}",
                    "http://web-schedule.zapto.org:5000/Api/User/DeleteProductFromUserFavorites",
                    "userId", UserInfo.id,
                    "productId", receiveIntent_id);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionDelete(url);
                    connection.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    if (isConnected){
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                UserInfo.favorites.remove(Integer.valueOf(receiveIntent_id));
                                favorite.setIcon(R.drawable.ic_favorite_no);
                                isFavorite = !isFavorite;
                            }
                            else if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST){
                                Toast.makeText(getApplicationContext(),
                                        "Product already not in favorite",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),
                                        "Failed to delete product on server",Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e){
                            Toast.makeText(getApplicationContext(), "Cant get response code",
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No internet connection",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void thread_addProductToUserFavorites(MenuItem favorite){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}&{3}={4}",
                    "http://web-schedule.zapto.org:5000/Api/User/AddProductToUserFavorites",
                    "userId", UserInfo.id,
                    "productId", receiveIntent_id);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionPut(url);
                    connection.getResponseCode();
                } catch (IOException e){
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    if (isConnected){
                        try {
                            if(connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                UserInfo.favorites.add(receiveIntent_id);
                                favorite.setIcon(R.drawable.ic_favorite_yes);
                                isFavorite = !isFavorite;
                            }
                            else if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST){
                                Toast.makeText(getApplicationContext(),
                                        "Product already in favorite",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),
                                        "Unknown response code",Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Failed to add product to server",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No internet connection",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void alert_needToLogin(String message){
        AlertDialog dialog = new AlertDialog.Builder(ProductActivity.this)
                .setMessage(message)
                .setPositiveButton("Log In", ((dialog1, which) -> {
                    setTransition_Login();
                }))
                .setNegativeButton("Cancel", ((dialog1, which) -> {}))
                .show();

        TextView text_dialog = dialog.findViewById(android.R.id.message);
        assert text_dialog != null;
        text_dialog.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        text_dialog.setTextColor(Color.BLACK);
    }

    private void setTransition_Login(){
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void setTransition_BasketActivity(){
        //Toast.makeText(getApplicationContext(),"setTransition_Basket", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, BasketActivity.class));
    }

    private void alert_failed(String message){
        AlertDialog dialog = new AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("Retry", ((dialog1, which) -> {
                    product_scrollView.setVisibility(View.GONE);
                    productFragmentProgressBar.setVisibility(View.VISIBLE);
                    thread_ProductActivity();
                }))
                .setNegativeButton("Cancel", ((dialog1, which) -> {}))
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