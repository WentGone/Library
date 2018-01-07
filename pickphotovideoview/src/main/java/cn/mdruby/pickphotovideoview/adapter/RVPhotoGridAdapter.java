package cn.mdruby.pickphotovideoview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.mdruby.pickphotovideoview.MediaModel;
import cn.mdruby.pickphotovideoview.R;
import cn.mdruby.pickphotovideoview.abstracts.OnItemPhotoClickListener;

/**
 * Created by Went_Gone on 2018/1/5.
 */

public class RVPhotoGridAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<MediaModel> mDatas;
    private boolean showCamera;
    private static final int CAMERA_VIEW_TYPE = 1;
    private static final int PHOTO_VIEW_TYPE = 2;

    public RVPhotoGridAdapter(Context context, List<MediaModel> mDatas, boolean showCamera) {
        this.context = context;
        this.mDatas = mDatas;
        this.showCamera = showCamera;
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera){
            if (position == 0){
                return CAMERA_VIEW_TYPE;
            }else {
                return PHOTO_VIEW_TYPE;
            }
        }else {
            return PHOTO_VIEW_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType){
            case CAMERA_VIEW_TYPE:
                viewHolder = new RVCameraViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pick_camera_layout,parent,false));
                break;
            case PHOTO_VIEW_TYPE:
                viewHolder = new RVPhotoGridViewHolder(LayoutInflater.from(context).inflate(R.layout.item_photo_grid_layout,parent,false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RVPhotoGridViewHolder){
            ((RVPhotoGridViewHolder) holder).bindView(position);
        }else if (holder instanceof RVCameraViewHolder){
            ((RVCameraViewHolder) holder).bindView(position);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null?0:mDatas.size();
//        return 10;
    }

    private class RVCameraViewHolder extends RecyclerView.ViewHolder{
        private View mViewRoot;

        public RVCameraViewHolder(View itemView) {
            super(itemView);
            mViewRoot = itemView.findViewById(R.id.item_pick_camera_layout_root);
        }

        void bindView(int position){
            mViewRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemPhotoClickListener != null){
                        onItemPhotoClickListener.onCameraClick();
                    }
                }
            });
        }
    }


    private class RVPhotoGridViewHolder extends RecyclerView.ViewHolder{
        private ImageView mIV;

        public RVPhotoGridViewHolder(View itemView) {
            super(itemView);
            mIV = itemView.findViewById(R.id.item_photo_grid_layout_IV);
        }

        private void bindView(final int position){
            MediaModel bean = mDatas.get(showCamera ? position - 1 : position);
            Glide.with(context).load(bean.getThumPath()).into(mIV);
            mIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemPhotoClickListener != null){
                        onItemPhotoClickListener.onPhotoClick(position);
                    }
                }
            });
        }
    }

    private OnItemPhotoClickListener onItemPhotoClickListener;

    public void setOnItemPhotoClickListener(OnItemPhotoClickListener onItemPhotoClickListener) {
        this.onItemPhotoClickListener = onItemPhotoClickListener;
    }
}
