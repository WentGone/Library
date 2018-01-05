package cn.mdruby.pickphotovideoview;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Went_Gone on 2017/11/16.
 */

public class MediaModel implements Serializable{
    private String name;
    private File file;
    private String path;
    private String thumPath;
    private long addDate;
    private String mimeType;
    private long duration;
    private String durationStr;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDurationStr() {
        return durationStr;
    }

    public void setDurationStr(String durationStr) {
        this.durationStr = durationStr;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getAddDate() {
        return addDate;
    }

    public void setAddDate(long addDate) {
        this.addDate = addDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumPath() {
        return thumPath;
    }

    public void setThumPath(String thumPath) {
        this.thumPath = thumPath;
    }
}
