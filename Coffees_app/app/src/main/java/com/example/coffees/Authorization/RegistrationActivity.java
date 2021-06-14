package com.example.coffees.Authorization;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffees.GlobalClasses.ControllerAPI;
import com.example.coffees.GlobalClasses.Toodles;
import com.example.coffees.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private ScrollView generalSignup_scrollView;
    private ProgressBar registrationActivityProgressBar;

    private EditText field_signupEmail;
    private EditText field_signupUsername;
    private EditText field_signupPassword;
    private EditText field_signupPasswordConfirm;
    private Button btn_signup;

    private boolean fitEmail = false;
    private boolean fitUsername = false;
    private boolean fitPassword = false;
    private boolean fitPasswordConfirm = false;
    private String regex_UserPass = "^[a-zA-Z0-9]+$"; //Только буквы и цифры для имени пользователя и пароля
    private String regex_email = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9]+$"; //Для email

    private boolean isConnected = false;
    private boolean isLoggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        Toolbar custom_toolbar = findViewById(R.id.custom_toolbar_id);
        custom_toolbar.setBackgroundColor(Color.parseColor("#1f3933"));
        custom_toolbar.setTitle("Coffees");
        custom_toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_arrow_left, null));
        custom_toolbar.setNavigationOnClickListener(v -> setTransition_LoginActivity());

        generalSignup_scrollView = findViewById(R.id.generalSignup_scrollView);
        generalSignup_scrollView.setVisibility(View.VISIBLE);
        registrationActivityProgressBar = findViewById(R.id.registrationActivityProgressBar);
        registrationActivityProgressBar.setVisibility(View.GONE);


        field_signupEmail = findViewById(R.id.field_signupEmail);
        validationEmail();
        field_signupUsername = findViewById(R.id.field_signupUsername);
        validationUsername();
        field_signupPassword = findViewById(R.id.field_signupPassword);
        validationPassword();
        field_signupPasswordConfirm = findViewById(R.id.field_signupPasswordConfirm);
        validationPasswordConfirm();


        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(v -> {
            if (fitEmail && fitUsername && fitPassword && fitPasswordConfirm){
                String email = field_signupEmail.getText().toString();
                String username = field_signupUsername.getText().toString();
                String password = field_signupPassword.getText().toString();
                /*
                Отправить запрос на регистрацию
                 */
                btn_signup.setClickable(false);
                registrationActivityProgressBar.setVisibility(View.VISIBLE);
                thread_registration(email, username, password);

                //Если 200 ОК
                //setTransition_closeRegistrationActivity();
            }
            else {
                if (!fitEmail)
                    setErrorEmail();
                if (!fitUsername)
                    setErrorUsername();
                if (!fitPassword)
                    setErrorPassword();
                if (!fitPasswordConfirm)
                    setErrorPasswordConfirm();
            }
        });
    }

    private void thread_registration(String email, String username, String password){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}&{3}={4}&{5}={6}&{7}={8}",
                    "http://web-schedule.zapto.org:5000/Api/User/AddUser",
                    "email", email,
                    "username", username,
                    "password", password,
                    "card_id", 0);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionPost(url);
                    connection.getResponseCode();

                } catch (IOException e){
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    if (isConnected){
                        try {
                            if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                                //Если 200 ОК
                                /*
                                Отправить запрос на получение необходимых данных пользователя
                                 */
                                Toast.makeText(getApplicationContext(),
                                        "Successfully registered",Toast.LENGTH_SHORT).show();
                                thread_login(username, password);
                            }
                            else if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST){
                                thread_fitEmail(email);
                                thread_fitUsername(username);
                            }
                            else {
                                alert_failed("Registration error");
                            }
                            registrationActivityProgressBar.setVisibility(View.GONE);
                            btn_signup.setClickable(true);
                        } catch (IOException e){
                            e.printStackTrace();
                            alert_failed("Cant get response code");
                            registrationActivityProgressBar.setVisibility(View.GONE);
                            btn_signup.setClickable(true);
                        }
                    }
                    else {
                        alert_failed("No internet connection, please try again later");
                        registrationActivityProgressBar.setVisibility(View.GONE);
                        btn_signup.setClickable(true);
                    }

                });
            }
        }).start();
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
                    registrationActivityProgressBar.setVisibility(View.GONE);
                    btn_signup.setClickable(true);
                    if (isConnected){
                        try{
                            if (isLoggedIn){
                                Toast.makeText(getApplicationContext(),
                                        "Successfully logged in",Toast.LENGTH_SHORT).show();
                                setTransition_closeRegistrationActivity();
                            }
                            else if (connection != null
                                    && connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST){
                                alert_failed("impossible login, need fix RegistrationActivity:thread_login");
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

    private void setErrorEmail(){
        field_signupEmail.setError("Email must only contain:\n" +
                "Recipient name\n" +
                "@ symbol\n" +
                "Domain name\n" +
                "Top-level domain");
    }

    private void setErrorUsername(){
        field_signupUsername.setError("Username must be between 3 and 15 characters, " +
                "and can only contain letters and numbers");
    }

    private void setErrorPassword(){
        field_signupPassword.setError("Password must be between 6 and 15 characters, " +
                "and can only contain letters and numbers");
    }

    private void setErrorPasswordConfirm(){
        field_signupPasswordConfirm.setError("Password mismatch");
    }

    private void validationEmail(){
        field_signupEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                String email = field_signupEmail.getText().toString();
                Pattern pattern = Pattern.compile(regex_email);

                if (email.length() > 50 || !pattern.matcher(email).matches()){
                    setErrorEmail();
                    fitEmail = false;
                }
                else{
                    thread_fitEmail(email);
                }
            }
        });
    }

    private void thread_fitEmail(String email){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}&{3}={4}",
                    "http://web-schedule.zapto.org:5000/Api/User/GetUserIdByEmail",
                    "userMail", email);

            @Override
            public void run() {
                try {
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionGet(url);
                    if (connection != null
                            && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        fitEmail = false;
                    }
                    else {
                        fitEmail = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    if (isConnected){
                        if (!fitEmail){
                            field_signupEmail.setError("An account with the same email address already exists");
                        }
                    }
                    else{
                        Log.i("Internet connection", "no connection");
                    }
                });
            }
        }).start();
    }

    private void validationUsername(){
        field_signupUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                String username = field_signupUsername.getText().toString();
                Pattern pattern = Pattern.compile(regex_UserPass);

                if (username.length() < 3 || username.length() > 15
                        || !pattern.matcher(username).matches()){
                    setErrorUsername();
                    fitUsername = false;
                }
                else
                    thread_fitUsername(username);
            }
        });
    }

    private void thread_fitUsername(String username){
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            ControllerAPI controllerAPI = new ControllerAPI();
            String url = MessageFormat.format("{0}?{1}={2}&{3}={4}",
                    "http://web-schedule.zapto.org:5000/Api/User/GetUserIdByUsername",
                    "username", username);

            @Override
            public void run() {
                try{
                    isConnected = Toodles.isNetworkAvailable(getApplication());
                    connection = controllerAPI.connectionGet(url);
                    if (connection != null
                            && connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        fitUsername = false;
                    }
                    else {
                        fitUsername = true;
                    }

                } catch (IOException e){
                    e.printStackTrace();
                }

                runOnUiThread(() -> {
                    if (isConnected){
                        if (!fitUsername){
                            field_signupUsername.setError("Username already exists");
                        }
                    }
                    else{
                        Log.i("Internet connection", "no connection");
                    }
                });
            }
        }).start();
    }

    private void validationPassword(){
        field_signupPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                String password = field_signupPassword.getText().toString();
                Pattern pattern = Pattern.compile(regex_UserPass);

                if (password.length() < 6 || password.length() > 15
                        || !pattern.matcher(password).matches()){
                    setErrorPassword();
                    fitPassword = false;
                }
                else
                    fitPassword = true;
            }
        });
    }

    private void validationPasswordConfirm(){
        field_signupPasswordConfirm.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                String passwordConfirm = field_signupPasswordConfirm.getText().toString();
                String password = field_signupPassword.getText().toString();

                if (!passwordConfirm.equals(password)){
                    setErrorPasswordConfirm();
                    fitPasswordConfirm = false;
                }
                else
                    fitPasswordConfirm = true;
            }
        });
    }

    private void setTransition_LoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void setTransition_closeRegistrationActivity(){
        finish();
    }

    //Свернуть клавиатуру по нажатии на пустое место экрана
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) { //Нажатие пальцем по экрану
            View v = getCurrentFocus(); //Получить текущий фокус
            if ( v instanceof EditText) {   //Установлен ли фокус в поле EditText
                Rect outRectEmail = new Rect(); //Создание экзампляра класса прямоугольника
                field_signupEmail.getGlobalVisibleRect(outRectEmail);   //Получить координаты углов прямоугольника поля EditText
                Rect outRectUsername = new Rect();  //Создание экзампляра класса прямоугольника
                field_signupUsername.getGlobalVisibleRect(outRectUsername);    //Получить координаты углов прямоугольника поля EditText
                Rect outRectPassword = new Rect();  //Создание экзампляра класса прямоугольника
                field_signupPassword.getGlobalVisibleRect(outRectPassword); //Получить координаты углов прямоугольника поля EditText
                Rect outRectPasswordConfirm = new Rect();   //Создание экзампляра класса прямоугольника
                field_signupPasswordConfirm.getGlobalVisibleRect(outRectPasswordConfirm);   //Получить координаты углов прямоугольника поля EditText

                int inputX = (int)event.getRawX();
                int inputY = (int)event.getRawY();

                if (!outRectEmail.contains(inputX, inputY)
                        && !outRectUsername.contains(inputX, inputY)
                        && !outRectPassword.contains(inputX, inputY)
                        && !outRectPasswordConfirm.contains(inputX, inputY)) {    //Если коориднаты места нажатия не находятся в координатах прямоугольников EditText, то
                    v.clearFocus(); //Очисть фокус с поля EditText
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //Т.к. фокус потерян, то убрать клавиатуру
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private void alert_failed(String message){
        AlertDialog dialog = new AlertDialog.Builder(getApplicationContext())
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
}