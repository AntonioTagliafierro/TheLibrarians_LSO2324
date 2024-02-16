package com.project.thelibrarians_lso2324.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.thelibrarians_lso2324.R;

public class ProfileActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageButton backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    });

        TextView modificaPasswordTextField = findViewById(R.id.modificaPasswordTextField);
        modificaPasswordTextField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        setupMenuButtons();
    }
}