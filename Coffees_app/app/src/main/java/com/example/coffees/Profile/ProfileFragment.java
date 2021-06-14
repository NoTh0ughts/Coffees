package com.example.coffees.Profile;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.coffees.GlobalClasses.ControllerAPI;
import com.example.coffees.GlobalClasses.Cards;
import com.example.coffees.GlobalClasses.ProductPictures;
import com.example.coffees.GlobalClasses.Toodles;
import com.example.coffees.GlobalClasses.UserInfo;
import com.example.coffees.MainActivity;
import com.example.coffees.ProfileClasses.ProfileFragmentModel;
import com.example.coffees.ProfileClasses.ProfileFragmentOrderInfo;
import com.example.coffees.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileFragment extends Fragment {
    View fragmentView;

    private int margin;
    private int margin_dp;

    private LinearLayout ordersContentLayout;
    private LinearLayout orderLayout;
    private ConstraintLayout orderImagesLayout;
    private CardView orderProductCardView;
    private ImageView orderProductImage;
    private CardView orderSubproductCardView;
    private ImageView orderSubproductImage;
    private LinearLayout orderInfoLayout;
    //private TextView orderTotal;
    private TextView orderAddress;
    private TextView orderDatetime;


    private Integer notAuthorizedPageId = null;
    private ScrollView profile_scrollView;
    private ProgressBar profileFragmentProgressBar;

    boolean isConnected = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        MainActivity activity = (MainActivity)getActivity();
        assert activity != null;
        activity.setToolbar("Coffees",null);
        setHasOptionsMenu(true);


        profile_scrollView = fragmentView.findViewById(R.id.profile_scrollView);
        profile_scrollView.setVisibility(View.GONE);
        profileFragmentProgressBar = fragmentView.findViewById(R.id.profileFragmentProgressBar);
        profileFragmentProgressBar.setVisibility(View.VISIBLE);


        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        profileFragmentProgressBar.setVisibility(View.VISIBLE);
        isLoggedIn();
    }

    private void isLoggedIn(){
        if (UserInfo.id != null){   //Если пользователь авторизован
            thread_ProfileFragment();

            MainActivity activity = (MainActivity) getActivity();
            if (activity != null)
                if (activity.getToolbar() != null)
                    getActivity().invalidateOptionsMenu();

            //На страницу зашли повторно, после того как неавторизованный пользователь
            //авторизировался
            ConstraintLayout profileFragmentLayout = fragmentView.findViewById(
                    R.id.profileFragmentLayout);
            if (notAuthorizedPageId != null)
                profileFragmentLayout.removeView(fragmentView.findViewById(notAuthorizedPageId));
        }
        else {
            profile_scrollView.setVisibility(View.GONE);
            profileFragmentProgressBar.setVisibility(View.GONE);
            notAuthorizedPageId = Toodles.notAuthorizedPageContent(getActivity(), fragmentView,
                    R.id.profileFragmentLayout, getContext(), getResources());
        }
    }

    private void setUserAvatar(Drawable image, ImageView imageView){
        BitmapDrawable bitmapImage = (BitmapDrawable) image;
        Bitmap bitmap = bitmapImage.getBitmap();
        int pixel = bitmap.getPixel(1,bitmap.getHeight()/2);

        imageView.setBackgroundColor(pixel);
    }

    private void thread_ProfileFragment(){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ArrayList<ProfileFragmentOrderInfo> profileOrders = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}",
                    "http://web-schedule.zapto.org:5000/Api/User/GetCompletedUserOrders",
                    "userId", UserInfo.id);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(requireActivity().getApplication());
                    connection = controllerAPI.connectionGet(url);
                    if (connection != null
                            && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        profileOrders = controllerAPI.profileOrders(connection);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

                requireActivity().runOnUiThread(() -> {
                    profileFragmentProgressBar.setVisibility(View.GONE);
                    if (isConnected){
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                profile_scrollView.setVisibility(View.VISIBLE);
                                createUserContent();
                                createPageContent(profileOrders);
                            }
                            else {
                                alert_failed("Failed get data from server");
                                profile_scrollView.setVisibility(View.GONE);
                                profileFragmentProgressBar.setVisibility(View.VISIBLE);
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

    private void createUserContent(){
        //Информация о пользователе
        ImageView userImageView = fragmentView.findViewById(R.id.userImageView);
        Drawable userAvatar = ResourcesCompat.getDrawable(getResources(),
                R.drawable.person, null);
        setUserAvatar(userAvatar, userImageView);


        TextView user_name = fragmentView.findViewById(R.id.user_name);
        user_name.setText(UserInfo.username);


        TextView value_since = fragmentView.findViewById(R.id.value_since);
        String dateSince_app;
        dateSince_app = parse_userSince(UserInfo.since);
        value_since.setText(dateSince_app);


        ImageView discountCardImage = fragmentView.findViewById(R.id.discountCardImage);
        Integer cardDrawId = Cards.cardsPictures.get(UserInfo.cardId);
        if (cardDrawId == null){
            cardDrawId = Cards.defaultCard;
        }
        discountCardImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                cardDrawId, null));
    }

    private void createPageContent(ArrayList<ProfileFragmentOrderInfo> profileOrders){
        margin = 5;
        margin_dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin,
                getResources().getDisplayMetrics());


        //Информация о заказах
        ordersContentLayout = fragmentView.findViewById(R.id.ordersContentLayout);
        if (profileOrders.size() == 0){
            TextView test_textView = new TextView(requireContext());
            test_textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            test_textView.setGravity(Gravity.CENTER_HORIZONTAL);
            test_textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            test_textView.setText("No orders found");
            ordersContentLayout.addView(test_textView);

            return;
        }
        for (ProfileFragmentOrderInfo orderInfo: profileOrders){
            String dateOrder_app;
            dateOrder_app = parse_dateOrder(orderInfo.dateOrder);
            String finalDateOrder_app = dateOrder_app;


            orderLayout = new LinearLayout(requireContext());
            orderLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams orderLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            orderLayoutParams.bottomMargin = margin_dp;
            orderLayout.setLayoutParams(orderLayoutParams);
            orderLayout.setOnClickListener(v ->
                    setTransition_UserOrdersFragment(orderInfo.orderId, finalDateOrder_app));


            orderImagesLayout = new ConstraintLayout(requireContext());
            orderImagesLayout.setId(View.generateViewId());
            int orderImagesLayout_size = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 110, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams orderImagesLayoutParams = new LinearLayout.LayoutParams(
                    orderImagesLayout_size, orderImagesLayout_size);
            orderImagesLayoutParams.rightMargin = 2 * margin_dp;
            orderImagesLayout.setLayoutParams(orderImagesLayoutParams);


            orderProductCardView = new CardView(requireContext());
            orderProductCardView.setId(View.generateViewId());
            int orderProductCardView_size = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams orderProductCardViewParams = new LinearLayout.LayoutParams(
                    orderProductCardView_size, orderProductCardView_size);
            orderProductCardView.setLayoutParams(orderProductCardViewParams);
            orderProductCardView.setRadius((float) orderProductCardView_size/2);
            //Назначим layout_constraint для компонента
            orderImagesLayout.addView(orderProductCardView);
            ConstraintSet orderProductCardViewConstraint = new ConstraintSet();
            orderProductCardViewConstraint.clone(orderImagesLayout);
            Toodles.setConstraints(
                    orderProductCardViewConstraint,
                    orderProductCardView.getId(),
                    orderImagesLayout.getId());
            orderProductCardViewConstraint.setVerticalBias(orderProductCardView.getId(), 0f);
            orderProductCardViewConstraint.setHorizontalBias(orderProductCardView.getId(), 0f);
            orderProductCardViewConstraint.applyTo(orderImagesLayout);


            orderProductImage = new ImageView(requireContext());
            LinearLayout.LayoutParams orderProductImageParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            orderProductImage.setLayoutParams(orderProductImageParams);
            Integer drawId = null;
            if (!orderInfo.products.isEmpty()){
                drawId = ProductPictures.productPictures.get(orderInfo.products.get(0).id);
                if (drawId == null){
                    drawId = ProductPictures.defaultPicture;
                }
            }
            float zoom_rate = 0.65f;
            Bitmap result_orderProductImage = Toodles.cropImage(drawId, zoom_rate, getResources());
            orderProductImage.setImageBitmap(result_orderProductImage);


            orderProductCardView.addView(orderProductImage);

            if (orderInfo.products.size() > 1){
                orderSubproductCardView = new CardView(requireContext());
                orderSubproductCardView.setId(View.generateViewId());
                int orderSubproductCardView_size = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams orderSubproductCardViewParams = new LinearLayout.LayoutParams(
                        orderSubproductCardView_size, orderSubproductCardView_size);
                orderSubproductCardView.setLayoutParams(orderSubproductCardViewParams);
                orderSubproductCardView.setRadius((float) orderSubproductCardView_size/2);
                //Назначи layout_constraint для компонента
                orderImagesLayout.addView(orderSubproductCardView);
                ConstraintSet orderSubproductCardViewConstraint = new ConstraintSet();
                orderSubproductCardViewConstraint.clone(orderImagesLayout);
                Toodles.setConstraints(
                        orderSubproductCardViewConstraint,
                        orderSubproductCardView.getId(),
                        orderImagesLayout.getId());
                orderSubproductCardViewConstraint.setVerticalBias(orderSubproductCardView.getId(), 1f);
                orderSubproductCardViewConstraint.setHorizontalBias(orderSubproductCardView.getId(), 1f);
                orderSubproductCardViewConstraint.applyTo(orderImagesLayout);


                orderSubproductImage = new ImageView(requireContext());
                LinearLayout.LayoutParams orderSubproductImageParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                orderSubproductImage.setLayoutParams(orderSubproductImageParams);
                Integer subDrawId = ProductPictures.productPictures.get(orderInfo.products.get(1).id);
                if (subDrawId == null){
                    subDrawId = ProductPictures.defaultPicture;
                }
                Bitmap result_orderSubproductImage = Toodles.cropImage(subDrawId, zoom_rate, getResources());
                orderSubproductImage.setImageBitmap(result_orderSubproductImage);

                orderSubproductCardView.addView(orderSubproductImage);
            }


            orderInfoLayout = new LinearLayout(requireContext());
            orderInfoLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams orderInfoLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            orderInfoLayout.setLayoutParams(orderInfoLayoutParams);


//            orderTotal = new TextView(requireContext());
//            LinearLayout.LayoutParams orderTotalParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    0);
//            orderTotal.setLayoutParams(orderTotalParams);
//            orderTotal.setTextColor(Color.BLACK);
//            orderTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//            orderTotal.setTypeface(orderTotal.getTypeface(), Typeface.BOLD);
//            orderTotal.setMaxLines(1);
//            orderTotal.setEllipsize(TextUtils.TruncateAt.END);
//            orderTotal.setText(String.valueOf(orderInfo.total));


            orderAddress = new TextView(requireContext());
            LinearLayout.LayoutParams orderAddressParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1);
            orderAddress.setLayoutParams(orderAddressParams);
            orderAddress.setTextColor(Color.BLACK);
            orderAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            orderAddress.setTypeface(orderAddress.getTypeface(), Typeface.BOLD);
            orderAddress.setMaxLines(2);
            orderAddress.setEllipsize(TextUtils.TruncateAt.END);
            orderAddress.setText(orderInfo.address);


            orderDatetime = new TextView(requireContext());
            LinearLayout.LayoutParams orderDatetimeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0);
            orderDatetime.setLayoutParams(orderDatetimeParams);
            orderDatetime.setGravity(Gravity.END);
            orderDatetime.setTextColor(Color.DKGRAY);
            orderDatetime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            orderDatetime.setText(finalDateOrder_app);



            //orderInfoLayout.addView(orderTotal);
            orderInfoLayout.addView(orderAddress);
            orderInfoLayout.addView(orderDatetime);
            orderLayout.addView(orderImagesLayout);
            orderLayout.addView(orderInfoLayout);
            ordersContentLayout.addView(orderLayout);
        }
    }

    private void setTransition_UserOrdersFragment(Integer orderId, String orderDate){
        Bundle bundle = new Bundle();
        bundle.putInt("id", orderId);
        bundle.putString("title", orderDate);
        Navigation.findNavController(fragmentView)
                .navigate(R.id.action_nav_profileFragment_to_userOrdersFragment, bundle);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NotNull MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menu.clear();

        menuInflater.inflate(R.menu.menu_toolbar, menu);
        MenuItem logout = menu.findItem(R.id.action_logout);


        if (UserInfo.id != null){
            logout.setVisible(true);
            logout.setOnMenuItemClickListener(item -> {
                UserInfo.logOut();
                if (ordersContentLayout != null && ordersContentLayout.getChildCount() > 2)
                    ordersContentLayout.removeAllViews();

                profile_scrollView.setVisibility(View.GONE);
                profileFragmentProgressBar.setVisibility(View.GONE);
                notAuthorizedPageId = Toodles.notAuthorizedPageContent(getActivity(), fragmentView,
                        R.id.profileFragmentLayout, getContext(), getResources());
                logout.setVisible(false);

               return false;
            });
        }
        else
            logout.setVisible(false);
    }

    private void alert_failed(String message){
        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setMessage(message)
                .setPositiveButton("Retry", ((dialog1, which) -> {
                    profile_scrollView.setVisibility(View.GONE);
                    profileFragmentProgressBar.setVisibility(View.VISIBLE);
                    thread_ProfileFragment();
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

    public String parse_dateOrder(String input){
        SimpleDateFormat sdfIn = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" );
        SimpleDateFormat sdfIn_mils = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.S'Z'" );
        SimpleDateFormat sdfOut = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        Date date = null;
        String output = null;

        try {
            date = sdfIn.parse(input);
            if (date != null)
                output = sdfOut.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            output = "unknown";
            try {
                date = sdfIn_mils.parse(input);
                if (date != null){
                    output = sdfOut.format(date);
                }
            } catch (ParseException e1){
                e1.printStackTrace();
                output = "unknown";
            }
        }

        return output;
    }

    public String parse_userSince(String input){
        SimpleDateFormat sdfIn = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" );
        SimpleDateFormat sdfIn_mils = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.S'Z'" );
        SimpleDateFormat sdfOut = new SimpleDateFormat("dd.MM.yyyy");

        Date date = null;
        String output = null;

        try {
            date = sdfIn.parse(input);
            if (date != null)
                output = sdfOut.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            output = "unknown";
            try {
                date = sdfIn_mils.parse(input);
                if (date != null){
                    output = sdfOut.format(date);
                }
            } catch (ParseException e1){
                e1.printStackTrace();
                output = "unknown";
            }
        }

        return output;
    }
}