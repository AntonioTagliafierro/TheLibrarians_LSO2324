package com.project.thelibrarians_lso2324.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.events.AuthenticationErrorEvent;
import com.project.thelibrarians_lso2324.exceptions.TokenPayloadExceptions;
import com.project.thelibrarians_lso2324.utils.LoginManager;
import com.project.thelibrarians_lso2324.utils.Utils;
import com.project.thelibrarians_lso2324.daos.UserDao;
import com.project.thelibrarians_lso2324.daos.TokenPayload;
import com.project.thelibrarians_lso2324.events.LoginEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class LoginActivity extends AppCompatActivity {

    List<EditText> loginFields = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView registerNow = findViewById(R.id.register_now);

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LibraryActivity.class);
                startActivity(intent);
            }
        });
        // In case there is a token in cache try to login
        if (Utils.getTokenFromCache(this).isPresent()) {
            String token = Utils.getTokenFromCache(this).get();
            try {
                TokenPayload tokenPayload = new TokenPayload(token);
                LoginManager.getInstance().setTokenPayload(tokenPayload);

                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            } catch (NoSuchElementException | TokenPayloadExceptions e) {
                Utils.showAlert(this, "Error", "Invalid token");
            }
        }

        Button btnLogin = findViewById(R.id.login_button);

        // Login Fields
        loginFields.add(findViewById(R.id.email_login_edittext));
        loginFields.add(findViewById(R.id.password_login_edittext));

        loginFields.get(1).setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                btnLogin.performClick();
                return true;
            }
            return false;
        });


        btnLogin.setOnClickListener(v -> {

            long emptyFieldsLogin = Utils.getAllFieldsNotEmpty(loginFields);
            UserDao userDao = new UserDao();

            // LOGIN ROUTE
            if (emptyFieldsLogin == 0) {
                String email = loginFields.get(0).getText().toString();
                String password = loginFields.get(1).getText().toString();

                if (!Utils.isEmailValid(email)) {
                    Utils.showAlert(this, "Invalid email");
                    return;
                }

                userDao.login(
                        loginFields.get(0).getText().toString(),
                        loginFields.get(1).getText().toString(),
                        this);
            }

            if (emptyFieldsLogin > 0) {
                Utils.showAlert(this, "Empty fields");
            }
        });

    }

    private void clearFields(List<EditText> fields) {

        for (EditText field : fields) {
            field.setText("");
        }
    }

    @Subscribe
    public void onMessageEvent(LoginEvent event) {
        LoginManager.getInstance().setTokenPayload(event.tokenPayload);

        Utils.addTokenToCache(
                event.tokenPayload.rawToken,
                this
        );

        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Subscribe
    public void onMessageEvent(AuthenticationErrorEvent event) {
        Utils.showAlert(this, "Error", event.getMessage());
        clearFields(loginFields);
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
