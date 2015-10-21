package kr.dont.portrait_camera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import kr.dont.portrait_camera.lib.PortraitCameraBridgeViewBase;

public class MainActivity extends AppCompatActivity implements PortraitCameraBridgeViewBase.CvCameraViewListener2 {

    private final String _TAG = "ProcessedCameraActivity:";

    private PortraitCameraBridgeViewBase mOpenCvCameraView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (PortraitCameraBridgeViewBase) findViewById(R.id.cameraView);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String TAG = new StringBuilder(_TAG).append("onResume").toString();
        if (!OpenCVLoader.initDebug()) {
            Log.i(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initiation");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, loaderCallback);
        } else {
            Log.i(TAG, "OpenCV library found inside package. Using it");
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onPause() {
        String TAG = new StringBuilder(_TAG).append("onPause").toString();
        Log.i(TAG, "Disabling a camera view");

        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        String TAG = new StringBuilder(_TAG).append("onDestroy").toString();
        Log.i(TAG, "Disabling a camera view");

        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }

        super.onDestroy();
    }

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            String TAG = new StringBuilder(_TAG).append("onManagerConnected").toString();

            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
            }
        }
    };

    @Override
    public void onCameraViewStarted(int width, int height) {
        String TAG = new StringBuilder(_TAG).append("onCameraViewStarted").toString();

        Log.i(TAG, "OpenCV CameraView Started");
    }

    @Override
    public void onCameraViewStopped() {
        String TAG = new StringBuilder(_TAG).append("onCameraViewStarted").toString();

        Log.i(TAG, "OpenCV CameraView Stopped");
    }

    @Override
    public Mat onCameraFrame(PortraitCameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();

        return rgba;
    }
}
