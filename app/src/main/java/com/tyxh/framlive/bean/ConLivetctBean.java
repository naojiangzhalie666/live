package com.tyxh.framlive.bean;

import java.util.List;

public class ConLivetctBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"total":16,"list":[{"id":42,"userId":11146,"roomId":11147,"uuId":"817ad207-c990-4f72-828c-396cca092184","title":null,"type":null,"startDate":"2021-06-08 17:54:07","endDate":"2021-06-08 17:54:42","isUseProp":2,"remark":null,"createDate":"2021-06-08 17:54:07","modifyDate":"2021-06-08 17:54:42","flag":2,"star":5,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":43,"userId":11146,"roomId":11147,"uuId":"eff35740-f894-4745-9e02-0afac5bf5feb","title":null,"type":null,"startDate":"2021-06-08 18:08:15","endDate":"2021-06-08 18:08:20","isUseProp":2,"remark":null,"createDate":"2021-06-08 18:08:15","modifyDate":"2021-06-08 18:08:20","flag":2,"star":3,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":44,"userId":11119,"roomId":11147,"uuId":"e545f343-b56e-4514-b61b-ea077efd1a94","title":null,"type":1,"startDate":"2021-06-08 19:06:10","endDate":null,"isUseProp":2,"remark":null,"createDate":"2021-06-08 19:06:10","modifyDate":"2021-06-08 19:06:10","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":45,"userId":11148,"roomId":11147,"uuId":"91a237ab-121f-41fe-b9fa-053969e37f36","title":null,"type":null,"startDate":"2021-06-08 19:20:06","endDate":"2021-06-08 19:20:43","isUseProp":2,"remark":null,"createDate":"2021-06-08 19:20:06","modifyDate":"2021-06-08 19:20:43","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":46,"userId":11149,"roomId":11147,"uuId":"e22a3e5d-f0de-44d0-a381-57834448cfc6","title":null,"type":1,"startDate":"2021-06-08 19:33:25","endDate":"2021-06-08 19:34:32","isUseProp":2,"remark":null,"createDate":"2021-06-08 19:33:25","modifyDate":"2021-06-08 19:34:32","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":47,"userId":11119,"roomId":11147,"uuId":"2fc7668f-28de-4e96-93b0-3f1a6a7defc3","title":null,"type":1,"startDate":"2021-06-08 19:56:37","endDate":"2021-06-08 19:57:20","isUseProp":2,"remark":null,"createDate":"2021-06-08 19:56:37","modifyDate":"2021-06-08 19:57:20","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":48,"userId":11119,"roomId":11147,"uuId":"2538e201-1e26-4ae9-a47f-c305b8bb239d","title":null,"type":1,"startDate":"2021-06-08 20:03:25","endDate":"2021-06-08 20:07:03","isUseProp":2,"remark":null,"createDate":"2021-06-08 20:03:25","modifyDate":"2021-06-08 20:07:03","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":49,"userId":11149,"roomId":11147,"uuId":"cbdc4559-431c-4140-ac7d-9de6415bab3c","title":null,"type":1,"startDate":"2021-06-08 20:18:17","endDate":"2021-06-08 20:21:57","isUseProp":2,"remark":null,"createDate":"2021-06-08 20:18:17","modifyDate":"2021-06-08 20:21:57","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":50,"userId":11149,"roomId":11147,"uuId":"1cdb501d-5e47-43ab-8c77-f341e353e7f5","title":null,"type":1,"startDate":"2021-06-08 20:23:21","endDate":"2021-06-08 20:23:23","isUseProp":2,"remark":null,"createDate":"2021-06-08 20:23:21","modifyDate":"2021-06-08 20:23:23","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":51,"userId":11149,"roomId":11147,"uuId":"468a3f77-b76b-408e-bea5-0e5c670e8d8a","title":null,"type":1,"startDate":"2021-06-08 20:24:20","endDate":"2021-06-08 20:28:03","isUseProp":2,"remark":null,"createDate":"2021-06-08 20:24:20","modifyDate":"2021-06-08 20:28:03","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"}],"pageNum":1,"pageSize":10,"size":10,"startRow":1,"endRow":10,"pages":2,"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2],"navigateFirstPage":1,"navigateLastPage":2}
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
         * total : 16
         * list : [{"id":42,"userId":11146,"roomId":11147,"uuId":"817ad207-c990-4f72-828c-396cca092184","title":null,"type":null,"startDate":"2021-06-08 17:54:07","endDate":"2021-06-08 17:54:42","isUseProp":2,"remark":null,"createDate":"2021-06-08 17:54:07","modifyDate":"2021-06-08 17:54:42","flag":2,"star":5,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":43,"userId":11146,"roomId":11147,"uuId":"eff35740-f894-4745-9e02-0afac5bf5feb","title":null,"type":null,"startDate":"2021-06-08 18:08:15","endDate":"2021-06-08 18:08:20","isUseProp":2,"remark":null,"createDate":"2021-06-08 18:08:15","modifyDate":"2021-06-08 18:08:20","flag":2,"star":3,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":44,"userId":11119,"roomId":11147,"uuId":"e545f343-b56e-4514-b61b-ea077efd1a94","title":null,"type":1,"startDate":"2021-06-08 19:06:10","endDate":null,"isUseProp":2,"remark":null,"createDate":"2021-06-08 19:06:10","modifyDate":"2021-06-08 19:06:10","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":45,"userId":11148,"roomId":11147,"uuId":"91a237ab-121f-41fe-b9fa-053969e37f36","title":null,"type":null,"startDate":"2021-06-08 19:20:06","endDate":"2021-06-08 19:20:43","isUseProp":2,"remark":null,"createDate":"2021-06-08 19:20:06","modifyDate":"2021-06-08 19:20:43","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":46,"userId":11149,"roomId":11147,"uuId":"e22a3e5d-f0de-44d0-a381-57834448cfc6","title":null,"type":1,"startDate":"2021-06-08 19:33:25","endDate":"2021-06-08 19:34:32","isUseProp":2,"remark":null,"createDate":"2021-06-08 19:33:25","modifyDate":"2021-06-08 19:34:32","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":47,"userId":11119,"roomId":11147,"uuId":"2fc7668f-28de-4e96-93b0-3f1a6a7defc3","title":null,"type":1,"startDate":"2021-06-08 19:56:37","endDate":"2021-06-08 19:57:20","isUseProp":2,"remark":null,"createDate":"2021-06-08 19:56:37","modifyDate":"2021-06-08 19:57:20","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":48,"userId":11119,"roomId":11147,"uuId":"2538e201-1e26-4ae9-a47f-c305b8bb239d","title":null,"type":1,"startDate":"2021-06-08 20:03:25","endDate":"2021-06-08 20:07:03","isUseProp":2,"remark":null,"createDate":"2021-06-08 20:03:25","modifyDate":"2021-06-08 20:07:03","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":49,"userId":11149,"roomId":11147,"uuId":"cbdc4559-431c-4140-ac7d-9de6415bab3c","title":null,"type":1,"startDate":"2021-06-08 20:18:17","endDate":"2021-06-08 20:21:57","isUseProp":2,"remark":null,"createDate":"2021-06-08 20:18:17","modifyDate":"2021-06-08 20:21:57","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":50,"userId":11149,"roomId":11147,"uuId":"1cdb501d-5e47-43ab-8c77-f341e353e7f5","title":null,"type":1,"startDate":"2021-06-08 20:23:21","endDate":"2021-06-08 20:23:23","isUseProp":2,"remark":null,"createDate":"2021-06-08 20:23:21","modifyDate":"2021-06-08 20:23:23","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"},{"id":51,"userId":11149,"roomId":11147,"uuId":"468a3f77-b76b-408e-bea5-0e5c670e8d8a","title":null,"type":1,"startDate":"2021-06-08 20:24:20","endDate":"2021-06-08 20:28:03","isUseProp":2,"remark":null,"createDate":"2021-06-08 20:24:20","modifyDate":"2021-06-08 20:28:03","flag":2,"star":null,"liveCode":null,"nickname":"机智敏捷的巴士"}]
         * pageNum : 1
         * pageSize : 10
         * size : 10
         * startRow : 1
         * endRow : 10
         * pages : 2
         * prePage : 0
         * nextPage : 2
         * isFirstPage : true
         * isLastPage : false
         * hasPreviousPage : false
         * hasNextPage : true
         * navigatePages : 8
         * navigatepageNums : [1,2]
         * navigateFirstPage : 1
         * navigateLastPage : 2
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
             * id : 42
             * userId : 11146
             * roomId : 11147
             * uuId : 817ad207-c990-4f72-828c-396cca092184
             * title : null
             * type : null
             * startDate : 2021-06-08 17:54:07
             * endDate : 2021-06-08 17:54:42
             * isUseProp : 2
             * remark : null
             * createDate : 2021-06-08 17:54:07
             * modifyDate : 2021-06-08 17:54:42
             * flag : 2
             * star : 5
             * liveCode : null
             * nickname : 机智敏捷的巴士
             */

            private int id;
            private int userId;
            private int roomId;
            private String uuId;
            private Object title;
            private Object type;
            private String startDate;
            private String endDate;
            private int isUseProp;
            private Object remark;
            private String createDate;
            private String modifyDate;
            private int flag;
            private int star;
            private Object liveCode;
            private String nickname;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getRoomId() {
                return roomId;
            }

            public void setRoomId(int roomId) {
                this.roomId = roomId;
            }

            public String getUuId() {
                return uuId;
            }

            public void setUuId(String uuId) {
                this.uuId = uuId;
            }

            public Object getTitle() {
                return title;
            }

            public void setTitle(Object title) {
                this.title = title;
            }

            public Object getType() {
                return type;
            }

            public void setType(Object type) {
                this.type = type;
            }

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

            public int getIsUseProp() {
                return isUseProp;
            }

            public void setIsUseProp(int isUseProp) {
                this.isUseProp = isUseProp;
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

            public int getStar() {
                return star;
            }

            public void setStar(int star) {
                this.star = star;
            }

            public Object getLiveCode() {
                return liveCode;
            }

            public void setLiveCode(Object liveCode) {
                this.liveCode = liveCode;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}
