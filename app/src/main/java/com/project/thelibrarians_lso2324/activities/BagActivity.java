package com.project.thelibrarians_lso2324.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.events.BagUpdateEvent;
import com.project.thelibrarians_lso2324.model.Book;
import com.project.thelibrarians_lso2324.viewmodel.BagViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BagActivity extends AppCompatActivity {

    BagViewModel model;
    RelativeLayout layout;
    private static final int K = 5; // Sostituisci con il valore desiderato di k

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);

        model = new ViewModelProvider(this).get(BagViewModel.class);
        // Tasto Torna Indietro
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BagActivity.this, HomeActivity.class);
                startActivity(intent);;  // Chiude l'activity e torna indietro
            }
        });

        // Contenitore per gli elementi nel carrello


        List<Book> books = new ArrayList<>(Objects.requireNonNull(model.getBooks().getValue()).keySet());
        RecyclerView recyclerView = findViewById(R.id.bagContainer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        // Tasto Checkout
        Button checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (K > 0) {
                    model.sendOrder(BagActivity.this);
                    EventBus.getDefault().post(new BagUpdateEvent(0));
                    model.clearCart();
                }
            }
        });
    }
}
