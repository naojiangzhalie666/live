package com.tyxh.framlive.bean;

import java.io.Serializable;
import java.util.List;

public class LiveCotctBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : [{"duration":0,"proType":2,"diaNum":0,"proId":0,"proName":"道具"},{"duration":0,"proType":3,"diaNum":0,"proId":0,"proName":"服务包"},{"duration":179692,"proType":4,"diaNum":89846,"proName":"钻石"}]
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

    public static class RetDataBean implements Serializable {
        /**
         * duration : 0
         * proType : 2
         * diaNum : 0
         * proId : 0
         * proName : 道具
         */

        private long duration;
        private int proType;
        private int diaNum;
        private int proId;
        private String proName;

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public int getProType() {
            return proType;
        }

        public void setProType(int proType) {
            this.proType = proType;
        }

        public int getDiaNum() {
            return diaNum;
        }

        public void setDiaNum(int diaNum) {
            this.diaNum = diaNum;
        }

        public int getProId() {
            return proId;
        }

        public void setProId(int proId) {
            this.proId = proId;
        }

        public String getProName() {
            return proName;
        }

        public void setProName(String proName) {
            this.proName = proName;
        }
    }
}
