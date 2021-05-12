package com.example.myapplication.bean;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;
import java.util.Map;

public class ZixBean {
    private String name;
    private String xxpath;
    private String xxUrl;
    private String stTm;
    private String edTm;
    private String stTTm;//用于展示的时间  --开始时间
    private String edTTm;//用于展示的时间  -- 结束时间

    private List<LocalMedia> mLocalMedia;
    private List<Map<String, Object>> mMapList;
    private int count = 0;


    public ZixBean(List<LocalMedia> localMedia) {
        mLocalMedia = localMedia;
    }

    public ZixBean(List<LocalMedia> localMedia, String stTm, String edTm) {
        mLocalMedia = localMedia;
        this.stTm = stTm;
        this.edTm = edTm;
    }

    public ZixBean(List<LocalMedia> localMedia, List<Map<String, Object>> mapList, String stTm, String edTm) {
        mMapList = mapList;
        mLocalMedia = localMedia;
        this.stTm = stTm;
        this.edTm = edTm;
    }

      public ZixBean(List<LocalMedia> localMedia, List<Map<String, Object>> mapList, String stTm, String edTm,String stTTm,String edTTm) {
        mMapList = mapList;
        mLocalMedia = localMedia;
        this.stTm = stTm;
        this.edTm = edTm;
        this.stTTm =stTTm;
        this.edTTm =edTTm;
    }


    public String getXxUrl() {
        return xxUrl;
    }

    public void setXxUrl(String xxUrl) {
        this.xxUrl = xxUrl;
    }

    public String getStTTm() {
        return stTTm;
    }

    public void setStTTm(String stTTm) {
        this.stTTm = stTTm;
    }

    public String getEdTTm() {
        return edTTm;
    }

    public void setEdTTm(String edTTm) {
        this.edTTm = edTTm;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Map<String, Object>> getMapList() {
        return mMapList;
    }

    public void setMapList(List<Map<String, Object>> mapList) {
        mMapList = mapList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXxpath() {
        return xxpath;
    }

    public void setXxpath(String xxpath) {
        this.xxpath = xxpath;
    }

    public String getStTm() {
        return stTm;
    }

    public void setStTm(String stTm) {
        this.stTm = stTm;
    }

    public String getEdTm() {
        return edTm;
    }

    public void setEdTm(String edTm) {
        this.edTm = edTm;
    }

    public List<LocalMedia> getLocalMedia() {
        return mLocalMedia;
    }

    public void setLocalMedia(List<LocalMedia> localMedia) {
        mLocalMedia = localMedia;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", xxpath='" + xxpath + '\'' +
                ", stTm='" + stTm + '\'' +
                ", edTm='" + edTm + '\'' +
                ", count=" + count +
                '}';
    }
}
