package bookstore.edu.cmu.bookstore;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import rest.client.Book;

import static bookstore.edu.cmu.bookstore.BookList.*;

public class BookDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Book book = (Book) getIntent().getSerializableExtra("BOOK");
        TextView bookName = (TextView) findViewById(R.id.nameText);
        bookName.setText(StringUtils.capitalize(book.getName() + " " + book.getEdition()));

        TextView bookAuthor = (TextView) findViewById(R.id.authorText);
        bookAuthor.setText(StringUtils.capitalize(book.getAuthor()));

        TextView rent_sale = (TextView) findViewById(R.id.saleText);
        if (book.getRentAllowed() != null && book.getRentAllowed() == true) {
            rent_sale.setText("Book for Rent");
        } else {
            rent_sale.setText("Book for Sale");
        }

        if (book.getPrice() != null) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            TextView bookPrice = (TextView) findViewById(R.id.priceText);
            bookPrice.setText(formatter.format(book.getPrice()));
        }

        if (book.getDescription() != null) {
            TextView bookDesc = (TextView) findViewById(R.id.descriptionText);
            bookDesc.setText(book.getDescription());
        }

        ImageView bookImage = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = getBitmapFromAsset("harry_potter.jpg");
        bookImage.setImageBitmap(bitmap);

        RatingBar condition = (RatingBar) findViewById(R.id.ratingBar);
        condition.setVisibility(View.VISIBLE);
        condition.setRating(book.getCondition());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.cart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Book added to cart", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ImageView directionImage = (ImageView) findViewById(R.id.directionImage);
        directionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> books = new ArrayList<>();
                books.add(book);
                Intent intent = new Intent(BookDetail.this, Map.class);
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

    private Bitmap getBitmapFromAsset(String assetName) {
        AssetManager assetManager = this.getAssets();
        InputStream stream = null;

        try {
            stream = assetManager.open(assetName);
            return BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
