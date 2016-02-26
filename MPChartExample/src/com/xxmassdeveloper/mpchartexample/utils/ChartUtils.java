package com.xxmassdeveloper.mpchartexample.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.xxmassdeveloper.mpchartexample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephenvinouze on 26/02/16.
 */
public class ChartUtils {

    private static final int MAX_EFFORT_VALUE = 10;
    private static final int MAX_PAIN_VALUE = 21;

    public enum ChartMode { LIGHT, DARK }

    @TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    public static BarLineChartBase configureChart(BarLineChartBase chart, ChartMode chartMode, Context context, OnChartGestureListener gestureListener) {
        chart.setDescription(null);
        chart.setScaleXEnabled(true);
        chart.setScaleYEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setBackgroundColor(ContextCompat.getColor(context, (chartMode == ChartMode.DARK) ? R.color.bg_dark : R.color.bg_light));
        chart.setOnChartGestureListener(gestureListener);

        configureLegend(context, chartMode, chart.getLegend());
        configureXAxis(context, chartMode, chart.getXAxis());
        configureYAxis(context, chartMode, chart.getAxisLeft(), MAX_EFFORT_VALUE);
        configureYAxis(context, chartMode, chart.getAxisRight(), MAX_PAIN_VALUE);

        return chart;
    }

    public static float getBarUnitValue(BarChart barChart) {
        BarData barData = barChart.getBarData();
        return barData.getDataSetCount() + barData.getGroupSpace();
    }

    public static LineData generateLineData(Context context, List<Entry> lineEntries1, List<Entry> lineEntries2, List<Entry> lineEntries3, List<String> xValues, ChartMode chartMode) {
        LineDataSet set1 = new LineDataSet(lineEntries1, "Effort");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ContextCompat.getColor(context, R.color.yellow));
        set1.setCircleColor(ContextCompat.getColor(context, R.color.yellow));
        set1.setCircleRadius(4f);

        LineDataSet set2 = new LineDataSet(lineEntries2, "Fatigue");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(ContextCompat.getColor(context, R.color.blue));
        set2.setCircleColor(ContextCompat.getColor(context, R.color.blue));
        set2.setCircleRadius(4f);

        LineDataSet set3 = new LineDataSet(lineEntries3, "Douleur");
        set3.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set3.setColor(ContextCompat.getColor(context, R.color.red));
        set3.setCircleColor(ContextCompat.getColor(context, R.color.red));
        set3.setCircleRadius(4f);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        LineData data = new LineData(xValues, dataSets);
        data.setDrawValues(false);
        data.setValueTextColor(getDefaultTextColor(chartMode));

        return data;
    }

    public static int getRandom(int minValue, int maxValue) {
        return (int) (Math.random() * maxValue) + minValue;
    }

    public static BarData generateBarData(Context context, List<BarEntry> barEntries1, List<BarEntry> barEntries2, List<BarEntry> barEntries3, List<String> xValues, ChartMode chartMode) {
        BarDataSet set1 = new BarDataSet(barEntries1, "Effort");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ContextCompat.getColor(context, R.color.yellow));

        BarDataSet set2 = new BarDataSet(barEntries2, "Fatigue");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(ContextCompat.getColor(context, R.color.blue));

        BarDataSet set3 = new BarDataSet(barEntries3, "Douleur");
        set3.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set3.setColor(ContextCompat.getColor(context, R.color.red));

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        BarData data = new BarData(xValues, dataSets);
        data.setGroupSpace(200);
        data.setDrawValues(false);
        data.setValueTextColor(getDefaultTextColor(chartMode));

        return data;
    }

    private static void configureXAxis(Context context, ChartMode chartMode, XAxis axis) {
        axis.setTypeface(getTypeface(context));
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setDrawGridLines(false);
        axis.setDrawAxisLine(false);
        axis.setTextColor(getDefaultTextColor(chartMode));
    }

    private static void configureYAxis(Context context, ChartMode chartMode, YAxis axis, int maxValue) {
        YAxis.AxisDependency dependency = axis.getAxisDependency();

        axis.setTypeface(getTypeface(context));
        axis.setValueFormatter(new LargeValueFormatter());
        axis.setDrawGridLines(dependency == YAxis.AxisDependency.LEFT);
        axis.setDrawAxisLine(false);
        axis.setTextColor(dependency == YAxis.AxisDependency.LEFT ? getDefaultTextColor(chartMode) : ContextCompat.getColor(context, R.color.red));
        axis.setAxisMinValue(0);
        axis.setAxisMaxValue(maxValue);
    }

    private static void configureLegend(Context context, ChartMode chartMode, Legend legend) {
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTypeface(getTypeface(context));
        legend.setYOffset(0f);
        legend.setYEntrySpace(0f);
        legend.setXEntrySpace(20f);
        legend.setTextColor(getDefaultTextColor(chartMode));
        legend.setTextSize(15f);
        legend.setFormSize(15f);
    }

    private static Typeface getTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
    }

    private static int getDefaultTextColor(ChartMode chartMode) {
        return (chartMode == ChartMode.DARK) ? Color.WHITE : Color.BLACK;
    }

}
