package com.tyxh.framlive.bean;

import java.util.List;

public class NoticeBean {

    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"pageNum":0,"pageSize":1,"totalPage":1,"total":1,"list":[{"id":1,"userId":1024859055654637600,"noticeContent":"我举报你了","pageUrl":"123","isRead":2,"createDate":"2021-05-13 16:51:17","modifyDate":"2021-05-13 16:51:17","flag":2}]}
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
         * pageNum : 0
         * pageSize : 1
         * totalPage : 1
         * total : 1
         * list : [{"id":1,"userId":1024859055654637600,"noticeContent":"我举报你了","pageUrl":"123","isRead":2,"createDate":"2021-05-13 16:51:17","modifyDate":"2021-05-13 16:51:17","flag":2}]
         */

        private int pageNum;
        private int pageSize;
        private int totalPage;
        private int total;
        private List<ListBean> list;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 1
             * userId : 1024859055654637600
             * noticeContent : 我举报你了
             * pageUrl : 123
             * isRead : 2
             * createDate : 2021-05-13 16:51:17
             * modifyDate : 2021-05-13 16:51:17
             * flag : 2
             */

            private int id;
            private long userId;
            private String noticeContent;
            private String pageUrl;
            private int isRead;
            private String createDate;
            private String modifyDate;
            private int flag;
            private String ico;

            public String getIco() {
                return ico;
            }

            public void setIco(String ico) {
                this.ico = ico;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public String getNoticeContent() {
                return noticeContent;
            }

            public void setNoticeContent(String noticeContent) {
                this.noticeContent = noticeContent;
            }

            public String getPageUrl() {
                return pageUrl;
            }

            public void setPageUrl(String pageUrl) {
                this.pageUrl = pageUrl;
            }

            public int getIsRead() {
                return isRead;
            }

            public void setIsRead(int isRead) {
                this.isRead = isRead;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getModifyDate() {
                return modifyDate;
            }

            public void setModifyDate(String modifyDate) {
                this.modifyDate = modifyDate;
            }

            public int getFlag() {
                return flag;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }
        }
    }
}
