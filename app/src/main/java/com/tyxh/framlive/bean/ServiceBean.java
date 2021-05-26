package com.tyxh.framlive.bean;

import java.util.List;

public class ServiceBean {

    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : [{"servicePackageId":2,"servicePackagePicUrl":"商品图片url","title":"商品标题","count":100,"time":600}]
     */

    private int retCode;
    private String retMsg;
    private List<RetDataBean> retData;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public List<RetDataBean> getRetData() {
        return retData;
    }

    public void setRetData(List<RetDataBean> retData) {
        this.retData = retData;
    }

    public static class RetDataBean {
        /**
         * servicePackageId : 2
         * servicePackagePicUrl : 商品图片url
         * title : 商品标题
         * count : 100
         * time : 600
         */

        private int servicePackageId;
        private String servicePackagePicUrl;
        private String title;
        private int count;
        private int time;
        private boolean select;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public int getServicePackageId() {
            return servicePackageId;
        }

        public void setServicePackageId(int servicePackageId) {
            this.servicePackageId = servicePackageId;
        }

        public String getServicePackagePicUrl() {
            return servicePackagePicUrl;
        }

        public void setServicePackagePicUrl(String servicePackagePicUrl) {
            this.servicePackagePicUrl = servicePackagePicUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
