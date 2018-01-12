package cn.mdruby.pickphotovideoview.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private List<MediaModel> mSelecteds;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private TextView mTVtitle;
    private int photoSize = 100;
    private boolean showVideo = true;
    private boolean showCamera = false;
    private int selectedCount = 3;
    private TextView mTVselected,mTVcount;
    private int isPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        pickData = (PickData) getIntent().getSerializableExtra(PICK_DATA);
        mDatas = new ArrayList<>();
        mSelecteds = new ArrayList<>();
        mRV = (RecyclerView) findViewById(R.id.act_pick_photo_RV);
        mRVList = (RecyclerView) findViewById(R.id.act_pick_photo_list_RV);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.act_pick_DrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mTVtitle = (TextView) findViewById(R.id.act_pick_TV_title);
        mTVselected = (TextView) findViewById(R.id.act_pick_TV_bottom_selected);
        mTVcount = (TextView) findViewById(R.id.act_pick_TV_bottom_count);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mRV.setLayoutManager(new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false));
        mRVList.setLayoutManager(new LinearLayoutManager(this));
        showCamera = pickData.isShowCamera();
        mAdapter = new RVPhotoGridAdapter(this,mDatas,showCamera);
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

        mTVselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //回调
                callback();
            }
        });

        getPictures();
    }

    private void callback() {
        Intent intent = getIntent();
        intent.putExtra(PickConfig.KEY.MEDIA_FILE_DATA, (Serializable) mSelecteds);
        setResult(RESULT_OK,intent);
        this.finish();
    }

    /**
     * 点击列表中的某一项
     * @param position
     */
    private void clickListItem(int position) {
        List<MediaModel> item = mListAdapter.getItem(position);
        mTVcount.setText("");
        mSelecteds.clear();
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
              mAdapter.notifyDataSetChanged();
          }
      });
        vhlepr.getImages(true);
        vhlepr.setShowVideo(showVideo);
    }

    @Override
    public void onCameraClick() {
        Intent intent = new Intent(this, CameraVideoActivity.class);
        startActivityForResult(intent, PickConfig.RequestCode.TAKE_PHOTO_BY_SELF);
    }

    @Override
    public void onPhotoClick(int position) {
        Intent intent = new Intent(this,PickPhotoPreviewActivity.class);
        MediaModel mediaModel = mDatas.get(position);
        isPosition = position;
        if (mDatas.size()>=photoSize){
            List<MediaModel> mediaModels = new ArrayList<MediaModel>(mDatas.subList((position - photoSize/2) < 0 ? 0 : (position - photoSize/2), mDatas.size() - position > photoSize/2 ? photoSize/2+position : (mDatas.size() - photoSize/2)));
            List<MediaModel> a = new ArrayList<>();
            for (int i = 0; i < mediaModels.size(); i++) {
                MediaModel mediaModel1 = mediaModels.get(i);
                if (!mediaModel1.getMimeType().contains("video")){
                    a.add(mediaModel1);
                }
            }
            intent.putExtra(PickPhotoPreviewActivity.MEDIA_DATAS, (Serializable) a);
            int po = 0;
            for (int i = 0; i < a.size(); i++) {
                if (a.get(i).getPath().equals(mediaModel.getPath())){
                    po = i;
                    break;
                }
            }
            intent.putExtra(PickPhotoPreviewActivity.POSITION_COUNT,po);
        }else {
            List<MediaModel> a = new ArrayList<>();
            for (int i = 0; i < mDatas.size(); i++) {
                MediaModel mediaModel1 = mDatas.get(i);
                if (!mediaModel1.getMimeType().contains("video")){
                    a.add(mediaModel1);
                }
            }
            intent.putExtra(PickPhotoPreviewActivity.MEDIA_DATAS, (Serializable) a);
            intent.putExtra(PickPhotoPreviewActivity.POSITION_COUNT,position);
        }
        startActivityForResult(intent,PickConfig.RequestCode.PRE_PHOTO_CODE);
    }

    @Override
    public void onVideoClick(int position) {
        MediaModel mediaModel = mDatas.get(position);
        Intent intent = new Intent(this,PickVideoPreviewActivity.class);
        intent.putExtra(PickVideoPreviewActivity.MEDIA_BEAN,mediaModel);
        startActivity(intent);
    }

    @Override
    public void onSelectClick(int position) {
        MediaModel item = mAdapter.getItem(position);
        item.setSelected(!item.isSelected());
        mAdapter.notifyItemChanged(showCamera?(position+1):position);
        setSelected(item);
        mTVcount.setText(mSelecteds.size()+"");
    }

    private void setSelected(MediaModel item) {
        if (item.isSelected()){
            if (!mSelecteds.contains(item)){
                mSelecteds.add(item);
            }
        }else {
            if (mSelecteds.contains(item)){
                mSelecteds.remove(item);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PickConfig.RequestCode.TAKE_PHOTO_BY_SELF:
                if (resultCode == RESULT_OK){
                    String imagePath = data.getStringExtra(AppConstant.KEY.IMG_PATH);
                    MediaModel mediaModel = new MediaModel();
                    mediaModel.setFile(new File(imagePath));
                    mediaModel.setPath(imagePath);
                    mediaModel.setThumPath(imagePath);
                    mediaModel.setMimeType(imagePath.endsWith(".mp4")?"video":"image");
                    if (imagePath.endsWith(".mp4")){
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(imagePath);
                            mediaPlayer.prepare();
                            int duration = mediaPlayer.getDuration();
                            String converted = String.format("%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes(duration),
                                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                            );
                            mediaModel.setDuration(duration);
                            mediaModel.setDurationStr(converted);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    mDatas.add(0,mediaModel);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case PickConfig.RequestCode.PRE_PHOTO_CODE:
                List<MediaModel> pres = (List<MediaModel>) data.getSerializableExtra(PickConfig.KEY.PRE_PHOTO_FILE);
                for (int i = 0; i < mDatas.size(); i++) {
                    for (int j = 0; j < pres.size(); j++) {
                        if (mDatas.get(i).compareTo(pres.get(j)) == 0){
                            mDatas.get(i).setSelected(pres.get(j).isSelected());
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                mSelecteds.clear();
                for (int i = 0; i < mDatas.size(); i++) {
                    setSelected(mDatas.get(i));
                }
                mTVcount.setText(mSelecteds.size()+"");
                break;
        }
    }
}
