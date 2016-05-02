package bookstore.edu.cmu.bookstore;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import rest.client.Book;
import rest.client.BookClient;

public class SearchableActivity extends AppCompatActivity {

    List<Book> books;
    ProgressBar pb;
    ListView listView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        pb = (ProgressBar) findViewById(R.id.progressBar2);
        pb.setVisibility(View.INVISIBLE);
        listView = (ListView) findViewById(R.id.listViewSearch);
        fab = (FloatingActionButton) findViewById(R.id.gmapSearch);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            performSearch(query);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchableActivity.this, BookDetail.class);
                Book book = books.get(position);
                intent.putExtra("BOOK", book);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchableActivity.this, Map.class);
                intent.putExtra("BOOKS", (Serializable) books);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        if (id == R.id.action_search) {
            onSearchRequested();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }

    private void performSearch(String query) {
        BookListTask task = new BookListTask();
        task.execute(query);
    }

    private class BookListTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            BookClient bc = new BookClient("52.27.184.29:80");
            try {
                books = bc.search(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccessfull) {
            if (isSuccessfull) {
                BookListAdapter bookAdapter = new BookListAdapter(SearchableActivity.this, R.layout.book_item, books);
                listView.setAdapter(bookAdapter);
            }

            pb.setVisibility(View.INVISIBLE);
        }
    }

}
