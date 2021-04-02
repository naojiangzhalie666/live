package com.example.myapplication.bean;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;
import java.util.Map;

public class ZixBean {
    private String name;
    private String xxpath;
    private String stTm;
    private String edTm;
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
        return "ZixBean{" +
                "name='" + name + '\'' +
                ", xxpath='" + xxpath + '\'' +
                ", stTm='" + stTm + '\'' +
                ", edTm='" + edTm + '\'' +
                ", count=" + count +
                '}';
    }
}
