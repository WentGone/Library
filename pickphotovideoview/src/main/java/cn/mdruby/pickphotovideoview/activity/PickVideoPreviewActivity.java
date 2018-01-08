package cn.mdruby.pickphotovideoview.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;

import cn.ittiger.player.Config;
import cn.ittiger.player.PlayerManager;
import cn.ittiger.player.VideoPlayerView;
import cn.ittiger.player.factory.ExoPlayerFactory;
import cn.mdruby.pickphotovideoview.App;
import cn.mdruby.pickphotovideoview.MediaModel;
import cn.mdruby.pickphotovideoview.R;
import tcking.github.com.giraffeplayer.GiraffePlayer;
import tcking.github.com.giraffeplayer.IjkVideoView;

public class PickVideoPreviewActivity extends AppCompatActivity {
    public static final String MEDIA_BEAN = "media_bean";
    private VideoPlayerView mVideoPlayer;
    private MediaModel mediaModel;
//    private IjkVideoView mVideoPlayer;
    private HttpProxyCacheServer proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_video_preview);
        mediaModel = (MediaModel) getIntent().getSerializableExtra(MEDIA_BEAN);
        PlayerManager.loadConfig(
                new Config.Builder(this)
                        .buildPlayerFactory(new ExoPlayerFactory(this))//使用ExoPlayer内核作为视频播放器，默认使用MediaPlayer
                        .enableSmallWindowPlay()//开启小窗口播放，默认不开其
                        .cache(true)//开启缓存功能，默认不开启
//                        .cacheProxy(HttpProxyCacheServer)//自定义缓存配置，不设置则采用默认的缓存配置
                        .build()
        );

        mVideoPlayer = (VideoPlayerView) findViewById(R.id.video_player_view);
//        mVideoPlayer.bind("file://"+mediaModel.getFile().getAbsolutePath(),"");
//        mVideoPlayer.bind("http://www.eywedu.com.cn/sanzijing/UploadFiles_2038/szj-01.mp4","");
//        mVideoPlayer.bind("https://cdsstest.mdruby.cn/hdata/video/358/12097da4924f4d0e887f3d0a6cffe883.mp4","");
//        mVideoPlayer = (IjkVideoView) findViewById(R.id.video_view);
//        mVideoPlayer.setVideoURI(Uri.parse(mediaModel.getPath()));
        String url = "https://cdsstest.mdruby.cn/hdata/video/358/12097da4924f4d0e887f3d0a6cffe883.mp4";
        HttpProxyCacheServer proxy = getProxy();
        String proxyUrl = proxy.getProxyUrl(url);
        GiraffePlayer player = new GiraffePlayer(this);
//        player.play(mediaModel.getFile().getAbsolutePath());
        player.play(proxyUrl);
//        Toast.makeText(this, "="+mediaModel.getPath(), Toast.LENGTH_SHORT).show();
//        Glide.with(this).load(mediaModel.getThumPath()).into(holder.mPlayerView.getThumbImageView());

    }

    public HttpProxyCacheServer getProxy() {
        return proxy == null ? (proxy = newProxy()) : proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }

}
