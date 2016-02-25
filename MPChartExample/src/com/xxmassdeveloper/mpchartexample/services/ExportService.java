package com.xxmassdeveloper.mpchartexample.services;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
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
import java.util.List;

/**
 * Created by stephenvinouze on 25/02/16.
 */
public class ExportService extends Service {

    private static final int NUMBER_OF_DAYS = 100;
    private static final int MINIMUM_VISIBLE_DAYS = 5;
    private static final int MAXIMUM_VISIBLE_DAYS = 15;

    private static final int MAX_EFFORT_VALUE = 10;
    private static final int MAX_PAIN_VALUE = 21;

    private final IBinder binder = new ExportBinder();

    private LineChart mLineChart;
    private Typeface tf;

    private static Handler backgroundHandler;

    public static Handler getBackgroundHandler() {
        if (backgroundHandler == null) {
            HandlerThread printerThread = new HandlerThread("PrinterThread");
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

        mLineChart.setData(generateLineData());
        mLineChart.setVisibleXRange(MAXIMUM_VISIBLE_DAYS, MAXIMUM_VISIBLE_DAYS * 2);
        mLineChart.zoom(NUMBER_OF_DAYS / MAXIMUM_VISIBLE_DAYS, 1, 0, 0);
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

    public void saveChart() {
        mLineChart.saveUnattachedChartToPath("line_chart", "", 1800, 1000, 50, ContextCompat.getColor(this, R.color.red));
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

    private List<String> getXvals() {
        List<String> xVals = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_DAYS; i++) {
            xVals.add((i + 1) + "");
        }
        return xVals;
    }

    private List<Entry> getLineEntries(int maxValue) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_DAYS; i++) {
            entries.add(new Entry(getRandom(maxValue, 0), i));
        }
        return entries;
    }

    private LineData generateLineData() {
        LineDataSet set1 = new LineDataSet(getLineEntries(MAX_EFFORT_VALUE), "Effort");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ContextCompat.getColor(this, R.color.yellow));
        set1.setCircleColor(ContextCompat.getColor(this, R.color.yellow));
        set1.setCircleRadius(4f);

        LineDataSet set2 = new LineDataSet(getLineEntries(MAX_EFFORT_VALUE), "Fatigue");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(ContextCompat.getColor(this, R.color.blue));
        set2.setCircleColor(ContextCompat.getColor(this, R.color.blue));
        set2.setCircleRadius(4f);

        LineDataSet set3 = new LineDataSet(getLineEntries(MAX_PAIN_VALUE), "Douleur");
        set3.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set3.setColor(ContextCompat.getColor(this, R.color.red));
        set3.setCircleColor(ContextCompat.getColor(this, R.color.red));
        set3.setCircleRadius(4f);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        LineData data = new LineData(getXvals(), dataSets);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        return data;
    }

    public class ExportBinder extends Binder {
        public ExportService getService() {
            return ExportService.this;
        }
    }

}
