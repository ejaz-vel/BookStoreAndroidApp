package bookstore.edu.cmu.bookstore;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;

import rest.client.Book;

import static bookstore.edu.cmu.bookstore.BookList.*;

public class BookDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Book book = (Book) getIntent().getSerializableExtra("BOOK");
        TextView bookName = (TextView) findViewById(R.id.nameText);
        bookName.setText(book.getName());

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        TextView bookPrice = (TextView) findViewById(R.id.priceText);
        bookPrice.setText(formatter.format(book.getPrice()));

        TextView bookDesc = (TextView) findViewById(R.id.descriptionText);
        bookDesc.setText(book.getDescription());

        ImageView bookImage = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = getBitmapFromAsset("harry_potter.jpg");
        bookImage.setImageBitmap(bitmap);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.cart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
