package cn.mdruby.pickphotovideoview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

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
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null?0:mDatas.size();
//        return 10;
    }

    private class RVPhotoGridViewHolder extends RecyclerView.ViewHolder{
        private ImageView mIV;

        public RVPhotoGridViewHolder(View itemView) {
            super(itemView);
            mIV = itemView.findViewById(R.id.item_photo_grid_layout_IV);
        }

        private void bindView(int position){
            MediaModel bean = mDatas.get(showCamera ? position - 1 : position);
            Glide.with(context).load(bean.getThumPath()).into(mIV);
        }
    }
}
