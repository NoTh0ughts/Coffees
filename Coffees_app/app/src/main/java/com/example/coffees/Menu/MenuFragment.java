package com.example.coffees.Menu;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Picture;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffees.GlobalClasses.ControllerAPI;
import com.example.coffees.GlobalClasses.Toodles;
import com.example.coffees.MainActivity;
import com.example.coffees.MenuClasses.MenuFragmentCategory;
import com.example.coffees.MenuClasses.MenuFragmentMenuPos;
import com.example.coffees.GlobalClasses.ProductPictures;
import com.example.coffees.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MenuFragment extends Fragment {
    View fragmentView;

    private int margin;
    private int margin_dp;
    private int count_menuPosition = 0;
    private int count_categories = 0;

    private LinearLayout menu_scrollLayout;
    private LinearLayout menuPositionLayout;
    private TextView title_menuPosition;
    private LinearLayout categoriesLayout;
    private LinearLayout categoryLayout;
    private CardView categoryCardView;
    private ImageView categoryImage;
    private TextView value_category;

    Bundle bundle = new Bundle();

    private ScrollView menu_scrollView;
    private ProgressBar menuFragmentProgressBar;

    private boolean isConnected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_menu, container, false);


        MainActivity activity = (MainActivity)getActivity();
        assert activity != null;
        activity.setToolbar("Coffees",null);
        setHasOptionsMenu(true);


        menu_scrollView = fragmentView.findViewById(R.id.menu_scrollView);
        menu_scrollView.setVisibility(View.GONE);
        menuFragmentProgressBar = fragmentView.findViewById(R.id.menuFragmentProgressBar);
        menuFragmentProgressBar.setVisibility(View.VISIBLE);


        thread_MenuFragment();


        return fragmentView;
    }

    private void thread_MenuFragment(){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            LinkedHashMap<MenuFragmentMenuPos, ArrayList<MenuFragmentCategory>> data = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = "http://web-schedule.zapto.org:5000/Api/Product/GetAllMenuPositionsIncludeCategory";

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(requireActivity().getApplication());
                    connection = controllerAPI.connectionGet(url);
                    if (connection != null && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        data = controllerAPI.menuFragment(connection);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                requireActivity().runOnUiThread(() -> {
                    menuFragmentProgressBar.setVisibility(View.GONE);
                    if (isConnected){
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                if (data.size() != 0){
                                    menu_scrollView.setVisibility(View.VISIBLE);
                                    menuFragmentProgressBar.setVisibility(View.GONE);
                                    createPageContent(data);
                                }
                                else
                                    alert_failed("Failed to parse json from server");

                            }
                            else {
                                alert_failed("Failed to get data from server");
                                menu_scrollView.setVisibility(View.GONE);
                                menuFragmentProgressBar.setVisibility(View.VISIBLE);
                            }
                        } catch (IOException e) {
                            Toast.makeText(requireContext(), "Cant get response code",
                                    Toast.LENGTH_SHORT).show();
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menu.clear();

        menuInflater.inflate(R.menu.menu_toolbar, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(true);
    }

    private void setTransition(Integer id, String title){
        bundle.putInt("id", id);
        bundle.putString("title", title);
        Navigation.findNavController(requireView()).navigate(R.id.action_menu_category, bundle);
    }

    private void createPageContent(LinkedHashMap<MenuFragmentMenuPos, ArrayList<MenuFragmentCategory>> data){
        margin = 5;
        margin_dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin,
                getResources().getDisplayMetrics());


        menu_scrollLayout = fragmentView.findViewById(R.id.menu_scrollLayout);

        for (MenuFragmentMenuPos curMenuPos : data.keySet()){
            ArrayList<MenuFragmentCategory> categories = data.get(curMenuPos);


            menuPositionLayout = new LinearLayout(requireContext());
            menuPositionLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams menuPositionLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            menuPositionLayoutParams.bottomMargin = 2*margin_dp;
            menuPositionLayout.setLayoutParams(menuPositionLayoutParams);


            title_menuPosition = new TextView(requireContext());
            LinearLayout.LayoutParams title_menuPositionParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            title_menuPositionParams.bottomMargin = margin_dp;
            title_menuPosition.setLayoutParams(title_menuPositionParams);
            title_menuPosition.setTextColor(Color.BLACK);
            title_menuPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            title_menuPosition.setTypeface(title_menuPosition.getTypeface(), Typeface.BOLD);
            title_menuPosition.setText(curMenuPos.name);


            categoriesLayout = new LinearLayout(requireContext());
            categoriesLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams categoriesLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            categoriesLayout.setLayoutParams(categoriesLayoutParams);

            for (MenuFragmentCategory curCategory: categories) {
                categoryLayout = new LinearLayout(requireContext());
                categoryLayout.setId(curCategory.id);
                categoryLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams categoryLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                categoryLayoutParams.bottomMargin = 2*margin_dp;
                categoryLayout.setLayoutParams(categoryLayoutParams);
                categoryLayout.setGravity(Gravity.CENTER_VERTICAL);
                categoryLayout.setOnClickListener(v ->
                        setTransition(curCategory.id, curCategory.name));


                categoryCardView = new CardView(requireContext());
                int side_length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        70, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams categoryCardViewParams = new LinearLayout.LayoutParams(
                        side_length,
                        side_length,
                        0
                );
                categoryCardViewParams.rightMargin = margin_dp;
                categoryCardView.setLayoutParams(categoryCardViewParams);
                categoryCardView.setRadius((float) side_length/2);


                categoryImage = new ImageView(requireContext());
                LinearLayout.LayoutParams categoryImageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                categoryImage.setLayoutParams(categoryImageParams);
                Integer drawId = ProductPictures.productPictures.get(curCategory.productId);
                if (drawId == null){
                    drawId = ProductPictures.defaultPicture;
                }
                Bitmap result_categoryImage = Toodles.cropImage(drawId, 0.65f,
                        getResources());
                categoryImage.setImageBitmap(result_categoryImage);


                value_category = new TextView(requireContext());
                LinearLayout.LayoutParams value_categoryParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );
                value_category.setLayoutParams(value_categoryParams);
                value_category.setMaxLines(1);
                value_category.setEllipsize(TextUtils.TruncateAt.END);
                value_category.setTextColor(Color.BLACK);
                value_category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                value_category.setTypeface(value_category.getTypeface(), Typeface.BOLD);
                value_category.setText(curCategory.name);


                categoryCardView.addView(categoryImage);
                categoryLayout.addView(categoryCardView);
                categoryLayout.addView(value_category);
                categoriesLayout.addView(categoryLayout);
            }

            menuPositionLayout.addView(title_menuPosition);
            menuPositionLayout.addView(categoriesLayout);
            menu_scrollLayout.addView(menuPositionLayout);
        }
    }

    private void alert_failed(String message){
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setPositiveButton("Retry", ((dialog1, which) -> {
                    menu_scrollView.setVisibility(View.GONE);
                    menuFragmentProgressBar.setVisibility(View.VISIBLE);
                    thread_MenuFragment();
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