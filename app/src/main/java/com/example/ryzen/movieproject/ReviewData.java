package com.example.ryzen.movieproject;

import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Ryzen on 4/23/2018.
 */

public class ReviewData {

    private static final String JSON_AUTHOR = "author";
    private static final String JSON_CONTENT = "content";
    private static final String JSON_RESULTS = "results";

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String REVIEWS = "reviews";
    private static final String PARAM_API_KEY = "api_key";
    private static final String API_KEY = ;

    public static List<Reviews> parseReviewsData (URL requestUrl) throws IOException {

        String jsonReview = reviewRequest(requestUrl);
        List<Reviews> reviewsList = parseReviewJSON(jsonReview);

        return  reviewsList;
    }

    public static String reviewRequest (URL url) throws IOException{

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("//A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            }else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static List<Reviews> parseReviewJSON (String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        List<Reviews> reviews = new ArrayList<>();
        String author = "";
        String content = "";

        try {
            JSONObject baseJsonResponse = new JSONObject(json);
            if (baseJsonResponse.has(JSON_RESULTS)) {
                JSONArray results = baseJsonResponse.getJSONArray(JSON_RESULTS);
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    author = result.getString(JSON_AUTHOR);
                    content = result.getString(JSON_CONTENT);
                    reviews.add(new Reviews(author, content));
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static URL buildReviewUrl (String path) {

        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(path)
                .appendPath(REVIEWS)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;

        try{
            url = new URL(buildUri.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}
