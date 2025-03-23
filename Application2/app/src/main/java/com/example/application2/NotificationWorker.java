package com.example.application2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.transform.Result;

public class NotificationWorker extends Worker {

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Fetch notifications from API
//        String url = "http://10.0.2.2:3000/api/notifications";
        String url = AppConfig.NOTIFICATIONS_ENDPOINT;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.has("success") && response.getBoolean("success")) {
                            JSONArray dataArray = response.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);

                                if (obj.has("notificationName") && obj.has("notificationDesc")) {
                                    String name = obj.optString("notificationName", "No Name");
                                    String desc = obj.optString("notificationDesc", "No Description");

                                    // Show a notification in the status bar
                                    showStatusBarNotification(name, desc);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                }
        );

        Volley.newRequestQueue(getApplicationContext()).add(request);

        return Result.success();
    }

    private void showStatusBarNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "notification_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1, builder.build());
    }
}