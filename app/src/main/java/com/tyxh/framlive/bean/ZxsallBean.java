package com.tyxh.framlive.bean;

import java.util.List;

public class ZxsallBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"total":5,"list":[{"id":3,"name":"谦虚的铃兰","user_id":null,"interest":"0","interests":"全部","anchorState":0,"createDate":"2021-05-20 09:20:39","type":2},{"id":2,"name":"和蔼的神仙鱼","user_id":null,"interest":"0","interests":"全部","anchorState":0,"createDate":"2021-05-20 09:20:39","type":2},{"id":1,"name":"不乐观的海豚","user_id":null,"interest":"0","interests":"全部","anchorState":0,"createDate":"2021-05-20 09:20:39","type":2},{"id":9,"name":"不好吃的绝妙的热带鱼","user_id":null,"interest":"1,2,4,7","interests":"情感修复,未成年人心理,婚姻家庭,职场问题","anchorState":null,"createDate":"2021-05-19 14:23:45","type":1},{"id":6,"name":"测试修改呢称6","user_id":null,"interest":"1,2,4,7","interests":"情感修复,未成年人心理,婚姻家庭,职场问题","anchorState":null,"createDate":"2021-05-19 14:23:31","type":1}],"pageNum":1,"pageSize":10,"size":5,"startRow":1,"endRow":5,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1}
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
         * total : 5
         * list : [{"id":3,"name":"谦虚的铃兰","user_id":null,"interest":"0","interests":"全部","anchorState":0,"createDate":"2021-05-20 09:20:39","type":2},{"id":2,"name":"和蔼的神仙鱼","user_id":null,"interest":"0","interests":"全部","anchorState":0,"createDate":"2021-05-20 09:20:39","type":2},{"id":1,"name":"不乐观的海豚","user_id":null,"interest":"0","interests":"全部","anchorState":0,"createDate":"2021-05-20 09:20:39","type":2},{"id":9,"name":"不好吃的绝妙的热带鱼","user_id":null,"interest":"1,2,4,7","interests":"情感修复,未成年人心理,婚姻家庭,职场问题","anchorState":null,"createDate":"2021-05-19 14:23:45","type":1},{"id":6,"name":"测试修改呢称6","user_id":null,"interest":"1,2,4,7","interests":"情感修复,未成年人心理,婚姻家庭,职场问题","anchorState":null,"createDate":"2021-05-19 14:23:31","type":1}]
         * pageNum : 1
         * pageSize : 10
         * size : 5
         * startRow : 1
         * endRow : 5
         * pages : 1
         * prePage : 0
         * nextPage : 0
         * isFirstPage : true
         * isLastPage : true
         * hasPreviousPage : false
         * hasNextPage : false
         * navigatePages : 8
         * navigatepageNums : [1]
         * navigateFirstPage : 1
         * navigateLastPage : 1
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
             * id : 3
             * name : 谦虚的铃兰
             * user_id : null
             * interest : 0
             * interests : 全部
             * anchorState : 0
             * createDate : 2021-05-20 09:20:39
             * type : 2
             */

            private int id;
            private String name;
            private int userId;
            private String interest;
            private String interests;
            private int anchorState;
            private String createDate;
            private String logo;
            private int type;

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

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

            public int getUser_id() {
                return userId;
            }

            public void setUser_id(int user_id) {
                this.userId = user_id;
            }

            public String getInterest() {
                return interest;
            }

            public void setInterest(String interest) {
                this.interest = interest;
            }

            public String getInterests() {
                return interests;
            }

            public void setInterests(String interests) {
                this.interests = interests;
            }

            public int getAnchorState() {
                return anchorState;
            }

            public void setAnchorState(int anchorState) {
                this.anchorState = anchorState;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
