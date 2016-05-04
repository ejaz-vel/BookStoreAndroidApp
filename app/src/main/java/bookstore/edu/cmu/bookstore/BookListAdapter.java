package bookstore.edu.cmu.bookstore;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import rest.client.Book;

/**
 * Created by ejazveljee on 4/5/16.
 */
public class BookListAdapter extends ArrayAdapter<Book> {
    private List<Book> books;

    public BookListAdapter(AppCompatActivity context, int resource, List<Book> objects) {
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
        bookName.setText(StringUtils.capitalize(book.getName()));

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        if (book.getPrice() != null) {
            TextView bookPrice = (TextView)convertView.findViewById(R.id.bookPrice);
            bookPrice.setText(formatter.format(book.getPrice()));
        }

        ImageView bookImage = (ImageView) convertView.findViewById(R.id.imageView);
        Bitmap bitmap = getBitmapFromAsset("harry_potter.jpg");
        bookImage.setImageBitmap(bitmap);

        if (book.getLatitude() != null && book.getLongitude() != null) {
            Double dist = getDistancetoBook(book);
            if (dist != null) {
                DecimalFormat df = new DecimalFormat("#.#");
                df.setRoundingMode(RoundingMode.CEILING);
                TextView bookDistance = (TextView) convertView.findViewById(R.id.bookDistance);
                bookDistance.setText(df.format(dist) + " miles");
            }
        }

        return convertView;
    }

    private Double getDistancetoBook(Book book) {
        int coarseLocPermissionCheck = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocPermissionCheck = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (coarseLocPermissionCheck == PackageManager.PERMISSION_GRANTED &&
                fineLocPermissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double currentLatitude = Math.toRadians(location.getLatitude());
                double bookLatitude = Math.toRadians(book.getLatitude());
                double latDiff = Math.toRadians(book.getLatitude() - location.getLatitude());
                double longDiff = Math.toRadians(book.getLongitude() - location.getLongitude());

                double a = (Math.sin(latDiff / 2) * Math.sin(latDiff / 2)) +
                        (Math.cos(currentLatitude) * Math.cos(bookLatitude) *
                                Math.sin(longDiff / 2) * Math.sin(longDiff / 2));
                double c = Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                return 3958.756 * c;
            }
        }
        return null;
    }

    private Bitmap getBitmapFromAsset(String assetName) {
        AssetManager assetManager = getContext().getAssets();
        InputStream stream;

        try {
            stream = assetManager.open(assetName);
            return BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
