package com.werb.pickphotoview.model;

import java.util.ArrayList;

public class PickHolder {
    private static ArrayList<MediaModel> selectedMedia;

    private static ArrayList<String> stringPaths;
    private static PickHolder holder = new PickHolder();

    public static PickHolder getInstance() { return holder; }

    public static PickHolder newInstance()
    {
        stringPaths = null;
        selectedMedia = null;
        holder = new PickHolder();
        return holder;
    }

    public static ArrayList<String> getStringPaths()
    {
        if (stringPaths == null)
            return new ArrayList<String>();
        if (selectedMedia == null){
            return new ArrayList<>();
        }
        return stringPaths;
    }
    public static ArrayList<MediaModel> getMeidas()
    {
        if (selectedMedia == null){
            return new ArrayList<>();
        }
        return selectedMedia;
    }

    public static void setStringPaths(ArrayList<String> stringPaths) { PickHolder.stringPaths = stringPaths; }

    public static void setSelectedMedia(ArrayList<MediaModel> selectedMedia) {
        PickHolder.selectedMedia = selectedMedia;
    }
}
