package cn.mdruby.baselibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Went_Gone on 2017/9/9.
 */

public abstract class BaseFragment extends Fragment implements ViewListener{
    protected static final String TAG = "BaseFragment";
    protected Context mContext;
    protected int page = 1;
    protected int pageSize = 15;
    protected int refreshTime = 300;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null);
        mContext = getActivity();
        initBeforeDatas();
        initViews(view);
        setListeners();
        return view;
    }


    @Override
    public void initViews() {
    }

    public abstract void initViews(View view);
}
