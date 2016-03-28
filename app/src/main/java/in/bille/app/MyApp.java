package in.bille.app;

/**
 * Created by Vibhor Goel on 2/11/2016.
 */
import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class MyApp extends Application {
    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override
    public void onCreate() {
super.onCreate();
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-73635301-1"); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);



    }
}