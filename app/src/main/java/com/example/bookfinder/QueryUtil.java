package com.example.bookfinder;

import android.text.TextUtils;
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
import java.util.List;

public class QueryUtil {

    private static final String LOG_TAG = QueryUtil.class.getName();

    private QueryUtil()
    {}

    public static List<Book> fetchBookData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
        }

        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        List<Book> books = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Book}s
        return books;
    }

    //method for creating url object
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    //method to make http request connection
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


     //Convert the InputStream into a String which contains the whole JSON response from the server.
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
    
    //Return a list of {@link Book} objects that has been built up from parsing the given JSON response.
    private static List<Book> extractFeatureFromJson(String BookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(BookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Books to
        List<Book> books = new ArrayList<>();

        //parsing JSONresponse
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(BookJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of Books
            JSONArray BookArray = baseJsonResponse.getJSONArray("items");

            // For each Book in the BookArray, create an {@link Book} object
            for (int i = 0; i < BookArray.length(); i++) {

                // Get a single Book at position i within the list of Books
                JSONObject currentBook = BookArray.getJSONObject(i);

                // For a given Book, extract the JSONObject associated with the key called "volinfo"
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                // Extract the value for the key called "title"
                String title = volumeInfo.getString("title");

                // Extract the value for the key called "authors"
                //JSONArray AuthorArray = volumeInfo.getJSONArray("authors");
                String author ="Anonymous";
                if (volumeInfo.has("authors"))
                    author = volumeInfo.getJSONArray("authors").getString(0);

                // Extract the value for the key called "publisher"
                String publisher ="";
                if (volumeInfo.has("publisher"))
                    publisher = volumeInfo.getString("publisher");

                // Extract the value for the key called "description"
                String description ="";
                if (volumeInfo.has("description"))
                    description = volumeInfo.getString("description");

                // Extract the value for the key called "previewLink"
                String previewlink ="";
                if (volumeInfo.has("previewLink"))
                    previewlink = volumeInfo.getString("previewLink");

                //Log.v(LOG_TAG, "Book Details: "+previewlink);

                // Extract the value for the key called "pages"
                int pageCount = 0;
                if (volumeInfo.has("pageCount"))
                    pageCount = volumeInfo.getInt("pageCount");

                // Extract the value for the key called "averagerating"
                double rating = 0.0;
                if (volumeInfo.has("averageRating"))
                    rating = volumeInfo.getDouble("averageRating");

                // Extract the value for the key called "categories"
                String categories ="";
                if (volumeInfo.has("categories"))
                    categories = volumeInfo.getJSONArray("categories").getString(0);

                // Extract the value for the key called "imagelink"
                String imagelink ="";
                if (volumeInfo.has("imageLinks"))
                    imagelink = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");


                // For a given Book, extract the JSONObject associated with the key called "saleInfo"
                JSONObject saleInfo = currentBook.getJSONObject("saleInfo");

                // Extract the value for the key called "retailPrice"
                double price = 0.0;
                if (saleInfo.has("retailPrice"))
                    price = saleInfo.getJSONObject("retailPrice").getDouble("amount");


                // Create a new Book object
                Book book = new Book(title,author,imagelink,price,rating,pageCount,categories,previewlink,
                        publisher,description);

                // Add the new Book to the list of Books.
                books.add(book);
            }

        } catch (JSONException e) {
            Log.e("QueryUtil", "Problem parsing the Book JSON results", e);
        }

        // Return the list of Books
        return books;
    }


}
