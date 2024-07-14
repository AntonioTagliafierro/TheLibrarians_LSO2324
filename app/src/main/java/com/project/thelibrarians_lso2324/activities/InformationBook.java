package com.project.thelibrarians_lso2324.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.project.thelibrarians_lso2324.Domain.CardBookPropertyDomain;
import com.project.thelibrarians_lso2324.R;


public class InformationBook extends AppCompatActivity {

    private TextView textViewTitle, textViewAuthor,textViewCategory,textViewTotalCopies,
    textViewAvailableCopies,textViewCopiesInUse,textViewISBN;
    private ImageView ImageViewCopertinaLibro;
    private CardBookPropertyDomain object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_book);
        Button EditBookButton = findViewById(R.id.buttonEdit);


        //Navigate to EditBook
        EditBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformationBook.this, EditBook.class);
                intent.putExtra("object", object);
                startActivity(intent);
                finish();
            }
        });


        //Navigate to Homepage
        ImageButton BackHomepageButton = findViewById(R.id.imageButtonBack);
        BackHomepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformationBook.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Information of the book
        initView();
        getBundle();
        }
        private void getBundle() {
        object = (CardBookPropertyDomain) getIntent().getSerializableExtra("object");
        int drawableResourceId = this.getResources().getIdentifier(object.getImageUrl(), "drawable", this.getPackageName());
            Glide.with(this).load(drawableResourceId).into(ImageViewCopertinaLibro);

            textViewTitle.setText(object.getTitle());
            textViewAuthor.setText(object.getAuthor());
            textViewCategory.setText(object.getCategory());
            textViewTotalCopies.setText(String.valueOf(object.getTotalCopies()));
            textViewAvailableCopies.setText(String.valueOf(object.getAvailableCopies()));
            textViewCopiesInUse.setText(String.valueOf(object.getCopiesInUse()));
        }

    private void initView() {
        textViewTitle = findViewById(R.id.textViewTitolo);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        textViewCategory = findViewById(R.id.textViewCategoryInformation);
        textViewISBN = findViewById(R.id.textViewISBN);
        textViewTotalCopies = findViewById(R.id.textViewTotalCopies);
        textViewAvailableCopies = findViewById(R.id.textViewAvailableCopies);
        textViewCopiesInUse = findViewById(R.id.textViewCopiesInUse);
        ImageViewCopertinaLibro = findViewById(R.id.imageViewCopertinaLibro);



    }


}
