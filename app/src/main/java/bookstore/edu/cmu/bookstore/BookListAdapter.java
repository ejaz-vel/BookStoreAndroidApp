package bookstore.edu.cmu.bookstore;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.List;

import rest.client.Book;

/**
 * Created by ejazveljee on 4/5/16.
 */
public class BookListAdapter extends ArrayAdapter<Book> {
    private List<Book> books;

    public BookListAdapter(BookList context, int resource, List<Book> objects) {
        super(context, resource, objects);
        books = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.book_item, parent, false);
        }

        Book book = books.get(position);
        TextView bookName = (TextView)convertView.findViewById(R.id.bookName);
        bookName.setText(book.getName());

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        TextView bookPrice = (TextView)convertView.findViewById(R.id.bookPrice);
        bookPrice.setText(formatter.format(book.getPrice()));

        ImageView bookImage = (ImageView) convertView.findViewById(R.id.imageView);
        Bitmap bitmap = getBitmapFromAsset("harry_potter.jpg");
        bookImage.setImageBitmap(bitmap);

        return convertView;
    }

    private Bitmap getBitmapFromAsset(String assetName) {
        AssetManager assetManager = getContext().getAssets();
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
