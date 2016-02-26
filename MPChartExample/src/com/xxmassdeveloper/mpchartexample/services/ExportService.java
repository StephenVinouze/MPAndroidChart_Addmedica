package com.xxmassdeveloper.mpchartexample.services;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.xxmassdeveloper.mpchartexample.R;
import com.xxmassdeveloper.mpchartexample.utils.ChartUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by stephenvinouze on 25/02/16.
 */
public class ExportService extends Service {

    private static final int MAX_EFFORT_VALUE = 10;
    private static final int MAX_PAIN_VALUE = 21;
    private static final int NOTIFICATION_ID = 100;

    private final IBinder binder = new ExportBinder();

    public class ExportBinder extends Binder {
        public ExportService getService() {
            return ExportService.this;
        }
    }

    private static Handler backgroundHandler;

    public static Handler getBackgroundHandler() {
        if (backgroundHandler == null) {
            HandlerThread printerThread = new HandlerThread("ExportThread");
            printerThread.start();
            backgroundHandler = new Handler(printerThread.getLooper());
        }
        return backgroundHandler;
    }

    private LineChart mLineChart;

    @Override
    public void onCreate() {
        super.onCreate();

        mLineChart = new LineChart(this);
        ChartUtils.configureChart(mLineChart, ChartUtils.ChartMode.LIGHT, this, null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    public void exportToPdf() {
        getBackgroundHandler().post(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();

                publishNotification("Export started", true);

                // Generate charts for the last year and save them to sdcard
                for (int i = 0; i < calendar.getActualMaximum(Calendar.MONTH); i++) {
                    calendar.add(Calendar.MONTH, -i);

                    int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

                    publishNotification("Generating chart for " + monthName, true);

                    List<Entry> lineEffortEntries = new ArrayList<>();
                    List<Entry> lineExhaustionEntries = new ArrayList<>();
                    List<Entry> linePainEntries = new ArrayList<>();
                    List<String> xValues = new ArrayList<>();
                    for (int j = 0; j < daysInMonth; j++) {
                        lineEffortEntries.add(new Entry(ChartUtils.getRandom(0, MAX_EFFORT_VALUE), j));
                        lineExhaustionEntries.add(new Entry(ChartUtils.getRandom(0, MAX_EFFORT_VALUE), j));
                        linePainEntries.add(new Entry(ChartUtils.getRandom(0, MAX_PAIN_VALUE), j));

                        xValues.add(String.format("%02d", j + 1));
                    }

                    mLineChart.setData(ChartUtils.generateLineData(ExportService.this, lineEffortEntries, lineExhaustionEntries, linePainEntries, xValues, ChartUtils.ChartMode.LIGHT));
                    mLineChart.invalidate();
                    if (!mLineChart.saveUnattachedChartToPath("line_chart_" + i, "", 1800, 1000, 50, ContextCompat.getColor(ExportService.this, R.color.bg_light))) {
                        Toast.makeText(ExportService.this, "Failed to save chart for " + monthName, Toast.LENGTH_SHORT).show();
                    }
                }

                // TODO: Generate Pdf using the generated charts
                publishNotification("Generating PDF", true);

/*                // Delete charts from sdcard once the Pdf has been generated
                for (int i = 0; i < calendar.getActualMaximum(Calendar.MONTH); i++) {

                    String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

                    publishNotification("Deleting chart for " + monthName, true);

                    if (!mLineChart.deleteFromPath("line_chart_" + i, "")) {
                        Toast.makeText(ExportService.this, "Failed to save chart for " + monthName, Toast.LENGTH_SHORT).show();
                    }
                }*/

                publishNotification("Export finished", false);
            }
        });
    }

    private void publishNotification(String message, boolean onGoing) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ExportService.this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setOngoing(onGoing)
                .setContentTitle("Exporting to PDF")
                .setContentText(message);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

}
