package com.example.coffees.Menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.coffees.GlobalClasses.ControllerAPI;
import com.example.coffees.GlobalClasses.Toodles;
import com.example.coffees.MainActivity;
import com.example.coffees.MenuClasses.CategoryFragmentProduct;
import com.example.coffees.MenuClasses.CategoryFragmentSubcategory;
import com.example.coffees.GlobalClasses.ProductPictures;
import com.example.coffees.MenuClasses.MenuFragmentCategory;
import com.example.coffees.MenuClasses.MenuFragmentMenuPos;
import com.example.coffees.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CategoryFragment extends Fragment {
    View fragmentView;

    private int margin;
    private int margin_dp;

    private LinearLayout category_scrollLayout;
    private LinearLayout categoryLayout;
    private TextView title_category;
    private HorizontalScrollView content_categoryScrollView;
    private LinearLayout content_categoryScrollLayout;
    private LinearLayout productLayout;
    private CardView productCardView;
    private ImageView productImage;
    private TextView productName;

    Bundle bundle = new Bundle();

    private ScrollView category_scrollView;
    private ProgressBar categoryFragmentProgressBar;

    int getBundle_id;   //id для отправки запроса

    private boolean isConnected = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_category, container, false);


        MainActivity activity = (MainActivity)getActivity();
        assert activity != null;
        Bundle receiveBundle = getArguments();

        if (receiveBundle != null){
            getBundle_id = receiveBundle.getInt("id", 0);
            String getBundle_title = receiveBundle.getString("title");
            activity.setToolbar(getBundle_title, ResourcesCompat.getDrawable(
                    getResources(), R.drawable.ic_arrow_left, null
            ));
            if (activity.getToolbar() != null)
                activity.getToolbar().setNavigationOnClickListener(v -> activity.onBackPressed());

            category_scrollView = fragmentView.findViewById(R.id.category_scrollView);
            category_scrollView.setVisibility(View.GONE);
            categoryFragmentProgressBar = fragmentView.findViewById(R.id.categoryFragmentProgressBar);
            categoryFragmentProgressBar.setVisibility(View.VISIBLE);

            thread_CategoryFragment();
        }
        else {
            activity.setToolbar("Default title", ResourcesCompat.getDrawable(
                    getResources(), R.drawable.ic_arrow_left, null
            ));
            alert_failed("Failed get data from previous page");
        }


        return fragmentView;
    }

    private void thread_CategoryFragment(){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            LinkedHashMap<CategoryFragmentSubcategory, ArrayList<CategoryFragmentProduct>> data = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}",
                    "http://web-schedule.zapto.org:5000/Api/Product/GetAllSubcategoryIncludeProducts",
                    "categoryId", getBundle_id);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(requireActivity().getApplication());
                    connection = controllerAPI.connectionGet(url);
                    if (connection != null && connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                        data = controllerAPI.categoryFragment(connection);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                requireActivity().runOnUiThread(() -> {
                    categoryFragmentProgressBar.setVisibility(View.GONE);
                    if (isConnected){
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                if (data.size() != 0){
                                    category_scrollView.setVisibility(View.VISIBLE);
                                    categoryFragmentProgressBar.setVisibility(View.GONE);
                                    createPageContent(data);
                                }
                                else
                                    alert_failed("Failed to parse json from server");

                            }
                            else {
                                alert_failed("Failed to get data from server");
                                category_scrollView.setVisibility(View.GONE);
                                categoryFragmentProgressBar.setVisibility(View.VISIBLE);
                            }
                        } catch (IOException e) {
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

    private void createPageContent(LinkedHashMap<CategoryFragmentSubcategory, ArrayList<CategoryFragmentProduct>> data){
        margin = 5;
        margin_dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin,
                getResources().getDisplayMetrics());

        category_scrollLayout = fragmentView.findViewById(R.id.category_scrollLayout);

        for (CategoryFragmentSubcategory curSubcategory: data.keySet()){
            ArrayList<CategoryFragmentProduct> products = data.get(curSubcategory);


            categoryLayout = new LinearLayout(requireContext());
            categoryLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams categoryLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            categoryLayoutParams.bottomMargin = margin_dp;
            categoryLayout.setLayoutParams(categoryLayoutParams);


            title_category = new TextView(requireContext());
            LinearLayout.LayoutParams title_categoryParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            title_categoryParams.setMargins(2 * margin_dp, 2 * margin_dp,
                    2 * margin_dp, 2 * margin_dp);
            title_category.setLayoutParams(title_categoryParams);
            title_category.setMaxLines(2);
            title_category.setEllipsize(TextUtils.TruncateAt.END);
            title_category.setTextColor(Color.BLACK);
            title_category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            title_category.setTypeface(title_category.getTypeface(), Typeface.BOLD);
            title_category.setText(curSubcategory.name);


            content_categoryScrollView = new HorizontalScrollView(requireContext());
            LinearLayout.LayoutParams content_categoryScrollViewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            content_categoryScrollView.setLayoutParams(content_categoryScrollViewParams);


            content_categoryScrollLayout = new LinearLayout(requireContext());
            content_categoryScrollLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams content_categoryScrollLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            content_categoryScrollLayoutParams.bottomMargin = 2*margin_dp;
            content_categoryScrollLayout.setLayoutParams(content_categoryScrollLayoutParams);


            for (CategoryFragmentProduct curProduct: products){
                productLayout = new LinearLayout(requireContext());
                productLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams productLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                productLayoutParams.setMargins(margin_dp,0, margin_dp,0);
                productLayout.setLayoutParams(productLayoutParams);
                productLayout.setOnClickListener(v -> setTransition(curProduct.id));


                productCardView = new CardView(requireContext());
                int side_length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        140, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams productCardViewParams = new LinearLayout.LayoutParams(
                        side_length, side_length
                );
                productCardViewParams.bottomMargin = margin_dp;
                productCardView.setLayoutParams(productCardViewParams);
                productCardView.setRadius((float) side_length/2);


                productImage = new ImageView(requireContext());
                LinearLayout.LayoutParams productImageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                productImage.setLayoutParams(productImageParams);
                Integer drawId = ProductPictures.productPictures.get(curProduct.id);
                if (drawId == null){
                    drawId = ProductPictures.defaultPicture;
                }
                Bitmap result_productImage = Toodles.cropImage(drawId, 0.65f,
                        getResources());
                productImage.setImageBitmap(result_productImage);


                productName = new TextView(requireContext());
                LinearLayout.LayoutParams productNameParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                productName.setLayoutParams(productNameParams);
                productName.setMinLines(2);
                productName.setMaxLines(2);
                productName.setEllipsize(TextUtils.TruncateAt.END);
                productName.setGravity(Gravity.CENTER_HORIZONTAL);
                productName.setTextColor(Color.BLACK);
                productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                productName.setText(curProduct.name);


                productCardView.addView(productImage);
                productLayout.addView(productCardView);
                productLayout.addView(productName);
                content_categoryScrollLayout.addView(productLayout);
            }

            content_categoryScrollView.addView(content_categoryScrollLayout);
            categoryLayout.addView(title_category);
            categoryLayout.addView(content_categoryScrollView);
            category_scrollLayout.addView(categoryLayout);
        }
    }

    private void setTransition(Integer id){
        bundle.putInt("id", id);
        //Navigation.findNavController(requireView()).navigate(R.id.action_category_product, bundle);
        Intent intent = new Intent(getActivity(), ProductActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void alert_failed(String message){
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setPositiveButton("Retry", ((dialog1, which) -> {
                    category_scrollView.setVisibility(View.GONE);
                    categoryFragmentProgressBar.setVisibility(View.VISIBLE);
                    thread_CategoryFragment();
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