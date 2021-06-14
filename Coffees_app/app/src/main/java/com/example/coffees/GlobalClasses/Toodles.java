package com.example.coffees.GlobalClasses;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import com.example.coffees.Authorization.LoginActivity;
import com.example.coffees.R;

public class Toodles {

    public static boolean isNetworkAvailable(Application application) {

        ConnectivityManager connManager =
                (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            NetworkCapabilities capabilities =
                    connManager.getNetworkCapabilities(connManager.getActiveNetwork());
            if (capabilities != null){
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    return true;
                }
                else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                    return true;
                }
                else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                    return true;
                }
            }
        }
        else {
            try {
                NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static void setConstraints(ConstraintSet constraintSet, int siblingId, int parentId){
        constraintSet.connect(siblingId, ConstraintSet.LEFT, parentId, ConstraintSet.LEFT);
        constraintSet.connect(siblingId, ConstraintSet.TOP, parentId, ConstraintSet.TOP);
        constraintSet.connect(siblingId, ConstraintSet.RIGHT, parentId, ConstraintSet.RIGHT);
        constraintSet.connect(siblingId, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM);

    }

    public static Bitmap cropImage(Integer image, float zoom_rate, Resources resources){
        if (image != null){
            BitmapDrawable bitmapImage = (BitmapDrawable) ResourcesCompat.getDrawable(
                    resources,image,null
            );
            int width = bitmapImage.getBitmap().getWidth();     //Ширина картинки
            int height = bitmapImage.getBitmap().getHeight();   //Высота картинки
            float zoomed_width = width * zoom_rate;     //Зазуменная ширина
            float zoomed_height = height * zoom_rate;   //Зазуменная высота
            int x_offset = (int) (width - width/2 - zoomed_width/2);    //Сдвиг по оси X
            int y_offset = (int) (height - height/2 - zoomed_height/2); //Сдвиг по оси Y

            return Bitmap.createBitmap(bitmapImage.getBitmap(),
                    x_offset,y_offset,
                    (int) zoomed_width,(int) zoomed_height);
        }
        else throw new NullPointerException();
    }

    public static Integer notAuthorizedPageContent(Activity activity, View fragmentView,
                                                Integer constraintLayoutId, Context context,
                                                Resources resources){
        ConstraintLayout constraintFragmentLayout = fragmentView.findViewById(constraintLayoutId);


        LinearLayout generalLayout = new LinearLayout(context);
        generalLayout.setId(View.generateViewId());
        generalLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams generalLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        generalLayout.setLayoutParams(generalLayoutParams);
        generalLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        //Установим layout_constraint
        constraintFragmentLayout.addView(generalLayout);
        ConstraintSet naLayoutConstraint = new ConstraintSet();
        naLayoutConstraint.clone(constraintFragmentLayout);
        Toodles.setConstraints(
                naLayoutConstraint,
                generalLayout.getId(),
                constraintFragmentLayout.getId());
        naLayoutConstraint.applyTo(constraintFragmentLayout);


        TextView textView = new TextView(context);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textViewParams.bottomMargin = 30;
        textView.setLayoutParams(textViewParams);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        //textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setText("To view this page you need to log in");


        Button button = new Button(context);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        button.setLayoutParams(buttonParams);
        button.setGravity(Gravity.CENTER);
        button.setBackground(ResourcesCompat.getDrawable(resources,
                R.drawable.rounded_corners, null));
        button.setTextColor(Color.WHITE);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        button.setText("Log in");
        button.setOnClickListener(v -> setTransition_Login(activity, context));


        generalLayout.addView(textView);
        generalLayout.addView(button);

        return generalLayout.getId();
    }

    private static void setTransition_Login(Activity activity, Context context){
        //Toast.makeText(context,"setTransition_Login", Toast.LENGTH_SHORT).show();
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }
}
