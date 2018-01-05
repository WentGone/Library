package cn.mdruby.cameravideolibrary;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class CameraActivity extends AppCompatActivity {
    private FragmentManager mfm;
    private CameraFragment mCameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mCameraFragment = new CameraFragment();
        mfm = getSupportFragmentManager();
        FragmentTransaction transaction = mfm.beginTransaction();
        transaction.replace(R.id.act_camera,mCameraFragment);
        transaction.commit();
    }
}
