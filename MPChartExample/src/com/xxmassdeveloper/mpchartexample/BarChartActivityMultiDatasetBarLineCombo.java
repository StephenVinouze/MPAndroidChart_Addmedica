
package com.xxmassdeveloper.mpchartexample;

import android.Manifest;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.util.ArrayList;
import java.util.List;

public class BarChartActivityMultiDatasetBarLineCombo extends DemoBase implements OnChartValueSelectedListener, OnChartGestureListener {

    private static final int WRITE_STORAGE_PERMISSION_CODE = 110;
    private static final int NUMBER_OF_DAYS = 100;

    private BarChart mBarChart;
    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_multiset_bar_line_combo);

        mBarChart = (BarChart) findViewById(R.id.bar_chart);
        mBarChart.setOnChartValueSelectedListener(this);
        mBarChart.setDescription("");
        mBarChart.setPinchZoom(false);
        mBarChart.setScaleYEnabled(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setOnChartGestureListener(this);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // define an offset to change the original position of the marker
        // (optional)
        // mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());

        // set the marker to the chart
        mBarChart.setMarkerView(mv);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        Legend l = mBarChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);
        l.setTypeface(tf);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xl = mBarChart.getXAxis();
        xl.setTypeface(tf);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
/*        xl.setDrawGridLines(false);
        xl.setDrawAxisLine(false);*/

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        mBarChart.getAxisRight().setEnabled(false);

        mBarChart.setData(generateBarData());
        mBarChart.setVisibleXRange(4 * 7 - 1, 4 * 30 - 1);
        mBarChart.zoom(NUMBER_OF_DAYS / 7, 1, 0, 0);
        mBarChart.centerViewTo(mBarChart.getXChartMax(), 0, YAxis.AxisDependency.LEFT);
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

    private List<IBarDataSet> getBarDataSet() {
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

        return dataSets;
    }

    private BarData generateBarData() {
        BarData data = new BarData(getXvals(), getBarDataSet());
        data.setValueFormatter(new LargeValueFormatter());
        data.setGroupSpace(80f);
        data.setValueTypeface(tf);

        return data;
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
/*            case R.id.actionToggleValues: {
                for (IBarDataSet set : mBarChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mBarChart.invalidate();
                break;
            }*/
            case R.id.actionSave: {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_PERMISSION_CODE);
                }
                else {
                    save();
                }

                break;
            }
            case R.id.animateY: {
                mBarChart.animateY(1000);
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

    }

    @Override
    public void onChartMaxScale(MotionEvent me, ChartTouchListener.ChartGesture gesture) {
        if (gesture == ChartTouchListener.ChartGesture.X_ZOOM)
            Log.d("scale", "switch to pie");
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {}

}
