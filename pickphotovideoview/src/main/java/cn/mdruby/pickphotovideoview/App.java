package cn.mdruby.pickphotovideoview;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by Went_Gone on 2018/1/8.
 */

public class App extends Application {
    private static App app;
    public static App getContext(){
        return app;
    }

    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        app = this;
        super.onCreate();
    }

    public HttpProxyCacheServer getProxy() {
        return proxy == null ? (proxy = newProxy()) : proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}
