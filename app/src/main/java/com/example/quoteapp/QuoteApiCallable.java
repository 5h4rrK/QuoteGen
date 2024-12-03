package com.example.quoteapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class QuoteApiCallable implements Callable<String> {
    // Base URL for the ZenQuotes API
    private String baseURL;

    // Constructor
    public QuoteApiCallable() {
        this.baseURL = "https://zenquotes.io/api";
    }

    @Override
    public String call() throws Exception {
        return getRandomQuote();
    }

    private String getRandomQuote() {
        StringBuilder response = new StringBuilder();
        String endpointURL = this.baseURL + "/random";

        try {
            // Open the connection to the API
            URL apiUrl = new URL(endpointURL);
            HttpsURLConnection connection = (HttpsURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            // Check the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Read the response from the API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                // Parse and return the quote
                return parseQuote(response.toString());
            } else {
                return "Failed to connect to API. Response code: " + responseCode;
            }
        } catch (java.net.UnknownHostException e) {
            // Handle no internet connectivity
            return "No internet connection. Please check your network settings.";
        } catch (MalformedURLException e) {
            return "Malformed URL: " + e.getMessage();
        } catch (IOException e) {
            return "I/O Exception: " + e.getMessage();
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }


    // Method to parse the JSON response and extract the quote and author
    private String parseQuote(String jsonResponse) {
        try {
            // Parse the JSON response as an array
            JSONArray jsonArray = new JSONArray(jsonResponse);
            JSONObject quoteObject = jsonArray.getJSONObject(0);

            // Extract the quote text and author
            String text = quoteObject.getString("q");
            String author = quoteObject.getString("a");

            // Return the formatted quote
            return "\"" + text + "\" - " + author;
        } catch (Exception e) {
            return "Error parsing quote: " + e.getMessage();
        }
    }
}
