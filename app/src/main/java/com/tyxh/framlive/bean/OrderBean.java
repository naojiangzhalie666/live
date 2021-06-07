package com.tyxh.framlive.bean;

import java.util.List;

public class OrderBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"total":1,"list":[{"id":165,"orderSn":"1622771534313","userId":11114,"diamondId":46,"amount":777,"orderType":1,"orderStatus":3,"tradeNo":null,"remark":"钻石包购买","createDate":"2021-06-04 09:52:14","modifyDate":"2021-06-04 10:07:14","flag":2,"giveNum":0,"sourceNum":77,"credential":null,"payType":null,"diamondBag":{"id":46,"proName":"7","platform":"2","proNum":77,"originalPrice":777,"discountType":0,"giveNum":0,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":2,"createDate":"2021-05-20 11:19:57","modifyDate":"2021-05-20 11:19:57","flag":2}}],"pageNum":1,"pageSize":10,"size":1,"startRow":1,"endRow":1,"pages":1,"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1}
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
         * total : 1
         * list : [{"id":165,"orderSn":"1622771534313","userId":11114,"diamondId":46,"amount":777,"orderType":1,"orderStatus":3,"tradeNo":null,"remark":"钻石包购买","createDate":"2021-06-04 09:52:14","modifyDate":"2021-06-04 10:07:14","flag":2,"giveNum":0,"sourceNum":77,"credential":null,"payType":null,"diamondBag":{"id":46,"proName":"7","platform":"2","proNum":77,"originalPrice":777,"discountType":0,"giveNum":0,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":2,"createDate":"2021-05-20 11:19:57","modifyDate":"2021-05-20 11:19:57","flag":2}}]
         * pageNum : 1
         * pageSize : 10
         * size : 1
         * startRow : 1
         * endRow : 1
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
             * id : 165
             * orderSn : 1622771534313
             * userId : 11114
             * diamondId : 46
             * amount : 777
             * orderType : 1
             * orderStatus : 3
             * tradeNo : null
             * remark : 钻石包购买
             * createDate : 2021-06-04 09:52:14
             * modifyDate : 2021-06-04 10:07:14
             * flag : 2
             * giveNum : 0
             * sourceNum : 77
             * credential : null
             * payType : null
             * diamondBag : {"id":46,"proName":"7","platform":"2","proNum":77,"originalPrice":777,"discountType":0,"giveNum":0,"discount":null,"specialOffer":null,"isLimitTimes":null,"limitTimes":null,"isLimitDuration":2,"limitStartDate":null,"limitEndDate":null,"isLimitAmount":null,"personalLimitAmount":null,"enterpriseLimitAmount":null,"diamondCount":0,"limitAmountType":null,"isShelves":2,"createDate":"2021-05-20 11:19:57","modifyDate":"2021-05-20 11:19:57","flag":2}
             */

            private int id;
            private String orderSn;
            private int userId;
            private int diamondId;
            private double amount;
            private int orderType;
            private int orderStatus;
            private Object tradeNo;
            private String remark;
            private String createDate;
            private String modifyDate;
            private int flag;
            private int giveNum;
            private int sourceNum;
            private Object credential;
            private Object payType;
            private DiamondBagBean diamondBag;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrderSn() {
                return orderSn;
            }

            public void setOrderSn(String orderSn) {
                this.orderSn = orderSn;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getDiamondId() {
                return diamondId;
            }

            public void setDiamondId(int diamondId) {
                this.diamondId = diamondId;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public int getOrderType() {
                return orderType;
            }

            public void setOrderType(int orderType) {
                this.orderType = orderType;
            }

            public int getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(int orderStatus) {
                this.orderStatus = orderStatus;
            }

            public Object getTradeNo() {
                return tradeNo;
            }

            public void setTradeNo(Object tradeNo) {
                this.tradeNo = tradeNo;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
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

            public int getGiveNum() {
                return giveNum;
            }

            public void setGiveNum(int giveNum) {
                this.giveNum = giveNum;
            }

            public int getSourceNum() {
                return sourceNum;
            }

            public void setSourceNum(int sourceNum) {
                this.sourceNum = sourceNum;
            }

            public Object getCredential() {
                return credential;
            }

            public void setCredential(Object credential) {
                this.credential = credential;
            }

            public Object getPayType() {
                return payType;
            }

            public void setPayType(Object payType) {
                this.payType = payType;
            }

            public DiamondBagBean getDiamondBag() {
                return diamondBag;
            }

            public void setDiamondBag(DiamondBagBean diamondBag) {
                this.diamondBag = diamondBag;
            }

            public static class DiamondBagBean {
                /**
                 * id : 46
                 * proName : 7
                 * platform : 2
                 * proNum : 77
                 * originalPrice : 777
                 * discountType : 0
                 * giveNum : 0
                 * discount : null
                 * specialOffer : null
                 * isLimitTimes : null
                 * limitTimes : null
                 * isLimitDuration : 2
                 * limitStartDate : null
                 * limitEndDate : null
                 * isLimitAmount : null
                 * personalLimitAmount : null
                 * enterpriseLimitAmount : null
                 * diamondCount : 0
                 * limitAmountType : null
                 * isShelves : 2
                 * createDate : 2021-05-20 11:19:57
                 * modifyDate : 2021-05-20 11:19:57
                 * flag : 2
                 */

                private int id;
                private String proName;
                private String platform;
                private int proNum;
                private double originalPrice;
                private int discountType;
                private int giveNum;
                private Object discount;
                private Object specialOffer;
                private Object isLimitTimes;
                private Object limitTimes;
                private int isLimitDuration;
                private Object limitStartDate;
                private Object limitEndDate;
                private Object isLimitAmount;
                private Object personalLimitAmount;
                private Object enterpriseLimitAmount;
                private int diamondCount;
                private Object limitAmountType;
                private int isShelves;
                private String createDate;
                private String modifyDate;
                private int flag;

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

                public double getOriginalPrice() {
                    return originalPrice;
                }

                public void setOriginalPrice(double originalPrice) {
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

                public Object getIsLimitTimes() {
                    return isLimitTimes;
                }

                public void setIsLimitTimes(Object isLimitTimes) {
                    this.isLimitTimes = isLimitTimes;
                }

                public Object getLimitTimes() {
                    return limitTimes;
                }

                public void setLimitTimes(Object limitTimes) {
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

                public Object getIsLimitAmount() {
                    return isLimitAmount;
                }

                public void setIsLimitAmount(Object isLimitAmount) {
                    this.isLimitAmount = isLimitAmount;
                }

                public Object getPersonalLimitAmount() {
                    return personalLimitAmount;
                }

                public void setPersonalLimitAmount(Object personalLimitAmount) {
                    this.personalLimitAmount = personalLimitAmount;
                }

                public Object getEnterpriseLimitAmount() {
                    return enterpriseLimitAmount;
                }

                public void setEnterpriseLimitAmount(Object enterpriseLimitAmount) {
                    this.enterpriseLimitAmount = enterpriseLimitAmount;
                }

                public int getDiamondCount() {
                    return diamondCount;
                }

                public void setDiamondCount(int diamondCount) {
                    this.diamondCount = diamondCount;
                }

                public Object getLimitAmountType() {
                    return limitAmountType;
                }

                public void setLimitAmountType(Object limitAmountType) {
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
}
