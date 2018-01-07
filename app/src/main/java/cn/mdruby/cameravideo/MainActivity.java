package cn.mdruby.cameravideo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observer;

import cn.mdruby.pickphotovideoview.PickPhotoActivity;
import cn.mdruby.pickphotovideoview.PickPhotoView;
import rx.Observable;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {
    private TextView mTVMain;
    private Button mBtnClick;
    private Observable<String> observable;
    private Subscriber<String> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTVMain = (TextView) findViewById(R.id.act_main_TV);
        mBtnClick = (Button) findViewById(R.id.act_main_Btn);
//        Observable<String>
//        startActivity(new Intent(this, PickPhotoActivity.class));
        new PickPhotoView.Bulid(this).showCamera(true).start();

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
}
