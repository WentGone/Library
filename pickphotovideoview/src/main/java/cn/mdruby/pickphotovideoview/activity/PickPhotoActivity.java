package cn.mdruby.pickphotovideoview.activity;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.mdruby.cameravideolibrary.AppConstant;
import cn.mdruby.cameravideolibrary.CameraVideoActivity;
import cn.mdruby.pickphotovideoview.MediaModel;
import cn.mdruby.pickphotovideoview.PickConfig;
import cn.mdruby.pickphotovideoview.PickData;
import cn.mdruby.pickphotovideoview.PickPhotoHelper;
import cn.mdruby.pickphotovideoview.PickPhotoListener;
import cn.mdruby.pickphotovideoview.PickPreferences;
import cn.mdruby.pickphotovideoview.R;
import cn.mdruby.pickphotovideoview.abstracts.OnItemPhotoClickListener;
import cn.mdruby.pickphotovideoview.abstracts.OnRVListClickListener;
import cn.mdruby.pickphotovideoview.adapter.RVPhotoGridAdapter;
import cn.mdruby.pickphotovideoview.adapter.RVPhotoListAdapter;

public class PickPhotoActivity extends AppCompatActivity implements OnItemPhotoClickListener{
    public static final String PICK_DATA="pick_data";
    private PickData pickData;
    private RecyclerView mRV;
    private RecyclerView mRVList;
    private RVPhotoGridAdapter mAdapter;
    private RVPhotoListAdapter mListAdapter;
    private List<MediaModel> mDatas;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private TextView mTVtitle;
    private int photoSize = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        pickData = (PickData) getIntent().getSerializableExtra(PICK_DATA);
        mDatas = new ArrayList<>();
        mRV = (RecyclerView) findViewById(R.id.act_pick_photo_RV);
        mRVList = (RecyclerView) findViewById(R.id.act_pick_photo_list_RV);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.act_pick_DrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mTVtitle = (TextView) findViewById(R.id.act_pick_TV_title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mRV.setLayoutManager(new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false));
        mRVList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RVPhotoGridAdapter(this,mDatas,pickData.isShowCamera());
        mRV.setAdapter(mAdapter);

        mListAdapter = new RVPhotoListAdapter(this);
        mRVList.setAdapter(mListAdapter);

        mAdapter.setOnItemPhotoClickListener(this);
        mListAdapter.setOnRVListClickListener(new OnRVListClickListener() {
            @Override
            public void onClickItem(int position) {
                clickListItem(position);
            }
        });

        getPictures();
    }

    /**
     * 点击列表中的某一项
     * @param position
     */
    private void clickListItem(int position) {
        List<MediaModel> item = mListAdapter.getItem(position);
        mTVtitle.setText(mListAdapter.getDirName(position));
        mDatas.clear();
        mDatas.addAll(item);
        mDrawerLayout.closeDrawers();
        mAdapter.notifyDataSetChanged();
    }

    public static final String ALL_PHOTOS = "All Photos";

    /**
     * 获取所有的照片
     */
    private void getPictures() {
      PickPhotoHelper vhlepr = new PickPhotoHelper(this, new PickPhotoListener() {
          @Override
          public void pickSuccess() {
              mDatas.clear();
              mDatas.addAll(PickPreferences.getInstance(PickPhotoActivity.this).getListImage().getGroupMedias().get(ALL_PHOTOS));
              Toast.makeText(PickPhotoActivity.this, "=="+mDatas.size(), Toast.LENGTH_SHORT).show();
              mAdapter.notifyDataSetChanged();
          }
      });
        vhlepr.getImages(true);
        vhlepr.setShowVideo(true);
    }

    @Override
    public void onCameraClick() {
        Intent intent = new Intent(this, CameraVideoActivity.class);
        startActivityForResult(intent, PickConfig.RrquestCode.TAKE_PHOTO_BY_SELF);
    }

    @Override
    public void onPhotoClick(int position) {
        Intent intent = new Intent(this,PickPhotoPreviewActivity.class);
        MediaModel mediaModel = mDatas.get(position);

        if (mDatas.size()>=photoSize){
            List<MediaModel> mediaModels = new ArrayList<MediaModel>(mDatas.subList((position - photoSize/2) < 0 ? 0 : (position - photoSize/2), mDatas.size() - position > photoSize/2 ? photoSize/2+position : (mDatas.size() - photoSize/2)));
            intent.putExtra(PickPhotoPreviewActivity.MEDIA_DATAS, (Serializable) mediaModels);
            int po = 0;
            for (int i = 0; i < mediaModels.size(); i++) {
                if (mediaModels.get(i).getPath().equals(mediaModel.getPath())){
                    po = i;
                    break;
                }
            }
            intent.putExtra(PickPhotoPreviewActivity.POSITION_COUNT,po);
        }else {
            intent.putExtra(PickPhotoPreviewActivity.MEDIA_DATAS, (Serializable) mDatas);
            intent.putExtra(PickPhotoPreviewActivity.POSITION_COUNT,position);
        }
        startActivity(intent);
    }

    @Override
    public void onVideoClick(int position) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PickConfig.RrquestCode.TAKE_PHOTO_BY_SELF:
                if (resultCode == RESULT_OK){
                    String imagePath = data.getStringExtra(AppConstant.KEY.IMG_PATH);
                    MediaModel mediaModel = new MediaModel();
                    mediaModel.setFile(new File(imagePath));
                    mediaModel.setPath(imagePath);
                    mediaModel.setThumPath(imagePath);
                    mDatas.add(0,mediaModel);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
