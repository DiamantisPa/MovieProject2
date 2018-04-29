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

public class VideoData {

    private static final String JSON_RESULTS = "results";
    private static final String JSON_ID = "id";
    private static final String JSON_KEY = "key";
    private static final String JSON_NAME = "name";
    private static final String JSON_SITE = "site";
    private static final String JSON_TYPE = "type";
    private static final String JSON_SIZE = "size";

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String PARAM_API_KEY = "api_key";
    private static final String API_KEY = ;
    private static final String VIDEOS = "videos";
    private static final String TRAILER_THUMBNAIL = "http://img.youtube.com/vi";
    private static final String DEFAULT_TRAILER = "hqdefault.jpg";

    public static List<Video> parseTrailerData(URL requestUrl) throws IOException {

        String jsonTrailer = trailerRequest(requestUrl);
        List<Video> videoList = parseVideoJSON(jsonTrailer);

        return videoList;
    }

    public static String trailerRequest (URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("//A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            }else  {
                return null;
            }
        } finally {
             urlConnection.disconnect();
        }
    }

    private static List<Video> parseVideoJSON (String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        List<Video> trailers = new ArrayList<>();
        String id = "";
        String key = "";
        String name = "";
        String site = "";
        String size = "";
        String type = "";

        try {
            JSONObject baseJsonResponse = new JSONObject(json);
            if (baseJsonResponse.has(JSON_RESULTS)) {
                JSONArray results = baseJsonResponse.getJSONArray(JSON_RESULTS);
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    id = result.getString(JSON_ID);
                    key = result.getString(JSON_KEY);
                    name = result.getString(JSON_NAME);
                    site = result.getString(JSON_SITE);
                    size = result.getString(JSON_SIZE);
                    type = result.getString(JSON_TYPE);
                    trailers.add(new Video(id , key , name, site, size, type));
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }

    public static URL buildVideoUlr (String id) {
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(VIDEOS)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;

        try{
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String trailerThumbnail (String path) {
        Uri buildUri = Uri.parse(TRAILER_THUMBNAIL).buildUpon()
                .appendPath(path)
                .appendPath(DEFAULT_TRAILER)
                .build();

        return buildUri.toString();
    }

}

