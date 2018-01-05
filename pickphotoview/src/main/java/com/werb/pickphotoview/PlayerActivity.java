package com.werb.pickphotoview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.werb.pickphotoview.model.MediaModel;


public class PlayerActivity extends Activity {
//    private PlayerView player;
    private Context mContext;
    private View rootView;
    private MediaModel mediaFile;
//    private SampleVideo sampleVideo;
//    private OrientationUtils orientationUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
//        rootView = getLayoutInflater().from(this).inflate(R.layout.simple_player_view_player, null);
        setContentView(R.layout.pick_activity_player_video);
        /*sampleVideo = findViewById(R.id.act_player_video_SampleVideo);
        mediaFile = (MediaModel) getIntent().getSerializableExtra(PickConfig.INTENT_IMG_PATH);
        String url = "http://183.6.245.249/v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4";
        url = mediaFile.getFile().getAbsolutePath();
        sampleVideo.setUp(url,true,"");
        orientationUtils = new OrientationUtils(this, sampleVideo);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        sampleVideo.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });

        //是否可以滑动调整
        sampleVideo.setIsTouchWiget(true);

        //设置返回按键功能
        sampleVideo.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        /*player = new PlayerView(this, rootView)
                .setTitle("")
                .setScaleType(PlayStateParams.fitparent)
                .forbidTouch(false)
                .hideMenu(true)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        Glide.with(mContext)
//                                .load("http://pic2.nipic.com/20090413/406638_125424003_2.jpg")
                                .load(mediaFile.getThumPath())
                                .into(ivThumbnail);
                    }
                })
                .setPlaySource(url)
                .setPlayerBackListener(new OnPlayerBackListener() {
                    @Override
                    public void onPlayerBack() {
                        //这里可以简单播放器点击返回键
                        finish();
                    }
                })
                .startPlay();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
//        sampleVideo.onVideoResume();
     /*   if (player != null) {
            player.onResume();
        }*/
    }
    @Override
    protected void onPause() {
//        sampleVideo.onVideoPause();
        super.onPause();
    }

}
