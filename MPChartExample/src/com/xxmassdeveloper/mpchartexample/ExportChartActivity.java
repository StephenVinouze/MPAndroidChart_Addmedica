
package com.xxmassdeveloper.mpchartexample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.xxmassdeveloper.mpchartexample.fragments.ExportChartFragment;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

public class ExportChartActivity extends DemoBase {

    private static final int WRITE_STORAGE_PERMISSION_CODE = 110;

    private ExportChartFragment exportChartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_export_chart);

        exportChartFragment = new ExportChartFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.line_chart_container, exportChartFragment).commit();
    }

    private void save() {
        exportChartFragment.saveChart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line_export, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
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

}
