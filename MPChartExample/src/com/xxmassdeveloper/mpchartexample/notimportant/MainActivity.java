
package com.xxmassdeveloper.mpchartexample.notimportant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.github.mikephil.charting.utils.Utils;
import com.xxmassdeveloper.mpchartexample.AnotherBarActivity;
import com.xxmassdeveloper.mpchartexample.BarChartActivity;
import com.xxmassdeveloper.mpchartexample.BarChartActivityMultiDataset;
import com.xxmassdeveloper.mpchartexample.BarChartActivityMultiDatasetBarLineCombo;
import com.xxmassdeveloper.mpchartexample.BarChartActivitySinus;
import com.xxmassdeveloper.mpchartexample.BarChartPositiveNegative;
import com.xxmassdeveloper.mpchartexample.BubbleChartActivity;
import com.xxmassdeveloper.mpchartexample.CandleStickChartActivity;
import com.xxmassdeveloper.mpchartexample.CombinedChartActivity;
import com.xxmassdeveloper.mpchartexample.CubicLineChartActivity;
import com.xxmassdeveloper.mpchartexample.DynamicalAddingActivity;
import com.xxmassdeveloper.mpchartexample.ExportChartActivity;
import com.xxmassdeveloper.mpchartexample.HorizontalBarChartActivity;
import com.xxmassdeveloper.mpchartexample.InvertedLineChartActivity;
import com.xxmassdeveloper.mpchartexample.LineChartActivity1;
import com.xxmassdeveloper.mpchartexample.LineChartActivity2;
import com.xxmassdeveloper.mpchartexample.LineChartActivityColored;
import com.xxmassdeveloper.mpchartexample.ListViewBarChartActivity;
import com.xxmassdeveloper.mpchartexample.ListViewMultiChartActivity;
import com.xxmassdeveloper.mpchartexample.MultiLineChartActivity;
import com.xxmassdeveloper.mpchartexample.PerformanceLineChart;
import com.xxmassdeveloper.mpchartexample.PieChartActivity;
import com.xxmassdeveloper.mpchartexample.R;
import com.xxmassdeveloper.mpchartexample.RadarChartActivitry;
import com.xxmassdeveloper.mpchartexample.RealtimeLineChartActivity;
import com.xxmassdeveloper.mpchartexample.ScatterChartActivity;
import com.xxmassdeveloper.mpchartexample.ScrollViewActivity;
import com.xxmassdeveloper.mpchartexample.StackedBarActivity;
import com.xxmassdeveloper.mpchartexample.StackedBarActivityNegative;
import com.xxmassdeveloper.mpchartexample.fragments.SimpleChartDemo;
import com.xxmassdeveloper.mpchartexample.realm.RealmMainActivity;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        setTitle("MPAndroidChart Example");

        // initialize the utilities
        Utils.init(this);

        ArrayList<ContentItem> objects = new ArrayList<ContentItem>();

        objects.add(new ContentItem("Line Chart", "A simple demonstration of the linechart."));
        objects.add(new ContentItem("Line Chart (Dual YAxis)",
                "Demonstration of the linechart with dual y-axis."));
        objects.add(new ContentItem("Bar Chart", "A simple demonstration of the bar chart."));
        objects.add(new ContentItem("Horizontal Bar Chart",
                "A simple demonstration of the horizontal bar chart."));
        objects.add(new ContentItem("Combined Chart",
                "Demonstrates how to create a combined chart (bar and line in this case)."));
        objects.add(new ContentItem("Pie Chart", "A simple demonstration of the pie chart."));
        objects.add(new ContentItem("Scatter Chart", "A simple demonstration of the scatter chart."));
        objects.add(new ContentItem("Bubble Chart", "A simple demonstration of the bubble chart."));
        objects.add(new ContentItem("Stacked Bar Chart",
                "A simple demonstration of a bar chart with stacked bars."));
        objects.add(new ContentItem("Stacked Bar Chart Negative",
                "A simple demonstration of stacked bars with negative and positive values."));
        objects.add(new ContentItem("Another Bar Chart",
                "Implementation of a BarChart that only shows values at the bottom."));
        objects.add(new ContentItem("Multiple Lines Chart",
                "A line chart with multiple DataSet objects. One color per DataSet."));
        objects.add(new ContentItem("Multiple Bars Chart",
                "A bar chart with multiple DataSet objects. One multiple colors per DataSet."));
        objects.add(new ContentItem(
                "Charts in ViewPager Fragments",
                "Demonstration of charts inside ViewPager Fragments. In this example the focus was on the design and look and feel of the chart."));
        objects.add(new ContentItem(
                "BarChart inside ListView",
                "Demonstrates the usage of a BarChart inside a ListView item."));
        objects.add(new ContentItem(
                "Multiple charts inside ListView",
                "Demonstrates the usage of different chart types inside a ListView."));
        objects.add(new ContentItem(
                "Inverted Line Chart",
                "Demonstrates the feature of inverting the y-axis."));
        objects.add(new ContentItem(
                "Candle Stick Chart",
                "Demonstrates usage of the CandleStickChart."));
        objects.add(new ContentItem(
                "Cubic Line Chart",
                "Demonstrates cubic lines in a LineChart."));
        objects.add(new ContentItem(
                "Radar Chart",
                "Demonstrates the use of a spider-web like (net) chart."));
        objects.add(new ContentItem(
                "Colored Line Chart",
                "Shows a LineChart with different background and line color."));
        objects.add(new ContentItem(
                "Realtime Chart",
                "This chart is fed with new data in realtime. It also restrains the view on the x-axis."));
        objects.add(new ContentItem(
                "Dynamical data adding",
                "This Activity demonstrates dynamical adding of Entries and DataSets (real time graph)."));
        objects.add(new ContentItem(
                "Performance Line Chart",
                "Renders up to 30.000 objects smoothly."));
        objects.add(new ContentItem(
                "Sinus Bar Chart",
                "A Bar Chart plotting the sinus function with 8.000 values."));
        objects.add(new ContentItem(
                "Chart in ScrollView",
                "This demonstrates how to use a chart inside a ScrollView."));
        objects.add(new ContentItem(
                "BarChart positive / negative",
                "This demonstrates how to create a BarChart with positive and negative values in different colors."));

        ContentItem realm = new ContentItem(
                "Realm.io Database",
                "This demonstrates how to use this library with Realm.io mobile database.");
        realm.isNew = true;
        objects.add(realm);

        ContentItem bar_and_line_charts = new ContentItem("Multiple Bars chart with switch in Line chart",
                "A bar chart with multiple DataSet that switches to line chart after scaling to a defined threshold");
        bar_and_line_charts.isNew = true;
        objects.add(bar_and_line_charts);

        ContentItem export_charts = new ContentItem("Export bar charts",
                "Line charts generated on-the-fly and saved in sdcard but never displayed to the user.");
        export_charts.isNew = true;
        objects.add(export_charts);

        MyAdapter adapter = new MyAdapter(this, objects);

        ListView lv = (ListView) findViewById(R.id.listView1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {

        Intent i = null;

        switch (pos) {
            case 0:
                i = new Intent(this, LineChartActivity1.class);
                break;
            case 1:
                i = new Intent(this, LineChartActivity2.class);
                break;
            case 2:
                i = new Intent(this, BarChartActivity.class);
                break;
            case 3:
                i = new Intent(this, HorizontalBarChartActivity.class);
                break;
            case 4:
                i = new Intent(this, CombinedChartActivity.class);
                break;
            case 5:
                i = new Intent(this, PieChartActivity.class);
                break;
            case 6:
                i = new Intent(this, ScatterChartActivity.class);
                break;
            case 7:
                i = new Intent(this, BubbleChartActivity.class);
                break;
            case 8:
                i = new Intent(this, StackedBarActivity.class);
                break;
            case 9:
                i = new Intent(this, StackedBarActivityNegative.class);
                break;
            case 10:
                i = new Intent(this, AnotherBarActivity.class);
                break;
            case 11:
                i = new Intent(this, MultiLineChartActivity.class);
                break;
            case 12:
                i = new Intent(this, BarChartActivityMultiDataset.class);
                break;
            case 13:
                i = new Intent(this, SimpleChartDemo.class);
                break;
            case 14:
                i = new Intent(this, ListViewBarChartActivity.class);
                break;
            case 15:
                i = new Intent(this, ListViewMultiChartActivity.class);
                break;
            case 16:
                i = new Intent(this, InvertedLineChartActivity.class);
                break;
            case 17:
                i = new Intent(this, CandleStickChartActivity.class);
                break;
            case 18:
                i = new Intent(this, CubicLineChartActivity.class);
                break;
            case 19:
                i = new Intent(this, RadarChartActivitry.class);
                break;
            case 20:
                i = new Intent(this, LineChartActivityColored.class);
                break;
            case 21:
                i = new Intent(this, RealtimeLineChartActivity.class);
                break;
            case 22:
                i = new Intent(this, DynamicalAddingActivity.class);
                break;
            case 23:
                i = new Intent(this, PerformanceLineChart.class);
                break;
            case 24:
                i = new Intent(this, BarChartActivitySinus.class);
                break;
            case 25:
                i = new Intent(this, ScrollViewActivity.class);
                break;
            case 26:
                i = new Intent(this, BarChartPositiveNegative.class);
                break;
            case 27:
                i = new Intent(this, RealmMainActivity.class);
                break;
            case 28:
                i = new Intent(this, BarChartActivityMultiDatasetBarLineCombo.class);
                break;
            case 29:
                i = new Intent(this, ExportChartActivity.class);
                break;
        }

        if (i != null)
            startActivity(i);

        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i = null;

        switch (item.getItemId()) {
            case R.id.viewGithub:
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://github.com/PhilJay/MPAndroidChart"));
                startActivity(i);
                break;
            case R.id.report:
                i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "philjay.librarysup@gmail.com", null));
                i.putExtra(Intent.EXTRA_SUBJECT, "MPAndroidChart Issue");
                i.putExtra(Intent.EXTRA_TEXT, "Your error report here...");
                startActivity(Intent.createChooser(i, "Report Problem"));
                break;
            case R.id.blog:
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.xxmassdeveloper.com"));
                startActivity(i);
                break;
            case R.id.website:
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://at.linkedin.com/in/philippjahoda"));
                startActivity(i);
                break;
        }

        return true;
    }
}
