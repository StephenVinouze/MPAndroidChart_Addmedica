
package com.xxmassdeveloper.mpchartexample;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.util.ArrayList;
import java.util.List;

public class BarChartActivityMultiDatasetBarLineCombo extends DemoBase implements OnChartValueSelectedListener, OnChartGestureListener {

    private static final int WRITE_STORAGE_PERMISSION_CODE = 110;
    private static final int NUMBER_OF_DAYS = 100;
    private static final int MINIMUM_VISIBLE_DAYS = 5;
    private static final int MAXIMUM_VISIBLE_DAYS = 15;
    private static final int SETS = 3;

    private static final int MAX_EFFORT_VALUE = 10;
    private static final int MAX_PAIN_VALUE = 21;

    private BarChart mBarChart;
    private LineChart mLineChart;
    private Typeface tf;
    private boolean isSwitching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_multiset_bar_line_combo);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mBarChart = (BarChart) findViewById(R.id.bar_chart);
        configureChart(mBarChart);

        mBarChart.setData(generateBarData());
        mBarChart.setVisibleXRange((SETS + 1) * MINIMUM_VISIBLE_DAYS - 1, (SETS + 1) * MAXIMUM_VISIBLE_DAYS - 1);
        mBarChart.zoom(NUMBER_OF_DAYS / MINIMUM_VISIBLE_DAYS, 1, 0, 0);

        mLineChart = (LineChart) findViewById(R.id.line_chart);
        configureChart(mLineChart);

        mLineChart.setData(generateLineData());
        mLineChart.setVisibleXRange(MAXIMUM_VISIBLE_DAYS, MAXIMUM_VISIBLE_DAYS * 2);
        mLineChart.zoom(NUMBER_OF_DAYS / MAXIMUM_VISIBLE_DAYS, 1, 0, 0);

        showBarChart();

        mBarChart.moveViewToX(mBarChart.getXChartMax());
    }

    @TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    private BarLineChartBase configureChart(BarLineChartBase chart) {
        chart.setDescription("");
        chart.setScaleXEnabled(true);
        chart.setScaleYEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setOnChartGestureListener(this);
        chart.setOnChartValueSelectedListener(this);
        chart.setBackgroundColor(ContextCompat.getColor(this, R.color.bg));

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
        axis.setTextColor(Color.WHITE);
    }

    private void configureYAxis(YAxis axis, int maxValue) {
        YAxis.AxisDependency dependency = axis.getAxisDependency();

        axis.setTypeface(tf);
        axis.setValueFormatter(new LargeValueFormatter());
        axis.setDrawGridLines(dependency == YAxis.AxisDependency.LEFT);
        axis.setDrawAxisLine(false);
        axis.setTextColor(dependency == YAxis.AxisDependency.LEFT ? Color.WHITE : ContextCompat.getColor(this, R.color.red));
        axis.setAxisMinValue(0);
        axis.setAxisMaxValue(maxValue);
    }

    private void configureLegend(Legend legend) {
        legend.setPosition(LegendPosition.ABOVE_CHART_RIGHT);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTypeface(tf);
        legend.setYOffset(0f);
        legend.setYEntrySpace(0f);
        legend.setXEntrySpace(20f);
        legend.setTextColor(Color.WHITE);
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

    private List<BarEntry> getBarEntries(int maxValue) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_DAYS; i++) {
            entries.add(new BarEntry(getRandom(maxValue, 0), i));
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

    private BarData generateBarData() {
        BarDataSet set1 = new BarDataSet(getBarEntries(MAX_EFFORT_VALUE), "Effort");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ContextCompat.getColor(this, R.color.yellow));

        BarDataSet set2 = new BarDataSet(getBarEntries(MAX_EFFORT_VALUE), "Fatigue");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(ContextCompat.getColor(this, R.color.blue));

        BarDataSet set3 = new BarDataSet(getBarEntries(MAX_PAIN_VALUE), "Douleur");
        set3.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set3.setColor(ContextCompat.getColor(this, R.color.red));

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        BarData data = new BarData(getXvals(), dataSets);
        data.setGroupSpace(80f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        return data;
    }

    @TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB_MR1)
    private boolean switchChart(BarLineChartBase fromChart, BarLineChartBase toChart) {
        if (!isSwitching && fromChart.getAlpha() == 1) {
            isSwitching = true;
            fromChart.setAlpha(0);
            toChart.bringToFront();
            toChart.setAlpha(1);
            return true;
        }
        return false;
    }

    private void showBarChart() {
        if (switchChart(mLineChart, mBarChart)) {
            mBarChart.moveViewToX((SETS + 1) * mLineChart.getLowestVisibleXIndex() - 1);
            mBarChart.animateY(500);
            isSwitching = false;
        }
    }

    private void showLineChart() {
        if (switchChart(mBarChart, mLineChart)) {
            mLineChart.moveViewToX(mBarChart.getLowestVisibleXIndex());
            mLineChart.animateX(1000);
            isSwitching = false;
        }
    }

    private void save() {
        mBarChart.saveToPath("title" + System.currentTimeMillis(), "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_with_pie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IBarDataSet set : mBarChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mBarChart.invalidate();
                break;
            }
            case R.id.animateX: {
                mLineChart.animateX(3000);
                break;
            }
            case R.id.actionSave: {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_PERMISSION_CODE);
                }
                else {
                    save();
                }

                break;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_STORAGE_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    save();
                }
            }
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {}

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {}

    @Override
    public void onChartLongPressed(MotionEvent me) {}

    @Override
    public void onChartDoubleTapped(MotionEvent me) {}

    @Override
    public void onChartSingleTapped(MotionEvent me) {}

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {}

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {}

    @Override
    public void onChartMinScale(MotionEvent me, ChartTouchListener.ChartGesture gesture) {
        if (gesture == ChartTouchListener.ChartGesture.X_ZOOM) {
            showBarChart();
        }
    }

    @Override
    public void onChartMaxScale(MotionEvent me, ChartTouchListener.ChartGesture gesture) {
        if (gesture == ChartTouchListener.ChartGesture.X_ZOOM) {
            showLineChart();
        }
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {}

}
