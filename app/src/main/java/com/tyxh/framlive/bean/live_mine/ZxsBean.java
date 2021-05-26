package com.tyxh.framlive.bean.live_mine;

import java.util.List;

/********************************************************************
  @version: 1.0.0
  @description: 咨询师入驻上传实体类
  @author: admin
  @time: 2021/5/12 8:57
  @变更历史:
********************************************************************/
public class ZxsBean {
    public String couHeadImg, frontIdPhoto, behindIdPhoto;
    public String couName, perIntroduce;
    public String userId;
    public List<String> imageList;
    public List<String> bdaList;
    public List<quaBeList> quaBeList;
    public String other = "";


    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public void setBdaList(List<String> bdaList) {
        this.bdaList = bdaList;
    }

    public void setQuaBeList(List<ZxsBean.quaBeList> quaBeList) {
        this.quaBeList = quaBeList;
    }


    public static class quaBeList{
        private String startDate;//开始时间
        private String endDate;//结束时间
        private String  imgUrl;//图片url
        private String  quaExplain;//资质说明

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getQuaExplain() {
            return quaExplain;
        }

        public void setQuaExplain(String quaExplain) {
            this.quaExplain = quaExplain;
        }
    }
}
