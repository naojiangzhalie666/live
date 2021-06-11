package com.tyxh.framlive.bean;

import java.util.List;

public class MxdetailBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"total":29,"list":[{"id":243,"amount":0.16,"userId":11147,"sourceUserId":null,"moneyType":1,"orderNum":null,"proId":0,"proConType":null,"proName":null,"proNum":null,"diaNum":null,"durationConsume":null,"uuId":null,"liveCode":null,"platform":2,"totalAmount":0.4,"proType":2,"remark":null,"createDate":"2021-06-09 11:04:57","modifyDate":null,"flag":null,"title":"连线收益"},{"id":484,"amount":0.12,"userId":11147,"sourceUserId":null,"moneyType":1,"orderNum":null,"proId":0,"proConType":null,"proName":null,"proNum":null,"diaNum":null,"durationConsume":null,"uuId":null,"liveCode":null,"platform":2,"totalAmount":0.3,"proType":2,"remark":null,"createDate":"2021-06-09 11:06:57","modifyDate":null,"flag":null,"title":"连线收益"},{"id":700,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208354206","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"b5eb4c54-1ec0-4930-83d7-2bd56b922d2c","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:34","modifyDate":"2021-06-09 11:12:34","flag":2,"title":"礼物收益"},{"id":701,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208354374","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"2f41daf8-cbe4-4fef-896b-e40942cc6ccf","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:34","modifyDate":"2021-06-09 11:12:34","flag":2,"title":"礼物收益"},{"id":702,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208354513","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"ca4a58d8-fb40-434c-9039-59e217e24529","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:35","modifyDate":"2021-06-09 11:12:35","flag":2,"title":"礼物收益"},{"id":703,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208354734","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"846c06b3-1d06-4fe4-aa1e-4b0fc6a3ddd0","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:35","modifyDate":"2021-06-09 11:12:35","flag":2,"title":"礼物收益"},{"id":704,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208354922","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"ac18bc68-994a-423a-b7a2-f13df6ddc34e","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:35","modifyDate":"2021-06-09 11:12:35","flag":2,"title":"礼物收益"},{"id":705,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208355027","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"918666c4-b81f-4e04-a562-04bda9c058b5","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:35","modifyDate":"2021-06-09 11:12:35","flag":2,"title":"礼物收益"},{"id":706,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208355229","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"d98e73de-7788-4a43-8965-5bc96a4f0cfd","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:35","modifyDate":"2021-06-09 11:12:35","flag":2,"title":"礼物收益"},{"id":707,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208355433","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"d70d3aad-c644-40ff-8a12-825541019462","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:35","modifyDate":"2021-06-09 11:12:35","flag":2,"title":"礼物收益"}],"pageNum":1,"pageSize":10,"size":10,"startRow":1,"endRow":10,"pages":3,"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2,3],"navigateFirstPage":1,"navigateLastPage":3}
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
         * total : 29
         * list : [{"id":243,"amount":0.16,"userId":11147,"sourceUserId":null,"moneyType":1,"orderNum":null,"proId":0,"proConType":null,"proName":null,"proNum":null,"diaNum":null,"durationConsume":null,"uuId":null,"liveCode":null,"platform":2,"totalAmount":0.4,"proType":2,"remark":null,"createDate":"2021-06-09 11:04:57","modifyDate":null,"flag":null,"title":"连线收益"},{"id":484,"amount":0.12,"userId":11147,"sourceUserId":null,"moneyType":1,"orderNum":null,"proId":0,"proConType":null,"proName":null,"proNum":null,"diaNum":null,"durationConsume":null,"uuId":null,"liveCode":null,"platform":2,"totalAmount":0.3,"proType":2,"remark":null,"createDate":"2021-06-09 11:06:57","modifyDate":null,"flag":null,"title":"连线收益"},{"id":700,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208354206","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"b5eb4c54-1ec0-4930-83d7-2bd56b922d2c","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:34","modifyDate":"2021-06-09 11:12:34","flag":2,"title":"礼物收益"},{"id":701,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208354374","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"2f41daf8-cbe4-4fef-896b-e40942cc6ccf","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:34","modifyDate":"2021-06-09 11:12:34","flag":2,"title":"礼物收益"},{"id":702,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208354513","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"ca4a58d8-fb40-434c-9039-59e217e24529","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:35","modifyDate":"2021-06-09 11:12:35","flag":2,"title":"礼物收益"},{"id":703,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208354734","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"846c06b3-1d06-4fe4-aa1e-4b0fc6a3ddd0","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:35","modifyDate":"2021-06-09 11:12:35","flag":2,"title":"礼物收益"},{"id":704,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208354922","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"ac18bc68-994a-423a-b7a2-f13df6ddc34e","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:35","modifyDate":"2021-06-09 11:12:35","flag":2,"title":"礼物收益"},{"id":705,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208355027","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"918666c4-b81f-4e04-a562-04bda9c058b5","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:35","modifyDate":"2021-06-09 11:12:35","flag":2,"title":"礼物收益"},{"id":706,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208355229","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"d98e73de-7788-4a43-8965-5bc96a4f0cfd","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:35","modifyDate":"2021-06-09 11:12:35","flag":2,"title":"礼物收益"},{"id":707,"amount":0.48,"userId":11147,"sourceUserId":11149,"moneyType":1,"orderNum":"1623208355433","proId":17,"proConType":1,"proName":"1","proNum":1,"diaNum":12,"durationConsume":null,"uuId":"d70d3aad-c644-40ff-8a12-825541019462","liveCode":null,"platform":null,"totalAmount":1.2,"proType":1,"remark":null,"createDate":"2021-06-09 11:12:35","modifyDate":"2021-06-09 11:12:35","flag":2,"title":"礼物收益"}]
         * pageNum : 1
         * pageSize : 10
         * size : 10
         * startRow : 1
         * endRow : 10
         * pages : 3
         * prePage : 0
         * nextPage : 2
         * isFirstPage : true
         * isLastPage : false
         * hasPreviousPage : false
         * hasNextPage : true
         * navigatePages : 8
         * navigatepageNums : [1,2,3]
         * navigateFirstPage : 1
         * navigateLastPage : 3
         */

        private int total;
        private int pageNum;
        private int pageSize;
        private int size;
        private int startRow;
        private int endRow;
        private int pages;
        private int prePage;
        private int nextPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private int navigatePages;
        private int navigateFirstPage;
        private int navigateLastPage;
        private List<ListBean> list;
        private List<Integer> navigatepageNums;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

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

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public int getNavigateFirstPage() {
            return navigateFirstPage;
        }

        public void setNavigateFirstPage(int navigateFirstPage) {
            this.navigateFirstPage = navigateFirstPage;
        }

        public int getNavigateLastPage() {
            return navigateLastPage;
        }

        public void setNavigateLastPage(int navigateLastPage) {
            this.navigateLastPage = navigateLastPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public static class ListBean {
            /**
             * id : 243
             * amount : 0.16
             * userId : 11147
             * sourceUserId : null
             * moneyType : 1
             * orderNum : null
             * proId : 0
             * proConType : null
             * proName : null
             * proNum : null
             * diaNum : null
             * durationConsume : null
             * uuId : null
             * liveCode : null
             * platform : 2
             * totalAmount : 0.4
             * proType : 2
             * remark : null
             * createDate : 2021-06-09 11:04:57
             * modifyDate : null
             * flag : null
             * title : 连线收益
             */

            private int id;
            private double amount;
            private int userId;
            private Object sourceUserId;
            private int moneyType;
            private Object orderNum;
            private int proId;
            private Object proConType;
            private Object proName;
            private Object proNum;
            private Object diaNum;
            private Object durationConsume;
            private Object uuId;
            private Object liveCode;
            private int platform;
            private double totalAmount;
            private int proType;
            private Object remark;
            private String createDate;
            private Object modifyDate;
            private Object flag;
            private String title;
            private boolean show;

            public boolean isShow() {
                return show;
            }

            public void setShow(boolean show) {
                this.show = show;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public Object getSourceUserId() {
                return sourceUserId;
            }

            public void setSourceUserId(Object sourceUserId) {
                this.sourceUserId = sourceUserId;
            }

            public int getMoneyType() {
                return moneyType;
            }

            public void setMoneyType(int moneyType) {
                this.moneyType = moneyType;
            }

            public Object getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(Object orderNum) {
                this.orderNum = orderNum;
            }

            public int getProId() {
                return proId;
            }

            public void setProId(int proId) {
                this.proId = proId;
            }

            public Object getProConType() {
                return proConType;
            }

            public void setProConType(Object proConType) {
                this.proConType = proConType;
            }

            public Object getProName() {
                return proName;
            }

            public void setProName(Object proName) {
                this.proName = proName;
            }

            public Object getProNum() {
                return proNum;
            }

            public void setProNum(Object proNum) {
                this.proNum = proNum;
            }

            public Object getDiaNum() {
                return diaNum;
            }

            public void setDiaNum(Object diaNum) {
                this.diaNum = diaNum;
            }

            public Object getDurationConsume() {
                return durationConsume;
            }

            public void setDurationConsume(Object durationConsume) {
                this.durationConsume = durationConsume;
            }

            public Object getUuId() {
                return uuId;
            }

            public void setUuId(Object uuId) {
                this.uuId = uuId;
            }

            public Object getLiveCode() {
                return liveCode;
            }

            public void setLiveCode(Object liveCode) {
                this.liveCode = liveCode;
            }

            public int getPlatform() {
                return platform;
            }

            public void setPlatform(int platform) {
                this.platform = platform;
            }

            public double getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(double totalAmount) {
                this.totalAmount = totalAmount;
            }

            public int getProType() {
                return proType;
            }

            public void setProType(int proType) {
                this.proType = proType;
            }

            public Object getRemark() {
                return remark;
            }

            public void setRemark(Object remark) {
                this.remark = remark;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public Object getModifyDate() {
                return modifyDate;
            }

            public void setModifyDate(Object modifyDate) {
                this.modifyDate = modifyDate;
            }

            public Object getFlag() {
                return flag;
            }

            public void setFlag(Object flag) {
                this.flag = flag;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
