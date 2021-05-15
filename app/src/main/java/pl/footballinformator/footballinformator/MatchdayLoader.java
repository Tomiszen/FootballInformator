package pl.footballinformator.footballinformator;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;

public class MatchdayLoader extends AsyncTaskLoader<ArrayList<Match>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = MatchdayLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link MatchdayLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public MatchdayLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Match> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        ArrayList<Match> matchday = QueryUtils.extractMatches(mUrl);
        return matchday;
    }

}
