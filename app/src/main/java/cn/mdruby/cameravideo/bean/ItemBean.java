package cn.mdruby.cameravideo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Went_Gone on 2018/1/11.
 */
@Entity
public class ItemBean {
    /**
     * ItemNo : 1
     * ItemName : 症状
     * SectionNo : 1
     * SectionName : 患者症状
     * UIType : 10000003
     * ValueUnit : null
     * ItemValue : null
     */
    @Id
    private int ItemNo;
    private String ItemName;
    private int SectionNo;
    private String SectionName;
    private String UIType;
    private String ValueUnit;
    private String ItemValue;
    @Generated(hash = 888186353)
    public ItemBean(int ItemNo, String ItemName, int SectionNo, String SectionName,
            String UIType, String ValueUnit, String ItemValue) {
        this.ItemNo = ItemNo;
        this.ItemName = ItemName;
        this.SectionNo = SectionNo;
        this.SectionName = SectionName;
        this.UIType = UIType;
        this.ValueUnit = ValueUnit;
        this.ItemValue = ItemValue;
    }
    @Generated(hash = 95333960)
    public ItemBean() {
    }
    public int getItemNo() {
        return this.ItemNo;
    }
    public void setItemNo(int ItemNo) {
        this.ItemNo = ItemNo;
    }
    public String getItemName() {
        return this.ItemName;
    }
    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }
    public int getSectionNo() {
        return this.SectionNo;
    }
    public void setSectionNo(int SectionNo) {
        this.SectionNo = SectionNo;
    }
    public String getSectionName() {
        return this.SectionName;
    }
    public void setSectionName(String SectionName) {
        this.SectionName = SectionName;
    }
    public String getUIType() {
        return this.UIType;
    }
    public void setUIType(String UIType) {
        this.UIType = UIType;
    }
    public String getValueUnit() {
        return this.ValueUnit;
    }
    public void setValueUnit(String ValueUnit) {
        this.ValueUnit = ValueUnit;
    }
    public String getItemValue() {
        return this.ItemValue;
    }
    public void setItemValue(String ItemValue) {
        this.ItemValue = ItemValue;
    }
}
