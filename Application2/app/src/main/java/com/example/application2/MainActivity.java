package com.example.application2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ImageButton notificationIcon;
    private TextView notificationBadge;
    private List<Notification> notifications = new ArrayList<>();
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        notificationIcon = findViewById(R.id.notification_icon);
        notificationBadge = findViewById(R.id.notification_badge);

        // Fetch notifications from API
        fetchNotifications();

        notificationIcon.setOnClickListener(v -> showNotificationPopup());

        // Schedule periodic work for real-time updates
        WorkManager workManager = WorkManager.getInstance(this);
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                NotificationWorker.class,
                15, // Repeat interval in minutes
                TimeUnit.MINUTES
        ).build();
        workManager.enqueue(workRequest);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Channel for app notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notification_channel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void fetchNotifications() {
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
                                    Notification notification = new Notification(name, desc);
                                    notifications.add(notification);

                                    // Show a notification in the status bar
                                    showStatusBarNotification(name, desc);
                                }
                            }
                            updateBadge(notifications.size());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void showStatusBarNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void updateBadge(int count) {
        if (count > 0) {
            notificationBadge.setVisibility(View.VISIBLE);
            notificationBadge.setText(String.valueOf(count));
        } else {
            notificationBadge.setVisibility(View.GONE);
        }
    }

    private void showNotificationPopup() {
        View popupView = getLayoutInflater().inflate(R.layout.notification_popup, null);
        ListView listView = popupView.findViewById(R.id.notification_list);

        ArrayAdapter<Notification> adapter = new ArrayAdapter<Notification>(
                this, android.R.layout.simple_list_item_2, android.R.id.text1, notifications) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                text1.setText(notifications.get(position).getName());
                text2.setText(notifications.get(position).getDescription());
                return view;
            }
        };

        listView.setAdapter(adapter);

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.showAsDropDown(notificationIcon);
    }
}