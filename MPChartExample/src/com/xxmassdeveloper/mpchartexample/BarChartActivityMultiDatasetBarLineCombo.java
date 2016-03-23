
package com.xxmassdeveloper.mpchartexample;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;
import com.xxmassdeveloper.mpchartexample.utils.ChartUtils;

import java.util.ArrayList;
import java.util.List;

public class BarChartActivityMultiDatasetBarLineCombo extends DemoBase implements OnChartValueSelectedListener, OnChartGestureListener {

    private static final int WRITE_STORAGE_PERMISSION_CODE = 110;
    private static final int NUMBER_OF_DAYS = 100;
    private static final int MINIMUM_VISIBLE_DAYS = 5;
    private static final int MAXIMUM_VISIBLE_DAYS = 15;

    private static final int MAX_EFFORT_VALUE = 10;
    private static final int MAX_PAIN_VALUE = 21;

    private BarChart mBarChart;
    private LineChart mLineChart;
    private boolean isSwitching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiset_bar_line_combo);

        List<Entry> lineEffortEntries = new ArrayList<>();
        List<Entry> lineExhaustionEntries = new ArrayList<>();
        List<Entry> linePainEntries = new ArrayList<>();
        List<BarEntry> barEffortEntries = new ArrayList<>();
        List<BarEntry> barExhaustionEntries = new ArrayList<>();
        List<BarEntry> barPainEntries = new ArrayList<>();
        List<String> xValues = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_DAYS; i++) {
            int effortValue = ChartUtils.getRandom(0, MAX_EFFORT_VALUE);
            int exhaustionValue = ChartUtils.getRandom(0, MAX_EFFORT_VALUE);
            int painValue = ChartUtils.getRandom(0, MAX_PAIN_VALUE);

            lineEffortEntries.add(new Entry(effortValue, i));
            lineExhaustionEntries.add(new Entry(exhaustionValue, i));
            linePainEntries.add(new Entry(painValue, i));

            barEffortEntries.add(new BarEntry(effortValue, i));
            barExhaustionEntries.add(new BarEntry(exhaustionValue, i));
            barPainEntries.add(new BarEntry(painValue, i));

            xValues.add(String.format("%02d", i + 1));
        }

        mBarChart = (BarChart) findViewById(R.id.bar_chart);
        ChartUtils.configureChart(mBarChart, ChartUtils.ChartMode.DARK, this, this);

        mBarChart.setData(ChartUtils.generateBarData(this, barEffortEntries, barExhaustionEntries, barPainEntries, xValues, ChartUtils.ChartMode.DARK));
        mBarChart.setVisibleXRange(ChartUtils.getBarUnitValue(mBarChart) * MINIMUM_VISIBLE_DAYS , ChartUtils.getBarUnitValue(mBarChart) * MAXIMUM_VISIBLE_DAYS);
        mBarChart.zoom(NUMBER_OF_DAYS / MINIMUM_VISIBLE_DAYS, 1, 0, 0);

        mLineChart = (LineChart) findViewById(R.id.line_chart);
        ChartUtils.configureChart(mLineChart, ChartUtils.ChartMode.DARK, this, this);

        mLineChart.setData(ChartUtils.generateLineData(this, lineEffortEntries, lineExhaustionEntries, linePainEntries, xValues, ChartUtils.ChartMode.DARK));
        mLineChart.setVisibleXRange(MAXIMUM_VISIBLE_DAYS, MAXIMUM_VISIBLE_DAYS * 2);
        mLineChart.zoom(NUMBER_OF_DAYS / MAXIMUM_VISIBLE_DAYS, 1, 0, 0);

        showBarChart();

        mBarChart.moveViewToX(mBarChart.getXChartMax());
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
            mBarChart.moveViewToX(ChartUtils.getBarUnitValue(mBarChart) * mLineChart.getLowestVisibleXIndex());
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
        getMenuInflater().inflate(R.menu.bar_with_line, menu);
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
