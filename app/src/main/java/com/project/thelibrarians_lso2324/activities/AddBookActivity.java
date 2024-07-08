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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.project.thelibrarians_lso2324.Domain.CardBookPropertyDomain;
import com.project.thelibrarians_lso2324.R;

import java.io.File;



public class AddBookActivity extends AppCompatActivity {
    //Permission constants
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Uri selectedImageUri;

    //UI elements
    private ImageView CopertinaFoto;
    private ImageButton AggiungiFoto;
    EditText Author,Title,ISBN,Category,Description,TotalCopies;
    private Button ConfirmAddition;
    private CardBookPropertyDomain object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addbookpage);
        //VIEWS
        CopertinaFoto = findViewById(R.id.imageViewCopertinaFoto);
        AggiungiFoto = findViewById(R.id.ButtonAggiungiCopertina);
        //Button click add image
        AggiungiFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                pickImageActivityResultLauncher.launch(intent);

            }

        });

        // Navigation logic to home
        ImageButton HomeButton = findViewById(R.id.SecondHome_button);
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBookActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        initView();
        AddBook();


    }

    private void AddBook() {
        ConfirmAddition = findViewById(R.id.ConfirmAddtion);
        ConfirmAddition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Title.getText().toString().isEmpty() || Author.getText().toString().isEmpty() || ISBN.getText().toString().isEmpty() || Category.getText().toString().isEmpty() || Description.getText().toString().isEmpty() || TotalCopies.getText().toString().isEmpty() || selectedImageUri == null) {
                    Toast.makeText(AddBookActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    object = new CardBookPropertyDomain(Title.getText().toString(), Author.getText().toString(), ISBN.getText().toString(), Category.getText().toString(), selectedImageUri.toString(), Description.getText().toString(), Integer.parseInt(TotalCopies.getText().toString()), Integer.parseInt(TotalCopies.getText().toString()), 0);
                    Intent intent = new Intent(AddBookActivity.this, HomePageActivity.class);
                    intent.putExtra("object", object);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    //Initialize the views
    private void initView() {
        Title =  (EditText) findViewById(R.id.TitleAddBook);
        Author = (EditText) findViewById(R.id.AutoreLibro_EditText);
        ISBN = (EditText) findViewById(R.id.ISBN_EditText);
        Category = (EditText) findViewById(R.id.GenereLibro_EditText);
        Description = (EditText) findViewById(R.id.DescrizioneLibro_EditText);
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
                                 selectedImageUri = data.getData();
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