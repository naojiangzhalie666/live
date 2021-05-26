package com.tyxh.framlive.bean;

import java.util.List;

public class AgeBean {

    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : [{"id":1,"name":"05后"},{"id":2,"name":"00后"},{"id":3,"name":"95后"},{"id":4,"name":"90后"},{"id":5,"name":"85后"},{"id":6,"name":"80后"},{"id":7,"name":"75后"},{"id":8,"name":"70后"},{"id":9,"name":"60后"}]
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
         * id : 1
         * name : 05后
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
