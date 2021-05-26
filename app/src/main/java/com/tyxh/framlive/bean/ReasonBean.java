package com.tyxh.framlive.bean;

import java.util.List;

public class ReasonBean {

    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : [{"reportReasonId":1,"reportReason":"色情暴力"},{"reportReasonId":2,"reportReason":"敲诈欺诈"},{"reportReasonId":3,"reportReason":"恶意骚扰"},{"reportReasonId":4,"reportReason":"侮辱谩骂"},{"reportReasonId":5,"reportReason":"引导私联"},{"reportReasonId":6,"reportReason":"其他"}]
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
         * reportReasonId : 1
         * reportReason : 色情暴力
         */

        private int reportReasonId;
        private String reportReason;

        public int getReportReasonId() {
            return reportReasonId;
        }

        public void setReportReasonId(int reportReasonId) {
            this.reportReasonId = reportReasonId;
        }

        public String getReportReason() {
            return reportReason;
        }

        public void setReportReason(String reportReason) {
            this.reportReason = reportReason;
        }
    }
}
