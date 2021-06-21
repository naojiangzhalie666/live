package com.tyxh.framlive.bean;

import java.util.List;

public class DiamondBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"total":10,"list":[{"id":8,"proName":"钻石4","platform":"2","proNum":88,"originalPrice":888,"discountType":1,"giveNum":5,"discount":null,"specialOffer":null,"isLimitTimes":1,"limitTimes":9,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":1,"personalLimitAmount":null,"enterpriseLimitAmount":9,"diamondCount":0,"limitAmountType":2,"isShelves":1,"createDate":"2021-05-12 13:08:52","modifyDate":"2021-05-12 13:08:52","flag":2},{"id":30,"proName":"想","platform":"2","proNum":1,"originalPrice":1,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:11:49","modifyDate":"2021-05-19 11:11:49","flag":2},{"id":31,"proName":"1","platform":"2","proNum":1,"originalPrice":111,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:12:30","modifyDate":"2021-05-19 11:12:30","flag":2},{"id":32,"proName":"1","platform":"2","proNum":1,"originalPrice":111,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:12:30","modifyDate":"2021-05-19 11:12:30","flag":2},{"id":33,"proName":"2","platform":"2","proNum":2,"originalPrice":222,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:13:49","modifyDate":"2021-05-19 11:13:49","flag":2},{"id":34,"proName":"3","platform":"2","proNum":33,"originalPrice":333,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:17:29","modifyDate":"2021-05-19 11:17:29","flag":2},{"id":36,"proName":"4","platform":"2","proNum":44,"originalPrice":444,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":2,"limitTimes":null,"isLimitDuration":1,"limitStartDate":"2021-05-19 00:00:00","limitEndDate":"2021-06-29 00:00:00","isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:20:11","modifyDate":"2021-05-19 11:20:11","flag":2},{"id":42,"proName":"5","platform":"2","proNum":5,"originalPrice":555,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":2,"limitTimes":null,"isLimitDuration":1,"limitStartDate":"2021-05-18 00:00:00","limitEndDate":"2021-06-28 00:00:00","isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:24:57","modifyDate":"2021-05-19 11:24:57","flag":2},{"id":44,"proName":"6","platform":"2","proNum":66,"originalPrice":666,"discountType":0,"giveNum":6,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 14:20:07","modifyDate":"2021-05-19 14:20:07","flag":2},{"id":46,"proName":"7","platform":"2","proNum":77,"originalPrice":777,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-20 11:19:57","modifyDate":"2021-05-20 11:19:57","flag":2}],"pageNum":1,"pageSize":100,"size":10,"startRow":1,"endRow":10,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1}
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
         * total : 10
         * list : [{"id":8,"proName":"钻石4","platform":"2","proNum":88,"originalPrice":888,"discountType":1,"giveNum":5,"discount":null,"specialOffer":null,"isLimitTimes":1,"limitTimes":9,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":1,"personalLimitAmount":null,"enterpriseLimitAmount":9,"diamondCount":0,"limitAmountType":2,"isShelves":1,"createDate":"2021-05-12 13:08:52","modifyDate":"2021-05-12 13:08:52","flag":2},{"id":30,"proName":"想","platform":"2","proNum":1,"originalPrice":1,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:11:49","modifyDate":"2021-05-19 11:11:49","flag":2},{"id":31,"proName":"1","platform":"2","proNum":1,"originalPrice":111,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:12:30","modifyDate":"2021-05-19 11:12:30","flag":2},{"id":32,"proName":"1","platform":"2","proNum":1,"originalPrice":111,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:12:30","modifyDate":"2021-05-19 11:12:30","flag":2},{"id":33,"proName":"2","platform":"2","proNum":2,"originalPrice":222,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:13:49","modifyDate":"2021-05-19 11:13:49","flag":2},{"id":34,"proName":"3","platform":"2","proNum":33,"originalPrice":333,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:17:29","modifyDate":"2021-05-19 11:17:29","flag":2},{"id":36,"proName":"4","platform":"2","proNum":44,"originalPrice":444,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":2,"limitTimes":null,"isLimitDuration":1,"limitStartDate":"2021-05-19 00:00:00","limitEndDate":"2021-06-29 00:00:00","isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:20:11","modifyDate":"2021-05-19 11:20:11","flag":2},{"id":42,"proName":"5","platform":"2","proNum":5,"originalPrice":555,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":2,"limitTimes":null,"isLimitDuration":1,"limitStartDate":"2021-05-18 00:00:00","limitEndDate":"2021-06-28 00:00:00","isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 11:24:57","modifyDate":"2021-05-19 11:24:57","flag":2},{"id":44,"proName":"6","platform":"2","proNum":66,"originalPrice":666,"discountType":0,"giveNum":6,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-19 14:20:07","modifyDate":"2021-05-19 14:20:07","flag":2},{"id":46,"proName":"7","platform":"2","proNum":77,"originalPrice":777,"discountType":null,"giveNum":null,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":1,"createDate":"2021-05-20 11:19:57","modifyDate":"2021-05-20 11:19:57","flag":2}]
         * pageNum : 1
         * pageSize : 100
         * size : 10
         * startRow : 1
         * endRow : 10
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
             * id : 8
             * proName : 钻石4
             * platform : 2
             * proNum : 88
             * originalPrice : 888
             * discountType : 1
             * giveNum : 5
             * discount : null
             * specialOffer : null
             * isLimitTimes : 1
             * limitTimes : 9
             * isLimitDuration : 2
             * limitStartDate : null
             * limitEndDate : null
             * isLimitAmount : 1
             * personalLimitAmount : null
             * enterpriseLimitAmount : 9
             * diamondCount : 0
             * limitAmountType : 2
             * isShelves : 1
             * createDate : 2021-05-12 13:08:52
             * modifyDate : 2021-05-12 13:08:52
             * flag : 2
             */

            private int id;
            private String proName;
            private String platform;
            private int proNum;
            private String originalPrice;
            private int discountType;
            private int giveNum;
            private Object discount;
            private Object specialOffer;
            private int isLimitTimes;
            private int limitTimes;
            private int isLimitDuration;
            private Object limitStartDate;
            private Object limitEndDate;
            private int isLimitAmount;
            private Object personalLimitAmount;
            private int enterpriseLimitAmount;
            private int diamondCount;
            private int limitAmountType;
            private int isShelves;
            private int firstCharge;
            private String createDate;
            private String modifyDate;
            private int flag;
            private boolean select;

            public int getFirstCharge() {
                return firstCharge;
            }

            public void setFirstCharge(int firstCharge) {
                this.firstCharge = firstCharge;
            }

            public boolean isSelect() {
                return select;
            }

            public void setSelect(boolean select) {
                this.select = select;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getProName() {
                return proName;
            }

            public void setProName(String proName) {
                this.proName = proName;
            }

            public String getPlatform() {
                return platform;
            }

            public void setPlatform(String platform) {
                this.platform = platform;
            }

            public int getProNum() {
                return proNum;
            }

            public void setProNum(int proNum) {
                this.proNum = proNum;
            }

            public String getOriginalPrice() {
                return originalPrice;
            }

            public void setOriginalPrice(String originalPrice) {
                this.originalPrice = originalPrice;
            }

            public int getDiscountType() {
                return discountType;
            }

            public void setDiscountType(int discountType) {
                this.discountType = discountType;
            }

            public int getGiveNum() {
                return giveNum;
            }

            public void setGiveNum(int giveNum) {
                this.giveNum = giveNum;
            }

            public Object getDiscount() {
                return discount;
            }

            public void setDiscount(Object discount) {
                this.discount = discount;
            }

            public Object getSpecialOffer() {
                return specialOffer;
            }

            public void setSpecialOffer(Object specialOffer) {
                this.specialOffer = specialOffer;
            }

            public int getIsLimitTimes() {
                return isLimitTimes;
            }

            public void setIsLimitTimes(int isLimitTimes) {
                this.isLimitTimes = isLimitTimes;
            }

            public int getLimitTimes() {
                return limitTimes;
            }

            public void setLimitTimes(int limitTimes) {
                this.limitTimes = limitTimes;
            }

            public int getIsLimitDuration() {
                return isLimitDuration;
            }

            public void setIsLimitDuration(int isLimitDuration) {
                this.isLimitDuration = isLimitDuration;
            }

            public Object getLimitStartDate() {
                return limitStartDate;
            }

            public void setLimitStartDate(Object limitStartDate) {
                this.limitStartDate = limitStartDate;
            }

            public Object getLimitEndDate() {
                return limitEndDate;
            }

            public void setLimitEndDate(Object limitEndDate) {
                this.limitEndDate = limitEndDate;
            }

            public int getIsLimitAmount() {
                return isLimitAmount;
            }

            public void setIsLimitAmount(int isLimitAmount) {
                this.isLimitAmount = isLimitAmount;
            }

            public Object getPersonalLimitAmount() {
                return personalLimitAmount;
            }

            public void setPersonalLimitAmount(Object personalLimitAmount) {
                this.personalLimitAmount = personalLimitAmount;
            }

            public int getEnterpriseLimitAmount() {
                return enterpriseLimitAmount;
            }

            public void setEnterpriseLimitAmount(int enterpriseLimitAmount) {
                this.enterpriseLimitAmount = enterpriseLimitAmount;
            }

            public int getDiamondCount() {
                return diamondCount;
            }

            public void setDiamondCount(int diamondCount) {
                this.diamondCount = diamondCount;
            }

            public int getLimitAmountType() {
                return limitAmountType;
            }

            public void setLimitAmountType(int limitAmountType) {
                this.limitAmountType = limitAmountType;
            }

            public int getIsShelves() {
                return isShelves;
            }

            public void setIsShelves(int isShelves) {
                this.isShelves = isShelves;
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
