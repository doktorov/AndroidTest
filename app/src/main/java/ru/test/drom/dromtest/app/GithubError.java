package ru.test.drom.dromtest.app;

import org.json.JSONException;
import org.json.JSONObject;

public class GithubError extends Throwable {
    public GithubError(String responseBody) {
        super(getMessage(responseBody));
    }

    private static String getMessage(String responseBody) {
        try {
            return new JSONObject(responseBody).optString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "Unknown exception";
    }
}