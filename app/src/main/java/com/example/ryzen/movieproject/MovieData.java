package com.example.ryzen.movieproject;

import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Ryzen on 3/7/2018.
 */

public class MovieData {

    private static final String JSON_ARRAY_RESULTS = "results";
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_VOTE_AVERAGE = "vote_average";
    private static final String JSON_POSTER_PATH = "poster_path";
    private static final String JSON_OVERVIEW = "overview";
    private static final String JSON_RELEASE_DATE = "release_date";

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String PARAM_API_KEY = "api_key";
    private static final String API_KEY = ;
    private static final String IMAGE_REQUEST_URL = "https://image.tmdb.org/t/p/w185";

    public static List<Movies> parseMovieData(URL requestUrl) throws IOException {

        String jsonResponse = httpRequest(requestUrl);
        List<Movies> movies = parseFromJSON(jsonResponse);

        return movies;
    }

    public static String httpRequest (URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("//A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            }else {

                return null;
            }

        } finally {

                urlConnection.disconnect();
        }

    }


    private static List<Movies> parseFromJSON(String json) {
        if (TextUtils.isEmpty(json)) return null;

        List<Movies> movies = new ArrayList<>();
        String id = "";
        String title = "";
        String voteAverage = "";
        String posterPath = "";
        String overview = "";
        String releaseDate = "";

        try {
            JSONObject baseJsonResponse = new JSONObject(json);
            if (baseJsonResponse.has(JSON_ARRAY_RESULTS)) {
                JSONArray results = baseJsonResponse.getJSONArray(JSON_ARRAY_RESULTS);
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    id = result.getString(JSON_ID);
                    title = result.getString(JSON_TITLE);
                    voteAverage = result.getString(JSON_VOTE_AVERAGE);
                    posterPath = result.getString(JSON_POSTER_PATH).substring(1); //remove '/'
                    overview = result.getString(JSON_OVERVIEW);
                    releaseDate = result.getString(JSON_RELEASE_DATE);
                    movies.add(new Movies(id, title, voteAverage, posterPath, overview, releaseDate));
                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return movies;
    }

    public static URL buildUrl (String path) {
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(path)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;
        try
        {
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return  url;
    }

    public static String imageUrl (String path) {
        Uri buildUri = Uri.parse(IMAGE_REQUEST_URL).buildUpon()
                .appendPath(path)
                .build();


        return  buildUri.toString();
    }
}
