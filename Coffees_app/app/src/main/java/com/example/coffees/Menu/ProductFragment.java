package com.example.coffees.Menu;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coffees.GlobalClasses.ControllerAPI;
import com.example.coffees.GlobalClasses.ProductPictures;
import com.example.coffees.MainActivity;
import com.example.coffees.MenuClasses.NutritionInfo;
import com.example.coffees.MenuClasses.ProductFragmentModel;
import com.example.coffees.R;

import java.text.MessageFormat;

public class ProductFragment extends Fragment {
    View fragmentView;

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



    //THIS FRAGMENT MUST BE DELETE


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        fragmentView = inflater.inflate(R.layout.fragment_product, container, false);
//
//        MainActivity activity = (MainActivity)getActivity();
//        assert activity != null;
//        activity.setToolbar(null,getResources().getDrawable(R.drawable.ic_arrow_left));
//        activity.setTransparentToolbar();
//        Toolbar toolbar = activity.getToolbar();
//        toolbar.getBackground().setAlpha(0);
//        toolbar.setNavigationOnClickListener(v -> {
//            activity.setDefaultToolbar();
//            activity.onBackPressed();
//        });
//        //if (toolbar != null)
//
//        createPageContent();
//
//        return fragmentView;
//    }
//
//    public void createPageContent(){
//        margin = 5;
//        margin_dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin,
//                getResources().getDisplayMetrics());
//
//
//        ControllerAPI contr = new ControllerAPI();
//        ProductFragmentModel productFragmentModel = contr.tmpProductFragment();
//
//
//        product_scrollLayout = fragmentView.findViewById(R.id.product_scrollLayout);
//
//        //Верхняя половина страницы
//        //Необходимо первостепенно создать картинку, чтобы достать цвет фона для использования далее
//        productImageBriefly = new ImageView(requireContext());
//        LinearLayout.LayoutParams productImageBrieflyParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//        );
//        productImageBriefly.setLayoutParams(productImageBrieflyParams);
//        Integer drawid = ProductPictures.productPictures.get(productFragmentModel.id);
//        if (drawid == null){
//            drawid = ProductPictures.defaultPicture;
//        }
//        productImageBriefly.setImageDrawable(requireContext().getDrawable(drawid));
//
//        Integer brieflyBackgroundColor = null;
//        if (productImageBriefly.getDrawable() != null)
//            brieflyBackgroundColor = setBrieflyBackgroundColor(productImageBriefly);
//
//
//
//        productBrieflyLayout = new LinearLayout(requireContext());
//        productBrieflyLayout.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams productBrieflyLayoutParamas = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        productBrieflyLayoutParamas.bottomMargin = 2 * margin_dp;
//        productBrieflyLayout.setLayoutParams(productBrieflyLayoutParamas);
//        productBrieflyLayout.setPadding(0, 2 * margin_dp, 0, 0);
//        productBrieflyLayout.setGravity(Gravity.CENTER_HORIZONTAL);
//        if (brieflyBackgroundColor != null){
//            productBrieflyLayout.setBackgroundColor(brieflyBackgroundColor);
//        }
//
//
//        productImageCardView = new CardView(requireContext());
//        int side_length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                180, getResources().getDisplayMetrics());
//        LinearLayout.LayoutParams productImageCardViewParams = new LinearLayout.LayoutParams(
//                side_length, side_length
//        );
//        productImageCardViewParams.bottomMargin = margin_dp;
//        productImageCardView.setLayoutParams(productImageCardViewParams);
//        productImageCardView.setRadius((float) side_length/2);
//        if (brieflyBackgroundColor != null)
//            productImageCardView.setBackgroundColor(brieflyBackgroundColor);
//        else
//            productImageCardView.setBackgroundColor(Color.GRAY);
//        productImageCardView.setElevation(0);
//
//
//        productNameBriefly = new TextView(requireContext());
//        LinearLayout.LayoutParams productNameBrieflyParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        productNameBrieflyParams.bottomMargin = margin_dp;
//        productNameBriefly.setLayoutParams(productNameBrieflyParams);
//        productNameBriefly.setGravity(Gravity.CENTER);
//        productNameBriefly.setTextColor(Color.WHITE);
//        productNameBriefly.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//        productNameBriefly.setText(productFragmentModel.name);
//
//
//        productCaloriesBriefly = new TextView(requireContext());
//        LinearLayout.LayoutParams productCaloriesBrieflyParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        productCaloriesBrieflyParams.bottomMargin = 2 * margin_dp;
//        productCaloriesBriefly.setLayoutParams(productCaloriesBrieflyParams);
//        productCaloriesBriefly.setGravity(Gravity.CENTER);
//        productCaloriesBriefly.setTextColor(Color.WHITE);
//        productCaloriesBriefly.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//        productCaloriesBriefly.setTypeface(productCaloriesBriefly.getTypeface(), Typeface.BOLD);
//        productCaloriesBriefly.setText(MessageFormat.format("{0} {1}", String.valueOf(productFragmentModel.calories), "cal."));
//
//
//        productImageCardView.addView(productImageBriefly);
//        productBrieflyLayout.addView(productImageCardView);
//        productBrieflyLayout.addView(productNameBriefly);
//        productBrieflyLayout.addView(productCaloriesBriefly);
//
//
//        //Нижняя половина страницы
//        productInfoLayout = new LinearLayout(requireContext());
//        productInfoLayout.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams productInfoLayoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        productInfoLayoutParams.setMargins(2 * margin_dp, 0,
//                2 * margin_dp, 2 * margin_dp);
//        productInfoLayout.setLayoutParams(productInfoLayoutParams);
//
//
//        nutritionLayout = new LinearLayout(requireContext());
//        nutritionLayout.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams nutritionLayoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        nutritionLayout.setLayoutParams(nutritionLayoutParams);
//
//
//        title_nutrition = new TextView(requireContext());
//        LinearLayout.LayoutParams title_nutritionParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        title_nutrition.setLayoutParams(title_nutritionParams);
//        title_nutrition.setTextColor(Color.BLACK);
//        title_nutrition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//        title_nutrition.setTypeface(title_nutrition.getTypeface(), Typeface.BOLD);
//        title_nutrition.setText("Nutrition Information");
//
//
//        content_nutritionLayout = new LinearLayout(requireContext());
//        content_nutritionLayout.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams content_nutritionLayoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        content_nutritionLayoutParams.setMargins(2 * margin_dp, 2 * margin_dp,
//                2 * margin_dp, 2 * margin_dp);
//        content_nutritionLayout.setLayoutParams(content_nutritionLayoutParams);
//
//
//
//        for (NutritionInfo currNutrition : productFragmentModel.nutritionInfo){
//            nutritionPositionLayout = new LinearLayout(requireContext());
//            nutritionPositionLayout.setOrientation(LinearLayout.HORIZONTAL);
//            LinearLayout.LayoutParams nutritionPositionLayoutParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            nutritionPositionLayoutParams.bottomMargin = margin_dp;
//            nutritionPositionLayout.setLayoutParams(nutritionPositionLayoutParams);
//            nutritionPositionLayout.setGravity(Gravity.CENTER_VERTICAL);
//
//
//            title_nutritionPosition = new TextView(requireContext());
//            LinearLayout.LayoutParams title_nutritionPositionParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    0
//            );
//            title_nutritionPositionParams.rightMargin = 2 * margin_dp;
//            title_nutritionPosition.setLayoutParams(title_nutritionPositionParams);
//            title_nutritionPosition.setTextColor(Color.BLACK);
//            title_nutritionPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//            title_nutritionPosition.setTypeface(title_nutritionPosition.getTypeface(), Typeface.BOLD);
//            title_nutritionPosition.setText(currNutrition.name);
//
//
//            value_nutritionPosition = new TextView(requireContext());
//            LinearLayout.LayoutParams value_nutritionPositionParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    0
//            );
//            value_nutritionPosition.setLayoutParams(value_nutritionPositionParams);
//            value_nutritionPosition.setTextColor(Color.DKGRAY);
//            value_nutritionPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//            value_nutritionPosition.setText(MessageFormat.format("{0} {1}", String.valueOf(currNutrition.value), "g"));
//
//
//            nutritionPositionLayout.addView(title_nutritionPosition);
//            nutritionPositionLayout.addView(value_nutritionPosition);
//            content_nutritionLayout.addView(nutritionPositionLayout);
//        }
//
//
//        ingredientsLayout = new LinearLayout(requireContext());
//        ingredientsLayout.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams ingredientsLayoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        ingredientsLayoutParams.bottomMargin = 10 * margin_dp;
//        ingredientsLayout.setLayoutParams(ingredientsLayoutParams);
//
//
//        title_ingredients = new TextView(requireContext());
//        LinearLayout.LayoutParams title_ingredientsParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        title_ingredients.setLayoutParams(title_ingredientsParams);
//        title_ingredients.setTextColor(Color.BLACK);
//        title_ingredients.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//        title_ingredients.setTypeface(title_ingredients.getTypeface(), Typeface.BOLD);
//        title_ingredients.setText("Ingredients");
//
//
//        content_ingredients = new TextView(requireContext());
//        LinearLayout.LayoutParams content_ingredientsParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        content_ingredientsParams.setMargins(2 * margin_dp, 2 * margin_dp,
//                2 * margin_dp, 2 * margin_dp);
//        content_ingredients.setLayoutParams(content_ingredientsParams);
//        content_ingredients.setTextColor(Color.DKGRAY);
//        content_ingredients.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//        String str_ingredients = "";
//        for (int i = 0; i < productFragmentModel.ingredients.size(); i++){
//            if (i == productFragmentModel.ingredients.size() - 1)
//                str_ingredients += productFragmentModel.ingredients.get(i);
//            else
//                str_ingredients += MessageFormat.format("{0}, ", productFragmentModel.ingredients.get(i));
//        }
//        content_ingredients.setText(str_ingredients);
//
//
//        nutritionLayout.addView(title_nutrition);
//        nutritionLayout.addView(content_nutritionLayout);
//        ingredientsLayout.addView(title_ingredients);
//        ingredientsLayout.addView(content_ingredients);
//
//        productInfoLayout.addView(nutritionLayout);
//        productInfoLayout.addView(ingredientsLayout);
//
//        product_scrollLayout.addView(productBrieflyLayout);
//        product_scrollLayout.addView(productInfoLayout);
//    }
//
//    public Integer setBrieflyBackgroundColor(ImageView img){
//        Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
//        return bitmap.getPixel(1, bitmap.getHeight()/2);
//    }
}