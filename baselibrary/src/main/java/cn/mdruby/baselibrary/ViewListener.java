package cn.mdruby.baselibrary;

/**
 * 控件的初始化等操作接口
 * Created by Went_Gone on 2017/5/3.
 */
public interface ViewListener {

    /**
     * 获取布局id
     * @return 所在布局的id
     */
    int getLayoutId();

    /**
     * 初始化传递过来的数据或在布局生成之前初始化的数据
     */
    void initBeforeDatas();

    /**
     * 初始化控件
     */
    void initViews();

    /**
     * 给控件设置监听
     */
    void setListeners();
}
