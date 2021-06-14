package com.example.coffees.Authorization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffees.GlobalClasses.ControllerAPI;
import com.example.coffees.GlobalClasses.Toodles;
import com.example.coffees.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText field_loginUsername;
    private EditText field_loginPassword;
    private TextView text_incorrectLoginData;


    private boolean fitUsername = false;
    private boolean fitPassword = false;
    private String regex = "^[a-zA-Z0-9]+$"; //Только буквы и цифры

    private ProgressBar loginActivityProgressBar;

    private boolean isConnected = false;
    private boolean isLoggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar custom_toolbar = findViewById(R.id.custom_toolbar_id);
        custom_toolbar.setBackgroundColor(Color.parseColor("#1f3933"));
        custom_toolbar.setTitle("Coffees");
        custom_toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_arrow_left, null));
        custom_toolbar.setNavigationOnClickListener(v -> onBackPressed());


        loginActivityProgressBar = findViewById(R.id.loginActivityProgressBar);
        loginActivityProgressBar.setVisibility(View.GONE);


        LinearLayout signupLayout = findViewById(R.id.signupLayout);
        signupLayout.setOnClickListener(v -> setTransition_RegistrationActivity());


        field_loginUsername = findViewById(R.id.field_loginUsername);
        validationUsername();
        field_loginPassword = findViewById(R.id.field_loginPassword);
        validationPassword();

        text_incorrectLoginData = findViewById(R.id.text_incorrectLoginData);

        Button btn_login = findViewById(R.id.btn_logIn);
        btn_login.setOnClickListener(v -> {
            if (fitUsername && fitPassword){
                String username = field_loginUsername.getText().toString();
                String password = field_loginPassword.getText().toString();
                /*
                Отправить запрос на авторизацию
                 */
                loginActivityProgressBar.setVisibility(View.VISIBLE);
                thread_login(username, password);
            }
            else {
                if (!fitUsername)
                    setErrorUsername();
                if (!fitPassword)
                    setErrorPassword();
            }
        });


    }

    private void thread_login(String username, String password){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}&{3}={4}",
                    "http://web-schedule.zapto.org:5000/Api/User/GetUserByLogin",
                    "username", username,
                    "password", password);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionGet(url);
                    if (connection != null
                            && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        //Если 200 ОК
                        /*
                        Отправить запрос на получение необходимых данных пользователя
                         */
                        isLoggedIn = controllerAPI.loginActivity(connection);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    loginActivityProgressBar.setVisibility(View.GONE);
                    if (isConnected){
                        try{
                            if (isLoggedIn){
                                Toast.makeText(getApplicationContext(),
                                        "Successfully logged in",Toast.LENGTH_SHORT).show();
                                setTransition_closeLoginActivity();
                            }
                            else if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST){
                                field_loginUsername.setError("Possibly wrong username");
                                field_loginPassword.setError("Possibly wrong password");
                                text_incorrectLoginData.setVisibility(View.VISIBLE);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),
                                        "Failed to login, possibly incorrect parsing of input data",
                                        Toast.LENGTH_SHORT).show();
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

    private void validationPassword(){
        field_loginPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                String password = field_loginPassword.getText().toString();
                Pattern pattern = Pattern.compile(regex);

                if (password.length() < 6 || password.length() > 15
                        || !pattern.matcher(password).matches()){
                    setErrorPassword();
                    fitPassword = false;
                }
                else
                    fitPassword = true;
            }
            else {
                if (text_incorrectLoginData.getVisibility() == View.VISIBLE)
                    text_incorrectLoginData.setVisibility(View.GONE);
            }
        });
    }

    private void validationUsername(){
        field_loginUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                String username = field_loginUsername.getText().toString();
                Pattern pattern = Pattern.compile(regex);

                if (username.length() < 3 || username.length() > 15
                        || !pattern.matcher(username).matches()){
                    setErrorUsername();
                    fitUsername = false;
                }
                else
                    fitUsername = true;
            }
            else {
                if (text_incorrectLoginData.getVisibility() == View.VISIBLE)
                    text_incorrectLoginData.setVisibility(View.GONE);
            }
        });
    }

    private void setErrorPassword(){
        field_loginPassword.setError("Password must be between 6 and 15 characters, " +
                "and can only contain letters and numbers");
    }

    private void setErrorUsername(){
        field_loginUsername.setError("Username must be between 3 and 15 characters, " +
                "and can only contain letters and numbers");
    }

    private void setTransition_RegistrationActivity(){
        startActivity(new Intent(this, RegistrationActivity.class));
        finish();
    }

    private void setTransition_closeLoginActivity(){
        finish();
    }

    //Свернуть клавиатуру по нажатии на пустое место экрана
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) { //Нажатие пальцем по экрану
            View v = getCurrentFocus(); //Получить текущий фокус
            if ( v instanceof EditText) {   //Установлен ли фокус в поле EditText
                Rect outRectUsername = new Rect();  //Создание экзампляра класса прямоугольника
                field_loginUsername.getGlobalVisibleRect(outRectUsername);    //Получить координаты углов прямоугольника поля EditText
                Rect outRectPassword = new Rect();  //Создание экзампляра класса прямоугольника
                field_loginPassword.getGlobalVisibleRect(outRectPassword);

                int inputX = (int) event.getRawX();
                int inputY = (int) event.getRawY();
                if (!outRectUsername.contains(inputX, inputY)
                        && !outRectPassword.contains(inputX, inputY)) {    //Если коориднаты места нажатия не находятся в координатах прямоугольников EditText, то
                    v.clearFocus(); //Очисть фокус с поля EditText
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //Т.к. фокус потерян, то убрать клавиатуру
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}