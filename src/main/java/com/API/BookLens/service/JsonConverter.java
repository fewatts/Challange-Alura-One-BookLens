package com.API.BookLens.service;

import com.google.gson.Gson;

/**
 * A utility class to handle JSON conversion.
 */
public class JsonConverter {

    private static final Gson gson = new Gson();

    /**
     * Converts a JSON string to an object of the specified class.
     *
     * @param json  The JSON string to convert.
     * @param clazz The class of the object to convert to.
     * @param <T>   The type of the object to convert to.
     * @return An object of the specified class populated with data from the JSON
     *         string.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

}