package cn.mdruby.pickphotovideoview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;

public class PickPhotoActivity extends AppCompatActivity {
    protected static final String PICK_DATA="pick_data";
    private PickData pickData;
    private RecyclerView mRV;
    private RVPhotoGridAdapter mAdapter;
    private List<MediaModel> mDatas;
//    private Subscriber<HashMap<>>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        pickData = (PickData) getIntent().getSerializableExtra(PICK_DATA);
        mDatas = new ArrayList<>();
        mRV = (RecyclerView) findViewById(R.id.act_pick_photo_RV);

        mRV.setLayoutManager(new GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false));
        mAdapter = new RVPhotoGridAdapter(this,mDatas,false);
        mRV.setAdapter(mAdapter);

        getPictures();
    }
    public static final String ALL_PHOTOS = "All Photos";


    private void getPictures() {
      new PickPhotoHelper(this, new PickPhotoListener() {
          @Override
          public void pickSuccess() {
              mDatas.clear();
              mDatas.addAll(PickPreferences.getInstance(PickPhotoActivity.this).getListImage().getGroupMedias().get(ALL_PHOTOS));
              Toast.makeText(PickPhotoActivity.this, "=="+mDatas.size(), Toast.LENGTH_SHORT).show();
              mAdapter.notifyDataSetChanged();
          }
      }).getImages(true);
    }
}
