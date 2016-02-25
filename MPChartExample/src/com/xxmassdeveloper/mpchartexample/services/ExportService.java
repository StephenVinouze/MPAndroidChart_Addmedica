package com.xxmassdeveloper.mpchartexample.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by stephenvinouze on 25/02/16.
 */
public class ExportService extends IntentService {

    private static final String INTENT_NAME = "ExportService";

    public ExportService() {
        super(INTENT_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
