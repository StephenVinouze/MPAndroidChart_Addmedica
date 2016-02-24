
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
        configureLegend(mBarChart.getLegend());
        configureXAxis(mBarChart.getXAxis());
        configureYAxis(mBarChart.getAxisLeft());

        mBarChart.setData(generateBarData());
        mBarChart.setVisibleXRange(4 * 7 - 1, 4 * 30 - 1);
        mBarChart.zoom(NUMBER_OF_DAYS / 7, 1, 0, 0);
        mBarChart.centerViewTo(mBarChart.getXChartMax(), 0, YAxis.AxisDependency.LEFT);

        mLineChart = (LineChart) findViewById(R.id.line_chart);
        configureChart(mLineChart);
        configureLegend(mLineChart.getLegend());
        configureXAxis(mLineChart.getXAxis());
        configureYAxis(mLineChart.getAxisLeft());

        mLineChart.setData(generateLineData());
        mLineChart.setVisibleXRange(4 * 7 - 1, 4 * 30 - 1);
        mLineChart.zoom(NUMBER_OF_DAYS / 7, 1, 0, 0);
        mLineChart.centerViewTo(mLineChart.getXChartMax(), 0, YAxis.AxisDependency.LEFT);

        showBarChart();
    }

    @TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    private BarLineChartBase configureChart(BarLineChartBase chart) {
        chart.setDescription("");
        chart.setScaleXEnabled(true);
        chart.setScaleYEnabled(false);
        chart.setDrawGridBackground(false);
        chart.getAxisRight().setEnabled(false);
        chart.setOnChartGestureListener(this);
        chart.setOnChartValueSelectedListener(this);

        return chart;
    }

    private void configureXAxis(XAxis axis) {
        axis.setTypeface(tf);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setDrawGridLines(false);
        axis.setDrawAxisLine(false);
    }

    private void configureYAxis(YAxis axis) {
        axis.setTypeface(tf);
        axis.setValueFormatter(new LargeValueFormatter());
        axis.setDrawGridLines(false);
        axis.setSpaceTop(30f);
        axis.setAxisMinValue(0f);
    }

    private void configureLegend(Legend legend) {
        legend.setPosition(LegendPosition.ABOVE_CHART_RIGHT);
        legend.setTypeface(tf);
        legend.setYOffset(0f);
        legend.setYEntrySpace(0f);
        legend.setXEntrySpace(10f);
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

    private List<Entry> getLineEntries() {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_DAYS; i++) {
            entries.add(new Entry(getRandom(10, 0), i));
        }
        return entries;
    }

    private List<BarEntry> getBarEntries() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_DAYS; i++) {
            entries.add(new BarEntry(getRandom(10, 0), i));
        }
        return entries;
    }

    private LineData generateLineData() {
        LineDataSet set1 = new LineDataSet(getLineEntries(), "Effort");
        set1.setColor(Color.rgb(104, 241, 175));
        LineDataSet set2 = new LineDataSet(getLineEntries(), "Fatigue");
        set2.setColor(Color.rgb(164, 228, 251));
        LineDataSet set3 = new LineDataSet(getLineEntries(), "Douleur");
        set3.setColor(Color.rgb(242, 247, 158));

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        LineData data = new LineData(getXvals(), dataSets);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTypeface(tf);

        return data;
    }

    private BarData generateBarData() {
        BarDataSet set1 = new BarDataSet(getBarEntries(), "Effort");
        set1.setColor(Color.rgb(104, 241, 175));
        BarDataSet set2 = new BarDataSet(getBarEntries(), "Fatigue");
        set2.setColor(Color.rgb(164, 228, 251));
        BarDataSet set3 = new BarDataSet(getBarEntries(), "Douleur");
        set3.setColor(Color.rgb(242, 247, 158));

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        BarData data = new BarData(getXvals(), dataSets);
        data.setValueFormatter(new LargeValueFormatter());
        data.setGroupSpace(80f);
        data.setValueTypeface(tf);

        return data;
    }

    @TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB_MR1)
    private boolean switchChart(BarLineChartBase fromChart, BarLineChartBase toChart) {
        if (!isSwitching && fromChart.getAlpha() == 1) {
            isSwitching = true;
            fromChart.setAlpha(0);
            toChart.setAlpha(1);
            toChart.centerViewTo((fromChart.getLowestVisibleXIndex() + fromChart.getHighestVisibleXIndex()) / 2, 0, YAxis.AxisDependency.LEFT);
            toChart.bringToFront();
            return true;
        }
        return false;
    }

    private void showBarChart() {
        if (switchChart(mLineChart, mBarChart)) {
            mBarChart.animateY(500);
            isSwitching = false;
        }
    }

    private void showLineChart() {
        if (switchChart(mBarChart, mLineChart)) {
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
                // If request is cancelled, the result arrays are empty.
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
