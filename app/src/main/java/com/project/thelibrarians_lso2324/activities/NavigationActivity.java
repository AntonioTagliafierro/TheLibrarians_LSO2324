package com.project.thelibrarians_lso2324.activities;

import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.utils.NavigationManager;

public class NavigationActivity extends AppCompatActivity {
    protected void setupMenuButtons() {
        ImageButton homeButton = findViewById(R.id.ImgBtnHome);
        ImageButton libraryButton = findViewById(R.id.imgBtnLibrary);
        ImageButton profileButton = findViewById(R.id.imgBtnProfile);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToHome(NavigationActivity.this);
            }
        });

        libraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToLibrary(NavigationActivity.this);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationManager.navigateToProfile(NavigationActivity.this);
            }
        });
    }
}
