package com.project.thelibrarians_lso2324.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import com.project.thelibrarians_lso2324.model.User;
import com.project.thelibrarians_lso2324.exceptions.TokenPayloadExceptions;
import com.project.thelibrarians_lso2324.daos.TokenPayload;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public final static String API_BASE_URL = "TOINSERT";


    public static boolean isEmailValid(String email) {
        // Definiamo un pattern per verificare l'indirizzo email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        // Verifichiamo se il pattern corrisponde all'indirizzo email
        return matcher.matches();
    }

    //Check if all the fields are not empty

    public static long getAllFieldsNotEmpty(Collection<? extends EditText> fields) {
        return fields.stream().filter(field -> field.getText().toString().isEmpty()).count();
    }

    //Show an alert with a custom message

    public static void showAlert(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Attenzione");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }


    //Show an alert with a custom title and message

    public static void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    public static User extractUserFromTokenPayload(TokenPayload tokenPayload) {
        return new User(tokenPayload.id, tokenPayload.email);
    }

    public static Pair<String, String> separateEmail(String email) {
        String[] splitted = email.split("@");
        return new Pair<>(splitted[0], splitted[1]);
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String addCurrencySymbol(String str) {
        return str + "€";
    }

    public static void addTokenToCache(String token, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("token", token);
        editor.apply();
    }

    private static void deleteTokenFromCache(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.remove("token");
        editor.apply();
    }

    public static Optional<String> getTokenFromCache(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);

        if (token == null) {
            System.out.println("Token not found in cache");
            return Optional.empty();
        }

        try {
            TokenPayload tokenPayload = new TokenPayload(token);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime expire = LocalDateTime.parse(tokenPayload.expire, formatter);
                LocalDateTime now = LocalDateTime.now();

                if (now.isAfter(expire)) {
                    System.out.println("Token expired");
                    deleteTokenFromCache(context);
                    return Optional.empty();
                }
            }


        } catch (TokenPayloadExceptions e) {
            System.out.println("Token not valid");
            deleteTokenFromCache(context);
            return Optional.empty();
        }

        return Optional.of(token);
    }
}
