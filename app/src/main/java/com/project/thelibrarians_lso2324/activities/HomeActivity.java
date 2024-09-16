package com.project.thelibrarians_lso2324.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.project.thelibrarians_lso2324.R;
import com.project.thelibrarians_lso2324.model.Book;
import com.project.thelibrarians_lso2324.model.LendLease;
import com.project.thelibrarians_lso2324.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends NavigationActivity {

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupMenuButtons();

        // Inizializza la RequestQueue di Volley
        requestQueue = Volley.newRequestQueue(this);

        // Chiama la funzione per controllare i prestiti in scadenza
        checkExpiringLoans();

    }

    // Funzione che controlla se ci sono libri in scadenza
    private void checkExpiringLoans() {
        String url =  Utils.API_BASE_URL;  // URL del server in C

        // Richiesta Volley per recuperare i dati dei prestiti
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Converti la risposta JSON in una lista di prestiti
                        List<LendLease> loans = parseLoanData(response);
                        // Controlla le date di scadenza
                        checkDueDates(loans);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gestisci gli errori qui
                        error.printStackTrace();
                    }
                }
        );

        // Aggiungi la richiesta alla coda di Volley
        requestQueue.add(jsonArrayRequest);
    }

    // Funzione per convertire il JSON array in una lista di oggetti LendLease
    private List<LendLease> parseLoanData(JSONArray response) {
        List<LendLease> loans = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // Adatta il formato alla tua data

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject loanObject = response.getJSONObject(i);

                // Parsing dei campi dal JSON
                String dueDateString = loanObject.getString("dueDate");
                Date dueDate = sdf.parse(dueDateString);

                // Parsing della lista di libri (presupponendo che ci sia un array di libri nel JSON)
                JSONArray booksArray = loanObject.getJSONArray("books");
                List<Book> books = new ArrayList<>();

                for (int j = 0; j < booksArray.length(); j++) {
                    JSONObject bookObject = booksArray.getJSONObject(j);
                    String bookTitle = bookObject.getString("title");  // Supponendo che ci sia un campo "title"
                    books.add(new Book(bookTitle));
                }

                // Creazione dell'oggetto LendLease
                LendLease loan = new LendLease();
                loan.setDueDate(dueDate);
                loan.setBooks(books);  // Assicurati che LendLease abbia il setter per i libri
                loans.add(loan);
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        return loans;
    }

    // Funzione per controllare le scadenze e mostrare Toast
    private void checkDueDates(List<LendLease> loans) {
        Date currentDate = new Date();  // Data corrente

        for (LendLease loan : loans) {
            if (loan.getDueDate() != null && loan.getDueDate().before(currentDate)) {
                // Se la data di restituzione è passata, prendi i titoli dei libri
                for (Book book : loan.getBooks()) {
                    String message = "Il libro \"" + book.getTitle() + "\" è scaduto!";
                    Toast.makeText(HomeActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}