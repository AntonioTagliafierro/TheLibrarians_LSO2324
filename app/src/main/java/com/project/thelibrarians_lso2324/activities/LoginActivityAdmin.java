package com.project.thelibrarians_lso2324.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.project.thelibrarians_lso2324.R;


public class LoginActivityAdmin extends AppCompatActivity {
EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpageadmin);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Button loginButton = findViewById(R.id.login_button);
        email = findViewById(R.id.email_login_edittext);
        password = findViewById(R.id.password_login_edittext);
        //Views and hide password on touch
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
public boolean onTouch (View v, MotionEvent event){
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (password.getTransformationMethod() == null) {
                            password.setTransformationMethod(new PasswordTransformationMethod());
                            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.password_icon, 0);
                        } else {
                            password.setTransformationMethod(null);
                            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.password_icon, 0);
                        }
                        return true;
                    }
                }
                return false;
            }
        });





//Navigate to HomePageActivity

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(LoginActivityAdmin.this, HomePageActivity.class);
                //Control of email and correct format with a required of domain
                if(email.getText().toString().isEmpty()){
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                } else if(!email.getText().toString().contains("@") || !email.getText().toString().contains("hotmail.com") && !email.getText().toString().contains("gmail.com")){
                    email.setError("Email is not valid");
                    email.requestFocus();
                    return;
                }else if(password.getText().toString().isEmpty()){     //Control of password and correct format with a required of maiusc,minusc and number
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                } else if(password.getText().toString().length() < 8){
                    password.setError("Password must be at least 6 characters");
                    password.requestFocus();
                    return;
                } else if(!password.getText().toString().matches(".*\\d.*")){
                    password.setError("Password must contain at least one number");
                    password.requestFocus();
                    return;
                } else if(!password.getText().toString().matches(".*[A-Z].*")){
                    password.setError("Password must contain at least one uppercase letter");
                    password.requestFocus();
                    return;
                } else if(!password.getText().toString().matches(".*[a-z].*")){
                    password.setError("Password must contain at least one lowercase letter");
                    password.requestFocus();
                    return;
                }else
                {

                    startActivity(intent);
                    finish();

                }




       }

            });
    }
}
