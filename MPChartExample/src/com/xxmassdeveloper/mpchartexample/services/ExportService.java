package com.xxmassdeveloper.mpchartexample.services;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.xxmassdeveloper.mpchartexample.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by stephenvinouze on 25/02/16.
 */
public class ExportService extends Service {

    private static final int DAYS_IN_MONTH = 31;
    private static final int MAX_EFFORT_VALUE = 10;
    private static final int MAX_PAIN_VALUE = 21;
    private static final int NOTIFICATION_ID = 100;

    private final IBinder binder = new ExportBinder();

    private Typeface tf;
    private LineChart mLineChart;

    private static Handler backgroundHandler;

    public static Handler getBackgroundHandler() {
        if (backgroundHandler == null) {
            HandlerThread printerThread = new HandlerThread("ExportThread");
            printerThread.start();
            backgroundHandler = new Handler(printerThread.getLooper());
        }
        return backgroundHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mLineChart = new LineChart(this);
        configureChart(mLineChart);
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
                publishNotification("Export started", true);

                // Generate charts for the last 6 months and save them to sdcard
                for (int i = 0; i < 6; i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MONTH, -i);

                    publishNotification("Generating chart for " + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()), true);

                    mLineChart.setData(generateLineData(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)));
                    mLineChart.invalidate();
                    mLineChart.saveUnattachedChartToPath("line_chart_" + i, "", 1800, 1000, 50, ContextCompat.getColor(ExportService.this, R.color.bg_light));
                }

                // Generate Pdf using the generated charts
                // Remember to delete charts from sdcard once the Pdf has been generated

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

    @TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    private BarLineChartBase configureChart(BarLineChartBase chart) {
        chart.setDescription("");
        chart.setScaleXEnabled(true);
        chart.setScaleYEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_dark));

        configureLegend(chart.getLegend());
        configureXAxis(chart.getXAxis());
        configureYAxis(chart.getAxisLeft(), MAX_EFFORT_VALUE);
        configureYAxis(chart.getAxisRight(), MAX_PAIN_VALUE);

        return chart;
    }

    private void configureXAxis(XAxis axis) {
        axis.setTypeface(tf);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setDrawGridLines(false);
        axis.setDrawAxisLine(false);
        axis.setTextColor(Color.BLACK);
    }

    private void configureYAxis(YAxis axis, int maxValue) {
        YAxis.AxisDependency dependency = axis.getAxisDependency();

        axis.setTypeface(tf);
        axis.setValueFormatter(new LargeValueFormatter());
        axis.setDrawGridLines(dependency == YAxis.AxisDependency.LEFT);
        axis.setDrawAxisLine(false);
        axis.setTextColor(dependency == YAxis.AxisDependency.LEFT ? Color.BLACK : ContextCompat.getColor(this, R.color.red));
        axis.setAxisMinValue(0);
        axis.setAxisMaxValue(maxValue);
    }

    private void configureLegend(Legend legend) {
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTypeface(tf);
        legend.setYOffset(0f);
        legend.setYEntrySpace(0f);
        legend.setXEntrySpace(20f);
        legend.setTextColor(Color.BLACK);
        legend.setTextSize(15f);
        legend.setFormSize(15f);
    }

    private int getRandom(int range, int startsfrom) {
        return (int) (Math.random() * range) + startsfrom;
    }

    private List<String> getXvals(int dayInMonth) {
        List<String> xVals = new ArrayList<>();
        for (int i = 0; i < dayInMonth; i++) {
            xVals.add(String.format("%02d", i + 1));
        }
        return xVals;
    }

    private List<Entry> getLineEntries(int maxValue, int dayInMonth) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dayInMonth; i++) {
            entries.add(new Entry(getRandom(maxValue, 0), i));
        }
        return entries;
    }

    private LineData generateLineData(int dayInMonth) {
        LineDataSet set1 = new LineDataSet(getLineEntries(MAX_EFFORT_VALUE, dayInMonth), "Effort");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ContextCompat.getColor(this, R.color.yellow));
        set1.setCircleColor(ContextCompat.getColor(this, R.color.yellow));
        set1.setCircleRadius(4f);

        LineDataSet set2 = new LineDataSet(getLineEntries(MAX_EFFORT_VALUE, dayInMonth), "Fatigue");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(ContextCompat.getColor(this, R.color.blue));
        set2.setCircleColor(ContextCompat.getColor(this, R.color.blue));
        set2.setCircleRadius(4f);

        LineDataSet set3 = new LineDataSet(getLineEntries(MAX_PAIN_VALUE, dayInMonth), "Douleur");
        set3.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set3.setColor(ContextCompat.getColor(this, R.color.red));
        set3.setCircleColor(ContextCompat.getColor(this, R.color.red));
        set3.setCircleRadius(4f);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        LineData data = new LineData(getXvals(dayInMonth), dataSets);
        data.setDrawValues(false);
        data.setValueTextColor(Color.BLACK);

        return data;
    }

    public class ExportBinder extends Binder {
        public ExportService getService() {
            return ExportService.this;
        }
    }

}
