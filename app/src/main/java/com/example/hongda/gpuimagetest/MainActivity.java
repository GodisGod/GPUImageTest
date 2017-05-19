package com.example.hongda.gpuimagetest;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hongda.gpuimagetest.testgpuimagefilter.GPUImageExtTexFilter;
import com.example.hongda.gpuimagetest.testgpuimagefilter.GPUImageFilterTools;

import java.io.File;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;

public class MainActivity extends AppCompatActivity {

    private GPUImageFilter mFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;

    private VideoSurfaceView videoSurfaceView = null;

    private MediaPlayer mediaPlayer;
    RelativeLayout rlGlViewContainer;

    public enum FilterType {
        NOFILTER, CONTRAST, GRAYSCALE, SHARPEN, SEPIA, SOBEL_EDGE_DETECTION, THREE_X_THREE_CONVOLUTION, FILTER_GROUP, EMBOSS, POSTERIZE, GAMMA, BRIGHTNESS, INVERT, HUE, PIXELATION,
        SATURATION, EXPOSURE, HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE, TONE_CURVE, BLEND_COLOR_BURN, BLEND_COLOR_DODGE, BLEND_DARKEN, BLEND_DIFFERENCE,
        BLEND_DISSOLVE, BLEND_EXCLUSION, BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, BLEND_SCREEN, BLEND_ALPHA,
        BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY, BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL, LOOKUP_AMATORKA,
        GAUSSIAN_BLUR, CROSSHATCH, BOX_BLUR, CGA_COLORSPACE, DILATION, KUWAHARA, RGB_DILATION, SKETCH, TOON, SMOOTH_TOON, BULGE_DISTORTION, GLASS_SPHERE, HAZE, LAPLACIAN, NON_MAXIMUM_SUPPRESSION,
        SPHERE_REFRACTION, SWIRL, WEAK_PIXEL_INCLUSION, FALSE_COLOR, COLOR_BALANCE, LEVELS_FILTER_MIN, BILATERAL_BLUR, HALFTONE, TRANSFORM2D
    }

    private GPUImageFilterTools.FilterType currentFilterType = GPUImageFilterTools.FilterType.NOFILTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlGlViewContainer = (RelativeLayout) findViewById(R.id.rlGlViewContainer);

        findViewById(R.id.button_choose_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GPUImageFilterTools.showDialog(MainActivity.this, new GPUImageFilterTools.OnGpuImageFilterChosenListener() {

                    @Override
                    public void onGpuImageFilterChosenListener(GPUImageFilter filter, GPUImageFilterTools.FilterType filterType) {
                        currentFilterType = filterType;
                        switchFilterTo(filter);
                    }

                });
            }
        });

        findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ResultListener resultListener = new ResultListener() {
                    @Override
                    public void onResult(final boolean success, final String extra) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, success ? "Generate Sucess!" : "GenerateFailed! reason="
                                        + extra, Toast.LENGTH_LONG).show();
                                Log.i("tag", "==========================!!!!!!done!!!!!!!!===================");
                            }
                        });

                    }
                };

                GenerageVideoActivity.startGenerage(MainActivity.this
                ,"/storage/emulated/0/DCIM/dateme/temp/yiba_v_1495108931887.mp4",currentFilterType);

//                try {
////                    ExtractDecodeEditEncodeMuxTest test = new ExtractDecodeEditEncodeMuxTest(MainActivity.this);
////                    File file = new File("/storage/emulated/0/DCIM/dateme/temp/yiba_v_1495173901328.mp4");
////                    test.setSource(file.getAbsolutePath());
////                    test.setFilterType(currentFilterType);
////                    test.testExtractDecodeEditEncodeMuxAudioVideo();
////                    ExtractDecodeEditEncodeMuxTest test2 = new ExtractDecodeEditEncodeMuxTest(MainActivity.this);
////
////                    ArrayList<GPUImageFilter> filterArrayList = new ArrayList<>();
////                    filterArrayList.add(mFilter);
////                    test2.setSource("/storage/emulated/0/DCIM/dateme/temp/yiba_v_1495108931887.mp4");
////                    test2.setFilterType(filterArrayList);
////                    test2.testExtractDecodeEditEncodeMuxAudioVideo(resultListener);
//
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }

            }
        });

        initVideo();

    }

    //


    private void initVideo() {
        //todo
        File file = new File("/storage/emulated/0/DCIM/dateme/temp/yiba_v_1495108931887.mp4");
        Log.i("LHD", "视频路径： " + file.getAbsolutePath());
        mediaPlayer = MediaPlayer.create(this, Uri.parse(file.getAbsolutePath()));
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        videoSurfaceView = new VideoSurfaceView(this, mediaPlayer);
        videoSurfaceView.setSourceSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());

        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(-1, -1);
        rlGlViewContainer.addView(videoSurfaceView, rllp);
    }

    private void switchFilterTo(final GPUImageFilter filter) {
        if (mFilter == null
                || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;

            GPUImageFilterGroup filters = new GPUImageFilterGroup();
            filters.addFilter(new GPUImageExtTexFilter());
            filters.addFilter(mFilter);

            videoSurfaceView.setFilter(filters);

            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
            mFilterAdjuster.adjust(50);
        }
    }


    @Override
    protected void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    static public interface ResultListener {
        void onResult(boolean success, String extra);
    }

}
