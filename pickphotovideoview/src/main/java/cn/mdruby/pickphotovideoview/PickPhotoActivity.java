package cn.mdruby.pickphotovideoview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.mdruby.cameravideolibrary.AppConstant;
import cn.mdruby.cameravideolibrary.CameraVideoActivity;
import cn.mdruby.pickphotovideoview.abstracts.OnItemPhotoClickListener;
import cn.mdruby.pickphotovideoview.adapter.RVPhotoGridAdapter;
import cn.mdruby.pickphotovideoview.adapter.RVPhotoListAdapter;

public class PickPhotoActivity extends AppCompatActivity implements OnItemPhotoClickListener{
    protected static final String PICK_DATA="pick_data";
    private PickData pickData;
    private RecyclerView mRV;
    private RecyclerView mRVList;
    private RVPhotoGridAdapter mAdapter;
    private RVPhotoListAdapter mListAdapter;
    private List<MediaModel> mDatas;
//    private Subscriber<HashMap<>>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        pickData = (PickData) getIntent().getSerializableExtra(PICK_DATA);
        mDatas = new ArrayList<>();
        mRV = (RecyclerView) findViewById(R.id.act_pick_photo_RV);
        mRVList = (RecyclerView) findViewById(R.id.act_pick_photo_list_RV);

        mRV.setLayoutManager(new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false));
        mRVList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RVPhotoGridAdapter(this,mDatas,pickData.isShowCamera());
        mRV.setAdapter(mAdapter);

        mListAdapter = new RVPhotoListAdapter(this);
        mRVList.setAdapter(mListAdapter);

        mAdapter.setOnItemPhotoClickListener(this);

        getPictures();
    }
    public static final String ALL_PHOTOS = "All Photos";


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
        vhlepr.setShowVideo(false);
    }

    @Override
    public void onCameraClick() {
        Intent intent = new Intent(this, CameraVideoActivity.class);
        startActivityForResult(intent,PickConfig.RrquestCode.TAKE_PHOTO_BY_SELF);
    }

    @Override
    public void onPhotoClick(int position) {

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
