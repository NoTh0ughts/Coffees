package com.example.coffees.Profile;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
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

import com.example.coffees.GlobalClasses.ControllerAPI;
import com.example.coffees.GlobalClasses.Toodles;
import com.example.coffees.GlobalClasses.ProductPictures;
import com.example.coffees.GlobalClasses.UserInfo;
import com.example.coffees.MainActivity;
import com.example.coffees.ProfileClasses.UserOrdersFragmentModel;
import com.example.coffees.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;

public class UserOrdersFragment extends Fragment {
    View fragmentView;

    private int margin;
    private int margin_dp;

    private TextView value_uordersTotal;
    private LinearLayout uordersLayout;
    private LinearLayout uorderLayout;
    private CardView uorderProductCardView;
    private ImageView uorderProductImage;
    private LinearLayout uorderInfoLayout;
    private TextView title_uorderInfo;
    private LinearLayout count_uorderInfoLayout;
    private TextView text_countUOrderInfo;
    private TextView value_countUOrderInfo;
    //private TextView value_uorderTotal;

    private ScrollView uorders_scrollView;
    private ProgressBar uordersFragmentProgressBar;

    private int orderId;
    private boolean isConnected = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_user_orders, container, false);

        MainActivity activity = (MainActivity)getActivity();
        assert activity != null;
        Bundle receiveBundle = getArguments();
        if (receiveBundle != null){
            orderId = receiveBundle.getInt("id", 0);
            String title = receiveBundle.getString("title");
            activity.setToolbar(title, ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_arrow_left, null));

            if (orderId != 0){
                uorders_scrollView = fragmentView.findViewById(R.id.uorders_scrollView);
                uorders_scrollView.setVisibility(View.GONE);
                uordersFragmentProgressBar = fragmentView.findViewById(R.id.uordersFragmentProgressBar);
                uordersFragmentProgressBar.setVisibility(View.VISIBLE);

                thread_UserOrderFragment();
            }
            else
                throw new IndexOutOfBoundsException("Id не был получен");
        }
        else {
            activity.setToolbar(null, ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_arrow_left, null));
        }
        activity.getToolbar().setNavigationOnClickListener(v -> activity.onBackPressed());


        return fragmentView;
    }

    public void thread_UserOrderFragment(){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ArrayList<UserOrdersFragmentModel> userOrders = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}",
                    "http://web-schedule.zapto.org:5000/Api/User/GetOrderByIdIncludeProducts",
                    "orderid", orderId);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(requireActivity().getApplication());
                    connection = controllerAPI.connectionGet(url);
                    if (connection != null
                            && connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                        userOrders = controllerAPI.userOrdersFragment(connection);
                } catch (IOException e){
                    e.printStackTrace();
                }

                requireActivity().runOnUiThread(() -> {
                    uordersFragmentProgressBar.setVisibility(View.GONE);
                    if (isConnected){
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                uorders_scrollView.setVisibility(View.VISIBLE);
                                createPageContent(userOrders);
                            }
                            else {
                                alert_failed("Failed get data from server");
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

    public void createPageContent(ArrayList<UserOrdersFragmentModel> userOrders){
        margin = 5;
        margin_dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin,
                getResources().getDisplayMetrics());


        value_uordersTotal = fragmentView.findViewById(R.id.value_uordersTotal);
        //int uordersTotal = 0;
        uordersLayout = fragmentView.findViewById(R.id.uordersLayout);
        for(UserOrdersFragmentModel userOrder : userOrders){
            uorderLayout = new LinearLayout(requireContext());
            uorderLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams uorderLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            uorderLayoutParams.bottomMargin = 4 * margin_dp;
            uorderLayout.setLayoutParams(uorderLayoutParams);


            uorderProductCardView = new CardView(requireContext());
            int uorderProductCardView_size = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()
            );
            LinearLayout.LayoutParams uorderProductCardViewParams = new LinearLayout.LayoutParams(
                    uorderProductCardView_size, uorderProductCardView_size
            );
            uorderProductCardViewParams.rightMargin = 2 * margin_dp;
            uorderProductCardView.setLayoutParams(uorderProductCardViewParams);
            uorderProductCardView.setRadius((float) uorderProductCardView_size/2);


            uorderProductImage = new ImageView(requireContext());
            LinearLayout.LayoutParams uorderProductImageParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            uorderProductImage.setLayoutParams(uorderProductImageParams);
            Integer drawId = ProductPictures.productPictures.get(userOrder.productId);
            if (drawId == null){
                drawId = ProductPictures.defaultPicture;
            }
            float zoom_rate = 0.65f;
            Bitmap result_uorderProductImage = Toodles.cropImage(drawId, zoom_rate, getResources());
            uorderProductImage.setImageBitmap(result_uorderProductImage);


            uorderInfoLayout = new LinearLayout(requireContext());
            uorderInfoLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams uorderInfoLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            uorderInfoLayout.setLayoutParams(uorderInfoLayoutParams);


            title_uorderInfo = new TextView(requireContext());
            LinearLayout.LayoutParams title_uorderInfoParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0
            );
            title_uorderInfo.setLayoutParams(title_uorderInfoParams);
            title_uorderInfo.setTextColor(Color.BLACK);
            title_uorderInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            title_uorderInfo.setTypeface(title_uorderInfo.getTypeface(), Typeface.BOLD);
            title_uorderInfo.setMaxLines(2);
            title_uorderInfo.setEllipsize(TextUtils.TruncateAt.END);
            if (userOrder.productName != null)
                title_uorderInfo.setText(userOrder.productName);
            else
                title_uorderInfo.setText("unknown");


            count_uorderInfoLayout = new LinearLayout(requireContext());
            count_uorderInfoLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams count_uorderInfoLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0
            );
            count_uorderInfoLayout.setLayoutParams(count_uorderInfoLayoutParams);


            text_countUOrderInfo = new TextView(requireContext());
            LinearLayout.LayoutParams text_countUOrderInfoParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            text_countUOrderInfoParams.rightMargin = margin_dp;
            text_countUOrderInfo.setLayoutParams(text_countUOrderInfoParams);
            text_countUOrderInfo.setTextColor(Color.DKGRAY);
            text_countUOrderInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            text_countUOrderInfo.setText("count:");


            value_countUOrderInfo = new TextView(requireContext());
            LinearLayout.LayoutParams value_countUOrderInfoParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            );
            value_countUOrderInfo.setLayoutParams(value_countUOrderInfoParams);
            value_countUOrderInfo.setTextColor(Color.DKGRAY);
            value_countUOrderInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            if (userOrder.productCount != null)
                value_countUOrderInfo.setText(String.valueOf(userOrder.productCount));
            else
                value_countUOrderInfo.setText("unknown");


//            value_uorderTotal = new TextView(requireContext());
//            LinearLayout.LayoutParams value_uorderTotalParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    1
//            );
//            value_uorderTotal.setLayoutParams(value_uorderTotalParams);
//            value_uorderTotal.setGravity(Gravity.BOTTOM);
//            value_uorderTotal.setTextColor(Color.BLACK);
//            value_uorderTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//            value_uorderTotal.setTypeface(value_uorderTotal.getTypeface(), Typeface.BOLD);
//            int orderTotal = userOrder.count * userOrder.price;
//            value_uorderTotal.setText(String.valueOf(orderTotal));


            count_uorderInfoLayout.addView(text_countUOrderInfo);
            count_uorderInfoLayout.addView(value_countUOrderInfo);
            uorderInfoLayout.addView(title_uorderInfo);
            uorderInfoLayout.addView(count_uorderInfoLayout);
            //uorderInfoLayout.addView(value_uorderTotal);
            uorderProductCardView.addView(uorderProductImage);
            uorderLayout.addView(uorderProductCardView);
            uorderLayout.addView(uorderInfoLayout);
            uordersLayout.addView(uorderLayout);
            //uordersTotal += orderTotal;
        }

        //value_uordersTotal.setText(String.valueOf(uordersTotal));
    }

    private void alert_failed(String message){
        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setMessage(message)
                .setPositiveButton("Retry", ((dialog1, which) -> {
                    uorders_scrollView.setVisibility(View.GONE);
                    uordersFragmentProgressBar.setVisibility(View.VISIBLE);
                    thread_UserOrderFragment();
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