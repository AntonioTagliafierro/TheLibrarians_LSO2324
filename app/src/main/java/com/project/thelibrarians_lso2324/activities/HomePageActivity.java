package com.project.thelibrarians_lso2324.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.project.thelibrarians_lso2324.Domain.CardBookPropertyDomain;
import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.adapters.RecommendedAdapter;

import java.util.ArrayList;



public class HomePageActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterRecommended;
    private  RecyclerView recyclerViewPopular,recyclerViewNewReleases;
    private ImageButton FilterButton;
    private CardBookPropertyDomain object;
    private TextView SeeAll,SeeAll2;
    private ImageButton imageButtonLove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminhomepage);

        //set underline for TextViewSeeAll
        SeeAll = findViewById(R.id.TextViewSeeAll);
        SeeAll.setPaintFlags(SeeAll.getPaintFlags() |   android.graphics.Paint.UNDERLINE_TEXT_FLAG);

        //set underline for TextViewSeeAll2
        SeeAll2 = findViewById(R.id.TextViewSeeAll2);
        SeeAll2.setPaintFlags(SeeAll2.getPaintFlags() |   android.graphics.Paint.UNDERLINE_TEXT_FLAG);


        //Navigate to LoveBook
        imageButtonLove = findViewById(R.id.imageButtonLove);
        imageButtonLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, LoveBook.class);
                startActivity(intent);
                finish();
            }
        });

        //Navigate to SeeAllBooks
        SeeAll = findViewById(R.id.TextViewSeeAll);
        SeeAll2 = findViewById(R.id.TextViewSeeAll2);
        SeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, AllBookPage.class);
                startActivity(intent);
                finish();
            }
        });
        SeeAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, AllBookPage.class);
                startActivity(intent);
                finish();
            }
        });



        //Add element in the recycler view
        initRecyclerview();

        //Navigate to AddBook
        ImageButton AddBookButton = findViewById(R.id.AddBook_button);
        AddBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, AddBookActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Views filter menu
        FilterButton = findViewById(R.id.imageButtonFilter);
        FilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }

            private void showDialog() {
                final Dialog dialog = new Dialog(HomePageActivity.this);
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
        ArrayList<CardBookPropertyDomain> itemsPopularBooks = new ArrayList<>();
        ArrayList<CardBookPropertyDomain> itemsNewReleases = new ArrayList<>();

        itemsPopularBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsPopularBooks.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));


        recyclerViewPopular= findViewById(R.id.recyclerViewMostPopularBooks);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapterRecommended = new RecommendedAdapter(itemsPopularBooks);
        recyclerViewPopular.setAdapter(adapterRecommended);


        object = (CardBookPropertyDomain) getIntent().getSerializableExtra("object");
        if (object!= null)  {
            itemsNewReleases.add(new CardBookPropertyDomain(object.getTitle(),object.getAuthor(),object.getISBN(),object.getCategory(),"pic1",object.getTotalCopies(),object.getAvailableCopies(),object.getCopiesInUse()));
        }

        itemsNewReleases.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsNewReleases.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsNewReleases.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsNewReleases.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsNewReleases.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsNewReleases.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsNewReleases.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsNewReleases.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsNewReleases.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));
        itemsNewReleases.add(new CardBookPropertyDomain("Il bosco di nebbia","Gabriella Genisi","AQ23457789087","Thriller","pic1",5,3,2));
        itemsNewReleases.add(new CardBookPropertyDomain("La principessa Angina","Roland Tapor","9788899729554","Historical novel","pic2",5,3,2));
        itemsNewReleases.add(new CardBookPropertyDomain("The power of balance ","William R.Torbert","080394067X","Economic management","pic3",5,3,2));

        recyclerViewNewReleases= findViewById(R.id.recyclerViewNewReleasesBooks);
        recyclerViewNewReleases.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapterRecommended = new RecommendedAdapter(itemsNewReleases);
        recyclerViewNewReleases.setAdapter(adapterRecommended);
    }
}
