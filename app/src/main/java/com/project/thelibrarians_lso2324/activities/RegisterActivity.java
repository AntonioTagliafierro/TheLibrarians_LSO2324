package com.project.thelibrarians_lso2324.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.events.AuthenticationErrorEvent;
import com.project.thelibrarians_lso2324.events.LoginEvent;
import com.project.thelibrarians_lso2324.utils.LoginManager;
import com.project.thelibrarians_lso2324.utils.Utils;
import com.project.thelibrarians_lso2324.daos.UserDao;
import com.project.thelibrarians_lso2324.daos.TokenPayload;
import com.project.thelibrarians_lso2324.events.RegisterEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class RegisterActivity extends AppCompatActivity {

    List<EditText> registerFields = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnRegister = findViewById(R.id.register_button);

        // Register Fields
        registerFields.add(findViewById(R.id.name_register_edittext));
        registerFields.add(findViewById(R.id.surname_register_edittext));
        registerFields.add(findViewById(R.id.email_register_edittext));
        registerFields.add(findViewById(R.id.password_register_edittext));
        registerFields.add(findViewById(R.id.confirm_email_register_edittext));
        registerFields.add(findViewById(R.id.confirm_password_register_edittext));

        btnRegister.setOnClickListener(v -> {

            long emptyFieldsRegister = Utils.getAllFieldsNotEmpty(registerFields);
            UserDao userDao = new UserDao();

            // REGISTER ROUTE
            if ( emptyFieldsRegister == 0) {
                if (!registerFields.get(3).getText().toString().equals(registerFields.get(5).getText().toString())) {
                    Utils.showAlert(this, "Passwords do not match");
                    return;
                }

                if (!Utils.isEmailValid(String.valueOf(registerFields.get(2).getText()))) {
                    Utils.showAlert(this, "Invalid email");
                    return;
                }
                if (!registerFields.get(2).getText().toString().equals(registerFields.get(4).getText().toString())) {
                    Utils.showAlert(this, "Passwords do not match");
                    return;
                }

                userDao.register(
                        registerFields.get(0).getText().toString(),
                        registerFields.get(1).getText().toString(),
                        registerFields.get(2).getText().toString(),
                        registerFields.get(3).getText().toString(),
                        this
                );
            }
            if ( emptyFieldsRegister > 0) {
                Utils.showAlert(this, "Empty fields");
            }
        });
    }
        private void clearFields(List<EditText> fields) {

            for (EditText field : fields) {
                field.setText("");
            }
        }

        /**@Subscribe
        public void onMessageEvent(LoginEvent event) {
            LoginManager.getInstance().setTokenPayload(event.tokenPayload);

            Utils.addTokenToCache(
                    event.tokenPayload.rawToken,
                    this
            );

            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/

        @Subscribe
        public void onMessageEvent(RegisterEvent event) {
            Utils.showAlert(this, "Successfully registered");
            clearFields(registerFields);
        }

        @Subscribe
        public void onMessageEvent(AuthenticationErrorEvent event) {
            Utils.showAlert(this, "Error", event.getMessage());
            clearFields(registerFields);
        }

        @Override
        protected void onStart() {
            super.onStart();
            EventBus.getDefault().register(this);
        }

        @Override
        protected void onStop() {
            super.onStop();
            EventBus.getDefault().unregister(this);
        }
}