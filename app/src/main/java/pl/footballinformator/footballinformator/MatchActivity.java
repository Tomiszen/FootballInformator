/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.footballinformator.footballinformator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;

public class MatchActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Match>> {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = MatchActivity.class.getName();

    /**
     * URL to query the USGS dataset for matchday information
     */
    private static final String REQUEST_URL = "http://api.football-data.org/v2";
    //"http://api.football-data.org/v2/competitions/2002/matches?matchday=16";

    /**
     * Constant value for the matchday loader ID.
     */
    private static final int MATCHDAY_LOADER_ID = 1;

    /**
     * Create a new {@link ArrayAdapter} of matches
     */
    private ArrayAdapter<Match> mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * ProgressBar that is displayed when the matches are loading
     */
    private ProgressBar mLoadingSpinner;

    /**
     * TextView that displays matchday number
     */
    private TextView mMatchdayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView matchesListView = (ListView) findViewById(R.id.list);

        mAdapter = new ResultsAdapter(this, new ArrayList<Match>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        matchesListView.setAdapter(mAdapter);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        matchesListView.setEmptyView(mEmptyStateTextView);

        mLoadingSpinner = findViewById(R.id.loading_spinner);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            getSupportLoaderManager().initLoader(MATCHDAY_LOADER_ID, null, this);
        } else {
            // Hide loading spinner
            mLoadingSpinner.setVisibility(View.GONE);

            // Set empty state text to display "No matches found."
            mEmptyStateTextView.setText(R.string.no_connection);
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String league = sharedPrefs.getString(
                getString(R.string.settings_league_key),
                getString(R.string.settings_league_default));

        String matchday = sharedPrefs.getString(
                getString(R.string.settings_matchday_key),
                getString(R.string.settings_matchday_default));

        // Find a reference to the {@link TextView} in the layout
        mMatchdayTextView = findViewById(R.id.matchday_number);
        mMatchdayTextView.setText("Matchday " + matchday);
    }

    @Override
    public Loader<ArrayList<Match>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String league = sharedPrefs.getString(
                getString(R.string.settings_league_key),
                getString(R.string.settings_league_default));

        String matchday = sharedPrefs.getString(
                getString(R.string.settings_matchday_key),
                getString(R.string.settings_matchday_default));

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendPath("competitions");
        uriBuilder.appendPath(league);
        uriBuilder.appendPath("matches");
        uriBuilder.appendQueryParameter("matchday", matchday);

        Log.i(LOG_TAG, uriBuilder.toString());

        // Create a new loader for the given URL
        return new MatchdayLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Match>> loader, ArrayList<Match> results) {

        // Clear the adapter of previous matches data
        mAdapter.clear();

        // Hide loading spinner
        mLoadingSpinner.setVisibility(View.GONE);

        // Set empty state text to display "No matches found."
        mEmptyStateTextView.setText(R.string.no_matches);

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (results != null && !results.isEmpty()) {
            mAdapter.addAll(results);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Match>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
