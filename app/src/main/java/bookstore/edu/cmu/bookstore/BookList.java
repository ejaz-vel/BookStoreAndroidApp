package bookstore.edu.cmu.bookstore;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rest.client.Book;
import rest.client.BookClient;
import rest.client.UserClient;

public class BookList extends AppCompatActivity {

    ProgressBar pb;
    ListView listView;
    FloatingActionButton fab;
    List<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);
        fab = (FloatingActionButton) findViewById(R.id.gmap);
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);

        BookListTask task = new BookListTask();
        task.execute("Harry Potter");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BookList.this, BookDetail.class);
                Book book = books.get(position);
                intent.putExtra("BOOK", book);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookList.this, Map.class);
                intent.putExtra("BOOKS", (Serializable) books);
                startActivity(intent);
            }
        });
    }

    private List<Book> getBookList() {
        List<Book> books = new ArrayList<>();

        Book b1 = new Book();
        b1.setName("Harry Potter");
        b1.setPrice(24.5);
        b1.setDescription("One year old. Good Condition");
        b1.setLatitude(40.443393);
        b1.setLongitude(-79.931734);

        Book b2 = new Book();
        b2.setName("Da Vinci Code");
        b2.setPrice(20.0);
        b2.setDescription("One of the best thrillers out there. Used only for 1 month");
        b2.setLatitude(40.443934);
        b2.setLongitude(-79.933833);

        Book b3 = new Book();
        b3.setName("Android Dev");
        b3.setPrice(10.0);
        b3.setDescription("A must have book for every Android developer");
        b3.setLatitude(40.445376);
        b3.setLongitude(-79.936508);

        Book b4 = new Book();
        b4.setName("Spark");
        b4.setPrice(30.0);
        b4.setDescription("One of the best guides out there for Spark. Selling it for CHEAP!!");
        b4.setLatitude(40.443756);
        b4.setLongitude(-79.935176);

        Book b5 = new Book();
        b5.setName("Hadoop Map Reduce");
        b5.setPrice(5.0);
        b5.setDescription("Brand New");
        b5.setLatitude(40.446893);
        b5.setLongitude(-79.933984);

        books.add(b1);
        books.add(b2);
        books.add(b3);
        books.add(b4);
        books.add(b5);
        return books;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class BookListTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //BookClient bc = new BookClient();
            UserClient uc = new UserClient("52.27.184.29:80");
            //books = getBookList();
            try {
                int coarseLocPermissionCheck = ContextCompat.checkSelfPermission(BookList.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);
                int fineLocPermissionCheck = ContextCompat.checkSelfPermission(BookList.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (coarseLocPermissionCheck == PackageManager.PERMISSION_GRANTED &&
                        fineLocPermissionCheck == PackageManager.PERMISSION_GRANTED) {
                    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    books = uc.recommendBooks(1, location.getLatitude(), location.getLongitude());
                } else {
                    books = uc.recommendBooks(1, null, null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccessfull) {
            if (isSuccessfull) {
                BookListAdapter bookAdapter = new BookListAdapter(BookList.this, R.layout.book_item, books);
                listView.setAdapter(bookAdapter);
            }
            pb.setVisibility(View.INVISIBLE);
        }
    }
}