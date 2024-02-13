package com.project.thelibrarians_lso2324.utils;

import android.content.Context;
import android.content.Intent;

import com.project.thelibrarians_lso2324.activities.HomeActivity;
import com.project.thelibrarians_lso2324.activities.LibraryActivity;
import com.project.thelibrarians_lso2324.activities.ProfileActivity;

public class NavigationManager {
    public static void navigateToHome(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToLibrary(Context context) {
        Intent intent = new Intent(context, LibraryActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToProfile(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }
}
