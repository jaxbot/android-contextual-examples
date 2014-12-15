package me.jaxbot.contextual;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jonathan on 12/10/14.
 */
public class Weather {
    final static String TAG = "Weather";

    public static boolean chanceOfRain() {
        // Not a real API call; replace URL with something like Forecast.io
        String data = getHTTPString("https://gist.githubusercontent.com/jaxbot/658b9b815b1d073fedd1/raw/6345bf8ff266633d166dcc9f5b1453a31c27868b/fakeweather.json");

        Log.i("Weather", data);

        try {
            JSONObject jObject = new JSONObject(data);
            if (jObject.getJSONObject("currently").getDouble("precipProbability") > 0.1) {
                return true;
            }
        } catch (JSONException e) {
            // In a real app, you'd throw an error.
            Log.e("Weather", "Json failed: " + e.toString());
        }

        return false;
    }

    private static String getHTTPString(String url) {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);

            HttpResponse response = httpclient.execute(httpget);

            String result = EntityUtils.toString(response.getEntity());

            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return "";
    }
}
