package midori.kitchen.content.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import midori.kitchen.R;

/**
 * Created by Januar on 7/29/2017.
 */

public class ScannerFragment extends Fragment {
    private static final int PERMISSIONS_REQUEST_CAMERA = 200;
    @BindView(R.id.cameraPreview)
    CompoundBarcodeView cameraPreview;

    @BindView(R.id.text_result)
    EditText text_result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        ButterKnife.bind(this, view);

        //update 1412 : Checking permission untuk pengguna marshmallow
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);
        } else {
            initControls();
        }

        return view;

    }

    private void initControls() {

        cameraPreview.decodeContinuous(callback);
        cameraPreview.setStatusText(null);
        cameraPreview.setMinimumHeight(100);
        cameraPreview.setMinimumWidth(100);

    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                //barcodeView.setStatusText(result.getText());
                cameraPreview.pause();
                text_result.setText(result.getText());
//                PPno = result.getText();
            }

            //Do something with code result
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onResume() {
        cameraPreview.resume();
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
    }

    @Override
    public void onPause() {
        cameraPreview.pause();
        super.onPause();
        //releaseCamera();
        //mCamera.setPreviewCallback(null);
    }




}
