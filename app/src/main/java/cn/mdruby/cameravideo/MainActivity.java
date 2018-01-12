package cn.mdruby.cameravideo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afa.tourism.greendao.gen.UserDao;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.List;

import cn.mdruby.pickphotovideoview.MediaModel;
import cn.mdruby.pickphotovideoview.PickConfig;
import cn.mdruby.pickphotovideoview.PickPhotoView;
import rx.Observable;
import rx.Subscriber;
import tcking.github.com.giraffeplayer.GiraffePlayer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView mTVMain;
    private Button mBtnClick;
    private Observable<String> observable;
    private Subscriber<String> subscriber;
    GiraffePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTVMain = (TextView) findViewById(R.id.act_main_TV);
        mBtnClick = (Button) findViewById(R.id.act_main_Btn);
//        Observable<String>
//        startActivity(new Intent(this, PickPhotoActivity.class));
        new PickPhotoView.Bulid(this).showCamera(true).start();
//        player = new GiraffePlayer(this);

        UserDao dao = App.getContext().getDaoSession().getUserDao();
        User insertData = new User(null, "Went", "23", "男", "测试");
        dao.insert(insertData);

        subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mTVMain.setText(s);
            }
        };

        mBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBtn();
            }
        });

        OkGo.<String>get("")
                .cacheKey("")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                    }
                });

//        downloadVideo();
    }


    private void downloadVideo() {
        String url = "https://cdsstest.mdruby.cn/hdata/video/358/12097da4924f4d0e887f3d0a6cffe883.mp4";
        GetRequest<File> request = OkGo.<File>get(url);
        DownloadTask task = OkDownload.request("", request).save().register(new DownLoadVideoListener(""));
        task.start();
    }
    private class DownLoadVideoListener extends DownloadListener{

        public DownLoadVideoListener(Object tag) {
            super(tag);
        }

        @Override
        public void onStart(Progress progress) {
            Toast.makeText(MainActivity.this, "开始", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProgress(Progress progress) {
            String filePath = progress.filePath;
            if (!TextUtils.isEmpty(filePath)){
//                Log.e(TAG, "onProgress: "+filePath );
//                player.play(filePath);
            }
        }

        @Override
        public void onError(Progress progress) {
            Toast.makeText(MainActivity.this, "错误", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish(File file, Progress progress) {

        }

        @Override
        public void onRemove(Progress progress) {

        }
    }

    private void clickBtn() {
        observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("你好");
                subscriber.onCompleted();
            }
        });

        observable.subscribe(subscriber);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PickConfig.RequestCode.SELECT_PHOTO:
                if (resultCode == RESULT_OK){
                    List<MediaModel> medias = (List<MediaModel>) data.getSerializableExtra(PickConfig.KEY.MEDIA_FILE_DATA);
                    Toast.makeText(this, "="+medias.size(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
