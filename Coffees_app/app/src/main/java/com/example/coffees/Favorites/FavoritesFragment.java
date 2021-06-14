package com.example.coffees.Favorites;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffees.Basket.BasketActivity;
import com.example.coffees.FavoritesClasses.FavoritesFragmentModel;
import com.example.coffees.GlobalClasses.ControllerAPI;
import com.example.coffees.GlobalClasses.Toodles;
import com.example.coffees.GlobalClasses.UserInfo;
import com.example.coffees.MainActivity;
import com.example.coffees.GlobalClasses.ProductPictures;
import com.example.coffees.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    View fragmentView;

    private int margin;
    private int margin_dp;

    private LinearLayout favorites_scrollLayout;
    private TextView title_favorites;
    private LinearLayout favoritesLayout;
    private LinearLayout favoriteLayout;
    private ConstraintLayout favoriteImagesLayout;
    private CardView favoriteProductCardView;
    private ImageView favoriteProductImage;
    private ImageView favoriteAddImage;
    private TextView value_favorites;

    private ScrollView favorites_scrollView;
    private ConstraintLayout favoriteBasketLayout;
    private TextView value_favoriteBasket;
    private Integer notAuthorizedPageId = null;
    private ProgressBar favoritesFragmentProgressBar;

    boolean isConnected = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_favorites, container, false);


        MainActivity activity = (MainActivity)getActivity();
        assert activity != null;
        activity.setToolbar("Coffees",null);


        favorites_scrollView = fragmentView.findViewById(R.id.favorites_scrollView);
        favorites_scrollView.setVisibility(View.GONE);
        favoriteBasketLayout = fragmentView.findViewById(R.id.favoriteBasketLayout);
        favoriteBasketLayout.setVisibility(View.GONE);
        favoritesFragmentProgressBar = fragmentView.findViewById(R.id.favoritesFragmentProgressBar);
        favoritesFragmentProgressBar.setVisibility(View.VISIBLE);

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (favorites_scrollLayout == null){ //Если страница ещё не была ни разу инициализирована
            favoritesFragmentProgressBar.setVisibility(View.VISIBLE);
            isLoggedIn();
        }

        value_favoriteBasket = fragmentView.findViewById(R.id.value_favoriteBasket);
        if (UserInfo.countInBasket != null){
            value_favoriteBasket.setText(String.valueOf(UserInfo.countInBasket));
        }
        else
            value_favoriteBasket.setText("!!!");
    }


    private void isLoggedIn(){
        if (UserInfo.id != null){   //Пользователь авторизирован
            thread_FavoritesFragment();

            //На страницу зашли повторно, после того как неавторизованный пользователь
            //авторизировался
            ConstraintLayout favoritesFragmentLayout = fragmentView.findViewById(
                    R.id.favoritesFragmentLayout);
            if (notAuthorizedPageId != null)
                favoritesFragmentLayout.removeView(fragmentView.findViewById(notAuthorizedPageId));
        }
        else {
            favorites_scrollView.setVisibility(View.GONE);
            favoriteBasketLayout.setVisibility(View.GONE);
            favoritesFragmentProgressBar.setVisibility(View.GONE);
            notAuthorizedPageId = Toodles.notAuthorizedPageContent(getActivity(), fragmentView,
                    R.id.favoritesFragmentLayout, getContext(), getResources());
        }
    }

    private void thread_FavoritesFragment(){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ArrayList<FavoritesFragmentModel> favorites = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}",
                    "http://web-schedule.zapto.org:5000/Api/User/GetFavoritesForUserById",
                    "userId", UserInfo.id);
            @Override
            public void run() {
                try{
                    isConnected = Toodles.isNetworkAvailable(requireActivity().getApplication());
                    connection = controllerAPI.connectionGet(url);
                    if (connection != null && connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                        favorites = controllerAPI.favoritesFragment(connection);
                } catch (IOException e){
                    e.printStackTrace();
                }

                requireActivity().runOnUiThread(() -> {
                    favoritesFragmentProgressBar.setVisibility(View.GONE);
                    if (isConnected){
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                //favorites_scrollView.setVisibility(View.VISIBLE);
                                createPageContent(favorites);
                                favorites_scrollView.setVisibility(View.VISIBLE);
                                favoriteBasketLayout.setVisibility(View.VISIBLE);
                            }
                            else {
                                alert_failed("Failed to get data from server");
                                favorites_scrollView.setVisibility(View.GONE);
                                favoritesFragmentProgressBar.setVisibility(View.VISIBLE);
                            }
                        } catch (IOException e){
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

    private void createPageContent(ArrayList<FavoritesFragmentModel> favoritesProducts){
        margin = 5;
        margin_dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin,
                getResources().getDisplayMetrics());


        //Назначим действие для корзины
        favoriteBasketLayout.setOnClickListener(v -> setTransition_BasketActivity());


//        ControllerAPI controllerAPI = new ControllerAPI();
//        ArrayList<FavoritesFragmentModel> favoritesProducts = controllerAPI.favoritesFragment();


        favorites_scrollLayout = fragmentView.findViewById(R.id.favorites_scrollLayout);
        title_favorites = fragmentView.findViewById(R.id.title_favorites);
        favoritesLayout = fragmentView.findViewById(R.id.favoritesLayout);

        if (favoritesProducts.size() == 0){
            TextView test_textView = new TextView(requireContext());
            test_textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            test_textView.setGravity(Gravity.CENTER_HORIZONTAL);
            test_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            test_textView.setText("Your favorites list is empty");

            favorites_scrollLayout.addView(test_textView);
            return;
        }

        for (FavoritesFragmentModel favoriteProduct : favoritesProducts){
            favoriteLayout = new LinearLayout(requireContext());
            favoriteLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams favoriteLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            favoriteLayoutParams.bottomMargin = 2 * margin_dp;
            favoriteLayout.setLayoutParams(favoriteLayoutParams);
            favoriteLayout.setOnClickListener(v -> thread_addProductBasket(favoriteProduct.id));


            favoriteImagesLayout = new ConstraintLayout(requireContext());
            int side_length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    100, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams favoriteImagesLayoutParams = new LinearLayout.LayoutParams(
                    side_length, side_length
            );
            favoriteImagesLayoutParams.rightMargin = 2 * margin_dp;
            favoriteImagesLayout.setLayoutParams(favoriteImagesLayoutParams);
            favoriteImagesLayout.setId(View.generateViewId());


            favoriteProductCardView = new CardView(requireContext());
            favoriteProductCardView.setId(View.generateViewId());
            LinearLayout.LayoutParams favoriteProductCardViewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            favoriteProductCardView.setLayoutParams(favoriteProductCardViewParams);
            ConstraintSet favoriteProductCardViewConstraint = new ConstraintSet();
            favoriteProductCardViewConstraint.clone(favoriteImagesLayout);
            Toodles.setConstraints(
                    favoriteProductCardViewConstraint,
                    favoriteProductCardView.getId(),
                    favoriteImagesLayout.getId());
            favoriteProductCardViewConstraint.applyTo(favoriteImagesLayout);
            favoriteProductCardView.setRadius((float) side_length/2);
            favoriteProductCardView.setElevation(0);


            favoriteProductImage = new ImageView(requireContext());
            LinearLayout.LayoutParams favoriteProductImageParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            favoriteProductImage.setLayoutParams(favoriteProductImageParams);
            Integer drawid = ProductPictures.productPictures.get(favoriteProduct.id);
            if (drawid == null){
                drawid = ProductPictures.defaultPicture;
            }
            Bitmap result_favoriteProductImage = Toodles.cropImage(drawid,0.65f,
                    getResources());
            favoriteProductImage.setImageBitmap(result_favoriteProductImage);
            favoriteProductCardView.addView(favoriteProductImage);
            favoriteImagesLayout.addView(favoriteProductCardView);


            favoriteAddImage = new ImageView(requireContext());
            favoriteAddImage.setId(View.generateViewId());
            int addImage_length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    30, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams favoriteAddImageParams = new LinearLayout.LayoutParams(
                    addImage_length, addImage_length
            );
            favoriteAddImage.setLayoutParams(favoriteAddImageParams);
            favoriteAddImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.plussign, null));
            favoriteImagesLayout.addView(favoriteAddImage);
            ConstraintSet favoriteAddImageConstraint = new ConstraintSet();
            favoriteAddImageConstraint.clone(favoriteImagesLayout);
            Toodles.setConstraints(
                    favoriteAddImageConstraint,
                    favoriteAddImage.getId(),
                    favoriteImagesLayout.getId());
            favoriteAddImageConstraint.setHorizontalBias(favoriteAddImage.getId(), 0f);
            favoriteAddImageConstraint.setVerticalBias(favoriteAddImage.getId(), 1f);
            favoriteAddImageConstraint.applyTo(favoriteImagesLayout);


            value_favorites = new TextView(requireContext());
            LinearLayout.LayoutParams value_favoritesParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            value_favorites.setLayoutParams(value_favoritesParams);
            value_favorites.setTextColor(Color.BLACK);
            value_favorites.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            value_favorites.setTypeface(value_favorites.getTypeface(), Typeface.BOLD);
            value_favorites.setMaxLines(4);
            value_favorites.setEllipsize(TextUtils.TruncateAt.END);
            value_favorites.setText(favoriteProduct.name);


            favoriteLayout.addView(favoriteImagesLayout);
            favoriteLayout.addView(value_favorites);
            favoritesLayout.addView(favoriteLayout);
        }
    }

    private void thread_addProductBasket(Integer favoriteProductId){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            String url = MessageFormat.format("{0}?{1}={2}&{3}={4}&{5}={6}",
                    "http://web-schedule.zapto.org:5000/Api/User/AddProductToBasket",
                    "userId", UserInfo.id,
                    "productId", favoriteProductId,
                    "count", 1);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(requireActivity().getApplication());
                    connection = new ControllerAPI().connectionPost(url);
                    connection.getResponseCode();
                } catch (IOException e){
                    e.printStackTrace();
                }

                requireActivity().runOnUiThread(() -> {
                    if (isConnected) {
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                Log.d("favoriteProduct ID", String.valueOf(favoriteProductId));
                                UserInfo.countInBasket++;
                                value_favoriteBasket.setText(String.valueOf(UserInfo.countInBasket));

                                Toast toast = Toast.makeText(getContext(),
                                        "Product added successfully", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP , 0, 0);
                                toast.show();
                            }
                            else {
                                Toast.makeText(getContext(),
                                        "Failed to add item to basket",Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(getContext(),"No internet connection",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void setTransition_BasketActivity(){
        //Toast.makeText(getContext(),"setTransition_Basket", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), BasketActivity.class));
    }

    private void alert_failed(String message){
        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setMessage(message)
                .setPositiveButton("Retry", ((dialog1, which) -> {
                    favorites_scrollView.setVisibility(View.GONE);
                    favoritesFragmentProgressBar.setVisibility(View.VISIBLE);
                    thread_FavoritesFragment();
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