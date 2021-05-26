package com.tyxh.framlive.bean;

public class UploadBean {

    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"url":"http://172.16.66.225/files/mall/20210510/074ba2eaf15e41e88206a7dfc5d4ccf9.jpg","name":"074ba2eaf15e41e88206a7dfc5d4ccf9.jpg"}
     */

    private int retCode;
    private String retMsg;
    private RetDataBean retData;

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

    public RetDataBean getRetData() {
        return retData;
    }

    public void setRetData(RetDataBean retData) {
        this.retData = retData;
    }

    public static class RetDataBean {
        /**
         * url : http://172.16.66.225/files/mall/20210510/074ba2eaf15e41e88206a7dfc5d4ccf9.jpg
         * name : 074ba2eaf15e41e88206a7dfc5d4ccf9.jpg
         */

        private String url;
        private String name;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
