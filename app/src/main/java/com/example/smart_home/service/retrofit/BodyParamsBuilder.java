package com.example.smart_home.service.retrofit;

import com.google.gson.JsonObject;

public class BodyParamsBuilder {
    public static JsonObject text2Speech(String query) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("undefined", query);
        return jsonObject;
    }
}
