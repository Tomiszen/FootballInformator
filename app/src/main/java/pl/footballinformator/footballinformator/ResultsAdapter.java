package pl.footballinformator.footballinformator;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by Tomisz on 2019-10-16.
 */

public class ResultsAdapter extends ArrayAdapter<Match> {

    private Context context;

    public ResultsAdapter(Activity context, ArrayList<Match> results) {
        super(context, 0, results);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.result_list_item, parent, false);
        }
        Match currentResult = getItem(position);

        TextView currentTextView = listItemView.findViewById(R.id.home_result);
        currentTextView.setText(String.valueOf(currentResult.getHomeScore()));
        currentTextView = listItemView.findViewById(R.id.away_result);
        currentTextView.setText(String.valueOf(currentResult.getAwayScore()));
        currentTextView = listItemView.findViewById(R.id.home_name);
        currentTextView.setText(currentResult.getHomeTeam());
        currentTextView = listItemView.findViewById(R.id.away_name);
        currentTextView.setText(currentResult.getAwayTeam());

        String dateString = currentResult.getDate();
        String date = dateString.substring(0, 10) + " " + dateString.substring(11, 19);
        currentTextView = listItemView.findViewById(R.id.date_text);
        currentTextView.setText(date);

        ImageView currentImageView = (ImageView) listItemView.findViewById(R.id.home_image);
        currentImageView.setImageResource(currentResult.getHomeLogoID());
        currentImageView = (ImageView) listItemView.findViewById(R.id.away_image);
        currentImageView.setImageResource(currentResult.getAwayLogoID());

        currentTextView = listItemView.findViewById(R.id.status);
        currentTextView.setText(currentResult.getStatus());

        int colorStatusHome = ContextCompat.getColor(getContext(), R.color.colorStatusGrey);
        int colorStatusAway = ContextCompat.getColor(getContext(), R.color.colorStatusGrey);
        if (currentResult.getStatus().equals("FINISHED")) {
            if (currentResult.getHomeScore() == currentResult.getAwayScore()) {
                colorStatusHome = ContextCompat.getColor(getContext(), R.color.colorStatusOrange);
                colorStatusAway = ContextCompat.getColor(getContext(), R.color.colorStatusOrange);
            } else if (currentResult.getHomeScore() > currentResult.getAwayScore()) {
                colorStatusHome = ContextCompat.getColor(getContext(), R.color.colorStatusGreen);
                colorStatusAway = ContextCompat.getColor(getContext(), R.color.colorStatusRed);
            } else {
                colorStatusHome = ContextCompat.getColor(getContext(), R.color.colorStatusRed);
                colorStatusAway = ContextCompat.getColor(getContext(), R.color.colorStatusGreen);
            }
        }
        View currentView = (View) listItemView.findViewById(R.id.home_status);
        currentView.setBackgroundColor(colorStatusHome);
        currentView = (View) listItemView.findViewById(R.id.away_status);
        currentView.setBackgroundColor(colorStatusAway);
        return listItemView;
    }
}
