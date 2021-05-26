package com.tyxh.framlive.bean.live_mine;

import java.util.List;

/********************************************************************
  @version: 1.0.0
  @description: 咨询机构入驻  上传实体类
  @author: admin
  @time: 2021/5/12 8:57
  @变更历史:
********************************************************************/
public class ZxjgBean {

    public String userId;       //用户id
    public String meLogo;       //机构logo
    public String meName ;      //机构名称
    public String mePhone;      //机构电话
    public String meAddress;    //机构地址
    public List<String> licenseList;     //营业执照集合
    public String meIntroduce;           //机构简介
    public String other;                 //其它说明--目前传String
    public List<DataBean> couBasicBeList;//咨询师集合


    public static class DataBean{
        public String couHeadImg;
        public String couName;
        public List<String> imageList;
        public List<quaBeList> quaBeList;


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

}
