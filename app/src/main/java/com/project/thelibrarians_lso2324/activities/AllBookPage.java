package com.project.thelibrarians_lso2324.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.project.thelibrarians_lso2324.Domain.CardBookPropertyDomain;
import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.adapters.AllBookAdapter;

import java.util.ArrayList;



public class AllBookPage extends AppCompatActivity {
private ImageButton BackHome;

    private RecyclerView.Adapter adapterRecommended;
    private  RecyclerView recyclerAllBook;
    private ImageButton FilterButton;
    private CardBookPropertyDomain object;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_book_page);
        //Navigate to homepage
        BackHome = findViewById(R.id.imageButtonBackAllBook);
        BackHome.setOnClickListener(v -> {
            Intent intent = new Intent(AllBookPage.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        });

        //Add element in the recycler view
        initRecyclerview();

        //Views filter menu
        FilterButton = findViewById(R.id.imageButtonFilterButton);
        FilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }

            private void showDialog() {
                final Dialog dialog = new Dialog(AllBookPage.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_filter_menu);
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                Button AddFilterButton = dialog.findViewById(R.id.AddFilterButton);
                AddFilterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });

    }
    private void initRecyclerview() {
        ArrayList<CardBookPropertyDomain> itemsAllBooks = new ArrayList<>();;

        itemsAllBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsAllBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));


        recyclerAllBook = findViewById(R.id.recyclerAllBook);
        recyclerAllBook.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapterRecommended = new AllBookAdapter(itemsAllBooks);
        recyclerAllBook.setAdapter(adapterRecommended);
    }



}