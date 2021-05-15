package pl.footballinformator.footballinformator;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Sample JSON response for a USGS query
     */
    private static final String SAMPLE_JSON_RESPONSE = "{\"count\":9,\"filters\":{\"matchday\":\"11\"},\"competition\":{\"id\":2002,\"area\":{\"id\":2088,\"name\":\"Germany\"},\"name\":\"Bundesliga\",\"code\":\"BL1\",\"plan\":\"TIER_ONE\",\"lastUpdated\":\"2019-11-25T14:25:03Z\"},\"matches\":[{\"id\":271502,\"season\":{\"id\":474,\"startDate\":\"2019-08-16\",\"endDate\":\"2020-05-16\",\"currentMatchday\":12},\"utcDate\":\"2019-11-08T19:30:00Z\",\"status\":\"FINISHED\",\"matchday\":11,\"stage\":\"REGULAR_SEASON\",\"group\":\"Regular Season\",\"lastUpdated\":\"2019-11-09T20:55:02Z\",\"score\":{\"winner\":\"AWAY_TEAM\",\"duration\":\"REGULAR\",\"fullTime\":{\"homeTeam\":1,\"awayTeam\":2},\"halfTime\":{\"homeTeam\":1,\"awayTeam\":0},\"extraTime\":{\"homeTeam\":null,\"awayTeam\":null},\"penalties\":{\"homeTeam\":null,\"awayTeam\":null}},\"homeTeam\":{\"id\":1,\"name\":\"1. FC Köln\"},\"awayTeam\":{\"id\":2,\"name\":\"TSG 1899 Hoffenheim\"},\"referees\":[{\"id\":57529,\"name\":\"Robert Kampka\",\"nationality\":null},{\"id\":340,\"name\":\"Benedikt Kempkes\",\"nationality\":null},{\"id\":8828,\"name\":\"Thomas Stein\",\"nationality\":null},{\"id\":43880,\"name\":\"Rafael Foltyn\",\"nationality\":null},{\"id\":57517,\"name\":\"Daniel Schlager\",\"nationality\":null}]},{\"id\":271503,\"season\":{\"id\":474,\"startDate\":\"2019-08-16\",\"endDate\":\"2020-05-16\",\"currentMatchday\":12},\"utcDate\":\"2019-11-09T14:30:00Z\",\"status\":\"FINISHED\",\"matchday\":11,\"stage\":\"REGULAR_SEASON\",\"group\":\"Regular Season\",\"lastUpdated\":\"2019-11-09T23:56:30Z\",\"score\":{\"winner\":\"DRAW\",\"duration\":\"REGULAR\",\"fullTime\":{\"homeTeam\":3,\"awayTeam\":3},\"halfTime\":{\"homeTeam\":1,\"awayTeam\":0},\"extraTime\":{\"homeTeam\":null,\"awayTeam\":null},\"penalties\":{\"homeTeam\":null,\"awayTeam\":null}},\"homeTeam\":{\"id\":6,\"name\":\"FC Schalke 04\"},\"awayTeam\":{\"id\":24,\"name\":\"TSV Fortuna 95 Düsseldorf\"},\"referees\":[{\"id\":57504,\"name\":\"Robert Hartmann\",\"nationality\":null},{\"id\":57505,\"name\":\"Christian Leicher\",\"nationality\":null},{\"id\":43879,\"name\":\"Marco Achmüller\",\"nationality\":null},{\"id\":57735,\"name\":\"Michael Bacher\",\"nationality\":null},{\"id\":43922,\"name\":\"Benjamin Brand\",\"nationality\":null}]},{\"id\":271504,\"season\":{\"id\":474,\"startDate\":\"2019-08-16\",\"endDate\":\"2020-05-16\",\"currentMatchday\":12},\"utcDate\":\"2019-11-09T14:30:00Z\",\"status\":\"FINISHED\",\"matchday\":11,\"stage\":\"REGULAR_SEASON\",\"group\":\"Regular Season\",\"lastUpdated\":\"2019-11-09T23:56:30Z\",\"score\":{\"winner\":\"AWAY_TEAM\",\"duration\":\"REGULAR\",\"fullTime\":{\"homeTeam\":2,\"awayTeam\":4},\"halfTime\":{\"homeTeam\":1,\"awayTeam\":2},\"extraTime\":{\"homeTeam\":null,\"awayTeam\":null},\"penalties\":{\"homeTeam\":null,\"awayTeam\":null}},\"homeTeam\":{\"id\":9,\"name\":\"Hertha BSC\"},\"awayTeam\":{\"id\":721,\"name\":\"RB Leipzig\"},\"referees\":[{\"id\":57515,\"name\":\"Sören Storks\",\"nationality\":null},{\"id\":57530,\"name\":\"Thorben Siewer\",\"nationality\":null},{\"id\":338,\"name\":\"Norbert Grudzinski\",\"nationality\":null},{\"id\":57703,\"name\":\"Marcel Unger\",\"nationality\":null},{\"id\":57516,\"name\":\"Benjamin Cortus\",\"nationality\":null}]},{\"id\":271509,\"season\":{\"id\":474,\"startDate\":\"2019-08-16\",\"endDate\":\"2020-05-16\",\"currentMatchday\":12},\"utcDate\":\"2019-11-09T14:30:00Z\",\"status\":\"FINISHED\",\"matchday\":11,\"stage\":\"REGULAR_SEASON\",\"group\":\"Regular Season\",\"lastUpdated\":\"2019-11-09T18:31:42Z\",\"score\":{\"winner\":\"AWAY_TEAM\",\"duration\":\"REGULAR\",\"fullTime\":{\"homeTeam\":2,\"awayTeam\":3},\"halfTime\":{\"homeTeam\":0,\"awayTeam\":2},\"extraTime\":{\"homeTeam\":null,\"awayTeam\":null},\"penalties\":{\"homeTeam\":null,\"awayTeam\":null}},\"homeTeam\":{\"id\":15,\"name\":\"1. FSV Mainz 05\"},\"awayTeam\":{\"id\":28,\"name\":\"1. FC Union Berlin\"},\"referees\":[{\"id\":253,\"name\":\"Harm Osmers\",\"nationality\":null},{\"id\":254,\"name\":\"Thomas Gorniak\",\"nationality\":null},{\"id\":57509,\"name\":\"Robert Kempter\",\"nationality\":null},{\"id\":57506,\"name\":\"Markus Schüller\",\"nationality\":null},{\"id\":57519,\"name\":\"Robert Schröder\",\"nationality\":null}]},{\"id\":271510,\"season\":{\"id\":474,\"startDate\":\"2019-08-16\",\"endDate\":\"2020-05-16\",\"currentMatchday\":12},\"utcDate\":\"2019-11-09T14:30:00Z\",\"status\":\"FINISHED\",\"matchday\":11,\"stage\":\"REGULAR_SEASON\",\"group\":\"Regular Season\",\"lastUpdated\":\"2019-11-09T23:56:31Z\",\"score\":{\"winner\":\"AWAY_TEAM\",\"duration\":\"REGULAR\",\"fullTime\":{\"homeTeam\":0,\"awayTeam\":1},\"halfTime\":{\"homeTeam\":0,\"awayTeam\":1},\"extraTime\":{\"homeTeam\":null,\"awayTeam\":null},\"penalties\":{\"homeTeam\":null,\"awayTeam\":null}},\"homeTeam\":{\"id\":29,\"name\":\"SC Paderborn 07\"},\"awayTeam\":{\"id\":16,\"name\":\"FC Augsburg\"},\"referees\":[{\"id\":15821,\"name\":\"Marco Fritz\",\"nationality\":null},{\"id\":57524,\"name\":\"Dominik Schaal\",\"nationality\":null},{\"id\":57525,\"name\":\"Marcel Pelgrim\",\"nationality\":null},{\"id\":57528,\"name\":\"Martin Thomsen\",\"nationality\":null},{\"id\":174,\"name\":\"Markus Schmidt\",\"nationality\":null}]},{\"id\":271506,\"season\":{\"id\":474,\"startDate\":\"2019-08-16\",\"endDate\":\"2020-05-16\",\"currentMatchday\":12},\"utcDate\":\"2019-11-09T17:30:00Z\",\"status\":\"FINISHED\",\"matchday\":11,\"stage\":\"REGULAR_SEASON\",\"group\":\"Regular Season\",\"lastUpdated\":\"2019-11-09T23:56:31Z\",\"score\":{\"winner\":\"HOME_TEAM\",\"duration\":\"REGULAR\",\"fullTime\":{\"homeTeam\":4,\"awayTeam\":0},\"halfTime\":{\"homeTeam\":1,\"awayTeam\":0},\"extraTime\":{\"homeTeam\":null,\"awayTeam\":null},\"penalties\":{\"homeTeam\":null,\"awayTeam\":null}},\"homeTeam\":{\"id\":5,\"name\":\"FC Bayern München\"},\"awayTeam\":{\"id\":4,\"name\":\"BV Borussia 09 Dortmund\"},\"referees\":[{\"id\":43878,\"name\":\"Felix Zwayer\",\"nationality\":null},{\"id\":43923,\"name\":\"Thorsten Schiffner\",\"nationality\":null},{\"id\":339,\"name\":\"Sascha Thielert\",\"nationality\":null},{\"id\":57532,\"name\":\"Frank Willenborg\",\"nationality\":null},{\"id\":15408,\"name\":\"Daniel Siebert\",\"nationality\":null}]},{\"id\":271505,\"season\":{\"id\":474,\"startDate\":\"2019-08-16\",\"endDate\":\"2020-05-16\",\"currentMatchday\":12},\"utcDate\":\"2019-11-10T12:30:00Z\",\"status\":\"FINISHED\",\"matchday\":11,\"stage\":\"REGULAR_SEASON\",\"group\":\"Regular Season\",\"lastUpdated\":\"2019-11-10T17:35:22Z\",\"score\":{\"winner\":\"HOME_TEAM\",\"duration\":\"REGULAR\",\"fullTime\":{\"homeTeam\":3,\"awayTeam\":1},\"halfTime\":{\"homeTeam\":2,\"awayTeam\":0},\"extraTime\":{\"homeTeam\":null,\"awayTeam\":null},\"penalties\":{\"homeTeam\":null,\"awayTeam\":null}},\"homeTeam\":{\"id\":18,\"name\":\"Borussia Mönchengladbach\"},\"awayTeam\":{\"id\":12,\"name\":\"SV Werder Bremen\"},\"referees\":[{\"id\":43943,\"name\":\"Tobias Stieler\",\"nationality\":null},{\"id\":56230,\"name\":\"Matthias Jöllenbeck\",\"nationality\":null},{\"id\":15747,\"name\":\"Christian Gittelmann\",\"nationality\":null},{\"id\":57514,\"name\":\"Tobias Christ\",\"nationality\":null},{\"id\":9567,\"name\":\"Sascha Stegemann\",\"nationality\":null}]},{\"id\":271508,\"season\":{\"id\":474,\"startDate\":\"2019-08-16\",\"endDate\":\"2020-05-16\",\"currentMatchday\":12},\"utcDate\":\"2019-11-10T14:30:00Z\",\"status\":\"FINISHED\",\"matchday\":11,\"stage\":\"REGULAR_SEASON\",\"group\":\"Regular Season\",\"lastUpdated\":\"2019-11-11T14:50:05Z\",\"score\":{\"winner\":\"AWAY_TEAM\",\"duration\":\"REGULAR\",\"fullTime\":{\"homeTeam\":0,\"awayTeam\":2},\"halfTime\":{\"homeTeam\":0,\"awayTeam\":1},\"extraTime\":{\"homeTeam\":null,\"awayTeam\":null},\"penalties\":{\"homeTeam\":null,\"awayTeam\":null}},\"homeTeam\":{\"id\":11,\"name\":\"VfL Wolfsburg\"},\"awayTeam\":{\"id\":3,\"name\":\"Bayer 04 Leverkusen\"},\"referees\":[{\"id\":57510,\"name\":\"Manuel Gräfe\",\"nationality\":null},{\"id\":57511,\"name\":\"Markus Sinn\",\"nationality\":null},{\"id\":57534,\"name\":\"Jan Neitzel\",\"nationality\":null},{\"id\":57516,\"name\":\"Benjamin Cortus\",\"nationality\":null},{\"id\":56187,\"name\":\"Guido Winkmann\",\"nationality\":null}]},{\"id\":271507,\"season\":{\"id\":474,\"startDate\":\"2019-08-16\",\"endDate\":\"2020-05-16\",\"currentMatchday\":12},\"utcDate\":\"2019-11-10T17:00:00Z\",\"status\":\"FINISHED\",\"matchday\":11,\"stage\":\"REGULAR_SEASON\",\"group\":\"Regular Season\",\"lastUpdated\":\"2019-11-11T21:55:04Z\",\"score\":{\"winner\":\"HOME_TEAM\",\"duration\":\"REGULAR\",\"fullTime\":{\"homeTeam\":1,\"awayTeam\":0},\"halfTime\":{\"homeTeam\":0,\"awayTeam\":0},\"extraTime\":{\"homeTeam\":null,\"awayTeam\":null},\"penalties\":{\"homeTeam\":null,\"awayTeam\":null}},\"homeTeam\":{\"id\":17,\"name\":\"SC Freiburg\"},\"awayTeam\":{\"id\":19,\"name\":\"Eintracht Frankfurt\"},\"referees\":[{\"id\":43875,\"name\":\"Felix Brych\",\"nationality\":null},{\"id\":43876,\"name\":\"Mark Borsch\",\"nationality\":null},{\"id\":43877,\"name\":\"Stefan Lupp\",\"nationality\":null},{\"id\":57518,\"name\":\"Florian Badstübner\",\"nationality\":null},{\"id\":43922,\"name\":\"Benjamin Brand\",\"nationality\":null}]}]}";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static ArrayList<Match> extractMatches(String requestURL) {

        // Create an empty ArrayList that we can start adding matches to
        ArrayList<Match> matches = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        // Create URL object
        URL url = createUrl(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with making HTTP request", e);
            return null;
        }

        try {

            JSONObject objectJSON = new JSONObject(jsonResponse);
            JSONArray arrayJSON = objectJSON.getJSONArray("matches");
            for (int i = 0; i < arrayJSON.length(); i++) {
                JSONObject match = arrayJSON.getJSONObject(i);
                JSONObject team = match.getJSONObject("homeTeam");
                String homeTeamName = team.getString("name");
                int homeID = team.getInt("id");
                team = match.getJSONObject("awayTeam");
                String awayTeamName = team.getString("name");
                int awayID = team.getInt("id");
                String date = match.getString("utcDate");
                String status = match.getString("status");
                JSONObject score = match.getJSONObject("score");
                JSONObject fullTimeScore = score.getJSONObject("fullTime");
                int homeScore = 0;
                int awayScore = 0;

                if (!status.equals("SCHEDULED")) {
                    homeScore = fullTimeScore.getInt("homeTeam");
                    awayScore = fullTimeScore.getInt("awayTeam");
                }

                int homeLogoID = getLogoID(homeID);
                int awayLogoID = getLogoID(awayID);

                matches.add(new Match(date, homeTeamName, awayTeamName, homeScore, awayScore, homeLogoID, awayLogoID, status));


            }
/*                    JSONObject season = match.getJSONObject("season");
                    int matchday = season.getInt("currentMatchday");

                    StringBuilder matchdayBuilder = new StringBuilder();
                    matchdayBuilder.append("Matchday ").append(matchday);
                    String matchdayNumber = matchdayBuilder.toString();
                    TextView matchdayTextView = findViewById(R.id.matchday_number);
                    matchdayTextView.setText(matchdayNumber);*/


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the match JSON results:        " + jsonResponse, e);
        }

        // Return the list of earthquakes
        return matches;
    }

    private static int getLogoID(int teamID) {
        Integer LogoID;
        switch (teamID) {
            case 1:
                LogoID = R.drawable.kol;
                break;
            case 2:
                LogoID = R.drawable.hof;
                break;
            case 3:
                LogoID = R.drawable.lev;
                break;
            case 4:
                LogoID = R.drawable.dor;
                break;
            case 5:
                LogoID = R.drawable.bmu;
                break;
            case 6:
                LogoID = R.drawable.s04;
                break;
            case 9:
                LogoID = R.drawable.her;
                break;
            case 11:
                LogoID = R.drawable.wol;
                break;
            case 12:
                LogoID = R.drawable.wer;
                break;
            case 15:
                LogoID = R.drawable.mai;
                break;
            case 16:
                LogoID = R.drawable.aug;
                break;
            case 17:
                LogoID = R.drawable.frg;
                break;
            case 18:
                LogoID = R.drawable.mgl;
                break;
            case 19:
                LogoID = R.drawable.fra;
                break;
            case 24:
                LogoID = R.drawable.f95;
                break;
            case 28:
                LogoID = R.drawable.unb;
                break;
            case 29:
                LogoID = R.drawable.pad;
                break;
            case 721:
                LogoID = R.drawable.rbl;
                break;

            default:
                LogoID = R.drawable.none;
        }
        return LogoID;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.addRequestProperty("X-Auth-Token", "***");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the matchday JSON result. ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
