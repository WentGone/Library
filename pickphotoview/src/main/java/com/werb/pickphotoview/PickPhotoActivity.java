package com.werb.pickphotoview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.werb.pickphotoview.adapter.PickGridAdapter;
import com.werb.pickphotoview.adapter.SpaceItemDecoration;
import com.werb.pickphotoview.model.GroupImage;
import com.werb.pickphotoview.model.MediaModel;
import com.werb.pickphotoview.model.PickData;
import com.werb.pickphotoview.model.PickHolder;
import com.werb.pickphotoview.util.PickConfig;
import com.werb.pickphotoview.util.PickGson;
import com.werb.pickphotoview.util.PickPhotoHelper;
import com.werb.pickphotoview.util.PickPhotoListener;
import com.werb.pickphotoview.util.PickPreferences;
import com.werb.pickphotoview.util.PickUtils;
import com.werb.pickphotoview.widget.MyToolbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by wanbo on 2016/12/30.
 */

public class PickPhotoActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 0X296;
    private PickData pickData;
    private RecyclerView photoList;
    private PickGridAdapter pickGridAdapter;
    private MyToolbar myToolbar;
    private TextView selectText, selectImageSize;
    //    private ArrayList<String> allPhotos;
    private ArrayList<MediaModel> allMedias;
    private RequestManager manager;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_activity_pick_photo);
        mContext = this;
        manager = Glide.with(mContext);
        pickData = (PickData) getIntent().getSerializableExtra(PickConfig.INTENT_PICK_DATA);
        if (pickData != null) {
            PickPreferences.getInstance(PickPhotoActivity.this).savePickData(pickData);
        } else {
            pickData = PickPreferences.getInstance(PickPhotoActivity.this).getPickData();
        }
        initToolbar();
        initRecyclerView();
        initSelectLayout();
    }

    @Override
    public void finish() {
        super.finish();
        PickHolder.newInstance(); //Reset stored selected image paths.
        overridePendingTransition(0, R.anim.pick_finish_slide_out_bottom);
    }

    private void initToolbar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(pickData.getStatusBarColor());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(pickData.isLightStatusBar()) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        selectText = (TextView) findViewById(R.id.tv_pick_photo);
        selectImageSize = (TextView) findViewById(R.id.tv_preview_photo);
        selectImageSize.setText(String.valueOf("0"));
        myToolbar = (MyToolbar) findViewById(R.id.toolbar);
        myToolbar.setBackgroundColor(pickData.getToolbarColor());
        myToolbar.setIconColor(pickData.getToolbarIconColor());
        myToolbar.setLeftIcon(R.mipmap.pick_ic_open);
        myToolbar.setRightIcon(R.mipmap.pick_ic_close);
        myToolbar.setPhotoDirName(getString(R.string.pick_all_photo));
        myToolbar.setLeftLayoutOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickPhotoActivity.this.startPhotoListActivity();
            }
        });
        myToolbar.setRightLayoutOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickPhotoActivity.this.finish();
            }
        });

        selectText.setOnClickListener(selectClick);
    }

    private void initRecyclerView() {
        photoList = (RecyclerView) findViewById(R.id.photo_list);
        photoList.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager layoutManager = new GridLayoutManager(this, pickData.getSpanCount());
        photoList.setLayoutManager(layoutManager);
        photoList.addItemDecoration(new SpaceItemDecoration(PickUtils.getInstance(PickPhotoActivity.this).dp2px(PickConfig.ITEM_SPACE), pickData.getSpanCount()));
        photoList.addOnScrollListener(scrollListener);
        PickPhotoHelper helper = new PickPhotoHelper(PickPhotoActivity.this, new PickPhotoListener() {
            @Override
            public void pickSuccess() {
                GroupImage groupImage = PickPreferences.getInstance(PickPhotoActivity.this).getListImage();
//                allPhotos = groupImage.mGroupMap.get(PickConfig.ALL_PHOTOS);
                allMedias = groupImage.mGroupMapMedia.get(PickConfig.ALL_PHOTOS);
                ArrayList<MediaModel> mediaModels = groupImage.mGroupMapMedia.get(PickConfig.ALL_PHOTOS);
//                ArrayList<MediaModel> mediaModels = groupImage.mGroupMapMedia.get("练习室");
                Log.e("TAG", "pickSuccess: "+mediaModels );
//                if(allPhotos == null){
                if(allMedias == null){
                    Log.d("PickPhotoView","Image is Empty");
                }else {
//                    Log.d("All photos size:", String.valueOf(allPhotos.size()));
                    Log.d("All photos size:", String.valueOf(allMedias.size()));
                }
//                if (allPhotos != null && !allPhotos.isEmpty()) {
                if (allMedias != null && !allMedias.isEmpty()) {
//                    pickGridAdapter = new PickGridAdapter(PickPhotoActivity.this, allPhotos, pickData, imageClick);
                    pickGridAdapter = new PickGridAdapter(PickPhotoActivity.this, mediaModels, pickData, imageClick);
                    pickGridAdapter.setOnItemClickListener(new PickGridAdapter.OnItemClickListener() {
                        @Override
                        public void onCameraClick() {
                            checkCameraPermission();

                        }

                        @Override
                        public void onGallery(int position) {

                        }
                    });
                    photoList.setAdapter(pickGridAdapter);
                }
            }
        });
        helper.setShowVideo(pickData.isShowVideo());
        helper.getImages(pickData.isShowGif());
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)  {

            //判断是否开启权限
            successPermisson();
        } else {
//            requestPermisson();
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    private void successPermisson() {
        try {
            File photoFile = PickUtils.getInstance(mContext).getPhotoFile(mContext);
            photoFile.delete();
            if (photoFile.createNewFile()) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, PickUtils.getInstance(mContext).getUri(photoFile));
                startActivityForResult(intent, PickConfig.CAMERA_PHOTO_DATA);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                successPermisson();
            } else {
                Toast.makeText(mContext, "已拒绝权限！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initSelectLayout() {
        LinearLayout selectLayout = (LinearLayout) findViewById(R.id.select_layout);
        selectLayout.setVisibility(View.VISIBLE);
    }

    public void updateSelectText(String selectSize) {
        if (selectSize.equals("0")) {
            selectImageSize.setText(String.valueOf(0));
            selectText.setText(getString(R.string.pick_pick));
            selectText.setTextColor(ContextCompat.getColor(mContext, R.color.pick_gray));
            selectText.setEnabled(false);
        } else {
            selectText.setText(getString(R.string.pick_upload));
            selectImageSize.setText(String.valueOf(selectSize));
            selectText.setTextColor(pickData.getSelectIconColor());
            selectText.setEnabled(true);
        }
    }

    private void startPhotoListActivity() {
        Intent intent = new Intent();
        intent.setClass(PickPhotoActivity.this, PickListActivity.class);
        intent.putExtra(PickConfig.INTENT_PICK_DATA, pickData);
        startActivityForResult(intent, PickConfig.LIST_PHOTO_DATA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            return;
        }
        if (requestCode == PickConfig.LIST_PHOTO_DATA) {
            if (data != null) {
                String dirName = data.getStringExtra(PickConfig.INTENT_DIR_NAME);
                GroupImage listImage = PickPreferences.getInstance(PickPhotoActivity.this).getListImage();
//                allPhotos = listImage.mGroupMap.get(dirName);
                allMedias = listImage.mGroupMapMedia.get(dirName);
                pickGridAdapter.updateData(allMedias);
                myToolbar.setPhotoDirName(dirName);
//                selectText.setText(getString(R.string.pick_pick));
                selectText.setText(getString(R.string.pick_upload));
                selectText.setTextColor(ContextCompat.getColor(mContext, R.color.pick_black));
            }
        } else if (requestCode == PickConfig.CAMERA_PHOTO_DATA) {
            String path;
            if (data != null) {
                path = data.getData().getPath();
                if (path.contains("/pick_camera")) {
                    path = path.replace("/pick_camera", "/storage/emulated/0/DCIM/Camera");
                }
            } else {
                path = PickUtils.getInstance(PickPhotoActivity.this).getFilePath(PickPhotoActivity.this);
            }
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
            Intent intent = new Intent();
            ArrayList<String> list = new ArrayList<>();
            list.add(path);
            ArrayList<MediaModel> mediaModels = new ArrayList<>();
            MediaModel mediaModel = new MediaModel();
            mediaModel.setMimeType("image/jpg");
            mediaModel.setThumPath(path);
            mediaModel.setPath(path);
            mediaModels.add(mediaModel);
            intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, mediaModels);
            setResult(PickConfig.PICK_PHOTO_DATA, intent);
            finish();
        }else if(requestCode == PickConfig.PREVIEW_PHOTO_DATA){
            if (data != null) {
                ArrayList<MediaModel> selectMedias = (ArrayList<MediaModel>) data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT);
                pickGridAdapter.setSelectMedia(selectMedias);
                pickGridAdapter.notifyDataSetChanged();
                updateSelectText(String.valueOf(selectMedias.size()));
            }
        }
    }

    View.OnClickListener imageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            String imgPath = (String) v.getTag(R.id.pick_image_path);
            MediaModel imgPath = (MediaModel) v.getTag(R.id.pick_image_path);
            Intent intent = new Intent();
            if (imgPath.getFile().getAbsolutePath().endsWith(".mp4")){
                intent.setClass(PickPhotoActivity.this, PlayerActivity.class);
            }else {
                intent.setClass(PickPhotoActivity.this, PickPhotoPreviewActivity.class);
            }
            intent.putExtra(PickConfig.INTENT_IMG_PATH, imgPath);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PickPhotoActivity.this);
//            String ss = preferences.getString(PickPreferences.IMAGE_LIST, "");
            String ss = PickGson.toJson(allMedias);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(PickConfig.MEIDA_PREVIEW_MODEL,ss);
            edit.apply();

//            intent.putExtra(PickConfig.INTENT_IMG_LIST,ss);
//            intent.putExtra(PickConfig.INTENT_IMG_LIST, allMedias);
            intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, pickGridAdapter.getSelectMeida());
            intent.putExtra(PickConfig.INTENT_PICK_DATA, pickData);
            startActivityForResult(intent, PickConfig.PREVIEW_PHOTO_DATA);
        }
    };

    private View.OnClickListener selectClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            select();
        }
    };

    public void select(){
        if(pickGridAdapter == null){
            return;
        }

        if (!pickGridAdapter.getSelectMeida().isEmpty()) {
            Intent intent = new Intent();
            intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, pickGridAdapter.getSelectMeida());
            setResult(PickConfig.PICK_PHOTO_DATA, intent);
            finish();
        }
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (Math.abs(dy) > PickConfig.SCROLL_THRESHOLD) {
                manager.pauseRequests();
            } else {
                manager.resumeRequests();
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                manager.resumeRequests();
            }
        }
    };
}
