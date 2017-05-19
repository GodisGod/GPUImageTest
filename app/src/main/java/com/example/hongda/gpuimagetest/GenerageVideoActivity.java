package com.example.hongda.gpuimagetest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.hongda.gpuimagetest.testgpuimagefilter.ExtractDecodeEditEncodeMuxTest;
import com.example.hongda.gpuimagetest.testgpuimagefilter.GPUImageFilterTools;

import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class GenerageVideoActivity extends AppCompatActivity {

    public static void startGenerage(Context ctx, String path, GPUImageFilterTools.FilterType filterType) {
        Intent intent = new Intent(ctx, GenerageVideoActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("filterType", filterType);
        ctx.startActivity(intent);
    }

    private String mPath;
    private com.example.hongda.gpuimagetest.testgpuimagefilter.GPUImageFilterTools.FilterType filterType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generage_video);

        mPath = getIntent().getStringExtra("path");
        filterType = (GPUImageFilterTools.FilterType) getIntent().getSerializableExtra("filterType");
        Log.i("LHD", "path:  " + mPath + "  " + filterType);
        ExtractDecodeEditEncodeMuxTest test = new ExtractDecodeEditEncodeMuxTest(GenerageVideoActivity.this);
        test.setSource(mPath);
        ArrayList<GPUImageFilter> filterArrayList = new ArrayList<>();
        GPUImageFilter filter = GPUImageFilterTools.createFilterForType(GenerageVideoActivity.this, filterType);
        filterArrayList.add(filter);
        test.setFilterType(filterArrayList);

        final MainActivity.ResultListener resultListener = new MainActivity.ResultListener() {
            @Override
            public void onResult(final boolean success, final String extra) {
                GenerageVideoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GenerageVideoActivity.this, success ? "Generate Sucess!" : "GenerateFailed! reason="
                                + extra, Toast.LENGTH_LONG).show();
                        Log.i("tag", "==========================!!!!!!done!!!!!!!!===================");
                    }
                });

            }
        };

        try {
            test.testExtractDecodeEditEncodeMuxAudioVideo(resultListener);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

}
