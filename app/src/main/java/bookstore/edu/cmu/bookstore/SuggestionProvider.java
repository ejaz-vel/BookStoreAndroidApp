package bookstore.edu.cmu.bookstore;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by ejazveljee on 5/2/16.
 */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "bookstore.edu.cmu.bookstore.SuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
