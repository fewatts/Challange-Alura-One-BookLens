package com.API.BookLens.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * A utility class to retrieve data from an API endpoint.
 */
public class GetAPIData {

    /**
     * Retrieves JSON data from the specified API endpoint.
     *
     * @param address The address of the API endpoint to retrieve data from.
     * @return A String containing the JSON data retrieved from the API endpoint.
     * @throws RuntimeException If an error occurs during the HTTP request.
     */
    static public String getBookData(String address) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(address))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response.body();
    }
    
}