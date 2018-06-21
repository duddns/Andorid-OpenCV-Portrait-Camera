package kr.dont.portrait_camera;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import kr.dont.portrait_camera.lib.PortraitCameraBridgeViewBase;

public class MainActivity extends AppCompatActivity implements PortraitCameraBridgeViewBase.CvCameraViewListener2 {

    private final String _TAG = "MainActivity:";

    private PortraitCameraBridgeViewBase mOpenCvCameraView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (PortraitCameraBridgeViewBase) findViewById(R.id.cameraView);
        mOpenCvCameraView.setCvCameraViewListener(this);

        // duddns: Change Back / Front Camera
        Button changeCameraButton = (Button) findViewById(R.id.changeCameraButton);
        changeCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TAG = new StringBuilder(_TAG).append("onClickChangeCameraButton").toString();

                stopCamera(TAG);

                int cameraIndex = mOpenCvCameraView.getCameraIndex();
                switch (cameraIndex) {
                    case PortraitCameraBridgeViewBase.CAMERA_ID_FRONT:
                        mOpenCvCameraView.setCameraIndex(PortraitCameraBridgeViewBase.CAMERA_ID_BACK);
                        break;
                    case PortraitCameraBridgeViewBase.CAMERA_ID_BACK:
                        mOpenCvCameraView.setCameraIndex(PortraitCameraBridgeViewBase.CAMERA_ID_FRONT);
                        break;
                    default:
                        mOpenCvCameraView.setCameraIndex(PortraitCameraBridgeViewBase.CAMERA_ID_BACK);
                        break;
                }

                startCamera(TAG);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        String TAG = new StringBuilder(_TAG).append("onResume").toString();
        startCamera(TAG);
    }

    @Override
    protected void onPause() {
        String TAG = new StringBuilder(_TAG).append("onPause").toString();
        stopCamera(TAG);

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        String TAG = new StringBuilder(_TAG).append("onDestroy").toString();
        stopCamera(TAG);

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


    private void startCamera(String TAG) {
        if (!OpenCVLoader.initDebug()) {
            Log.i(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initiation");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, loaderCallback);
        } else {
            Log.i(TAG, "OpenCV library found inside package. Using it");
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private void stopCamera(String TAG) {
        Log.i(TAG, "Disabling a camera view");

        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }
}
