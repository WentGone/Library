package cn.mdruby.pickphotovideoview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Went_Gone on 2018/1/5.
 */

public class PickPhotoView {
    private Context context;
    private PickData pickData;

    public PickPhotoView(Bulid bulid){
        pickData = bulid.pickData;
        context = bulid.context;
    }

    private void startPickActivity(){
        Intent intent = new Intent(context,PickPhotoActivity.class);
        intent.putExtra(PickPhotoActivity.PICK_DATA,pickData);
        ((Activity)context).startActivityForResult(intent,11);
    }


    public static class Bulid{
        private PickData pickData;
        private Context context;

        public Bulid(Context context) {
            this.context = context;
        }

        public void showCamera(boolean showCamera){
            pickData.setShowCamera(showCamera);
        }

        private PickPhotoView create(){
            return new PickPhotoView(this);
        }

        public void start(){
            create().startPickActivity();
        }
    }
}
