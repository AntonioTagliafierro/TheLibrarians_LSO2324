package com.project.thelibrarians_lso2324.activities;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.project.thelibrarians_lso2324.Domain.CardBookPropertyDomain;
import com.project.thelibrarians_lso2324.R;

import java.io.File;



public class EditBook extends AppCompatActivity {
    //Permission constants
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    //UI elements
    private ImageView CopertinaFoto;
    private ImageButton AggiungiFoto;
    EditText Author,Title,ISBN,Category,TotalCopies;
    private Button ConfirmEdits;

    //Object for passing data
    private CardBookPropertyDomain object;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        //VIEWS
        CopertinaFoto = findViewById(R.id.imageViewCopertinaFoto);
        AggiungiFoto = findViewById(R.id.ButtonAggiungiCopertina);
        ConfirmEdits = findViewById(R.id.ConfirmEdits);


        //Button click
        AggiungiFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                pickImageActivityResultLauncher.launch(intent);

            }

        });

        //Navigate to CardBook
        ImageButton BackCardBookButton = findViewById(R.id.imageButtonBackCardBook);
        BackCardBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditBook.this, InformationBook.class);
                startActivity(intent);
                finish();

            }
        });


        //Information of the book
        initView();
        getBundle();

        //Modify the book
        ModifyTheBook();

    }

    private void ModifyTheBook() {
        ConfirmEdits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setTitle(Title.getText().toString());
                object.setAuthor(Author.getText().toString());
                object.setCategory(Category.getText().toString());
                object.setISBN(ISBN.getText().toString());
                object.setTotalCopies(Integer.parseInt(TotalCopies.getText().toString()));
                Intent intent = new Intent(EditBook.this, InformationBook.class);
                intent.putExtra("object", object);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getBundle() {
        object = (CardBookPropertyDomain) getIntent().getSerializableExtra("object");
        int drawableResourceId = this.getResources().getIdentifier(object.getImageUrl(), "drawable", this.getPackageName());
        Glide.with(this).load(drawableResourceId).into(CopertinaFoto);

        Title.setText(object.getTitle());
        Author.setText(object.getAuthor());
        Category.setText(object.getCategory());
        TotalCopies.setText(String.valueOf(object.getTotalCopies()));
        ISBN.setText(object.getISBN());
    }
    private void initView() {
        Title =  (EditText) findViewById(R.id.Text_PlainText);
        Author = (EditText) findViewById(R.id.AutoreLibro_EditText);
        ISBN = (EditText) findViewById(R.id.ISBN_EditText);
        Category = (EditText) findViewById(R.id.GenereLibro_EditText);
        TotalCopies = (EditText) findViewById(R.id.CopieTotali_EditText);
    }




    // step 1 get path from uri to select photo
    public String getPathFromUri(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();


        return res;
    }

    //step 2 new way to handle onActivityResult in Android 13
    ActivityResultLauncher<Intent> pickImageActivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK) {
                                Intent data = result.getData();
                                Uri selectedImageUri = data.getData();
                                final String path = getPathFromUri(selectedImageUri);
                                if (path != null) {
                                    File file = new File(path);
                                    selectedImageUri = Uri.fromFile(file);
                                }
                                CopertinaFoto.setImageURI(selectedImageUri);
                            }


                        }
                    });


}