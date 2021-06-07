package com.tyxh.framlive.bean;

import java.util.List;

public class SjBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"endRow":0,"hasNextPage":true,"hasPreviousPage":true,"isFirstPage":true,"isLastPage":true,"list":[{"createDate":"","endDate":"","flag":0,"id":0,"isUseProp":0,"modifyDate":"","proHistory":{"businessDiaNum":0,"createDate":"","durationConsume":0,"flag":0,"id":0,"modifyDate":"","proId":0,"proName":"","proNum":0,"proType":0,"roomDiaNum":0,"roomId":0,"roomIncome":0,"userId":0,"uuId":""},"remark":"","roomId":0,"star":0,"startDate":"","title":"","type":0,"userId":0,"uuId":""}],"navigateFirstPage":0,"navigateLastPage":0,"navigatePages":0,"navigatepageNums":[],"nextPage":0,"pageNum":0,"pageSize":0,"pages":0,"prePage":0,"size":0,"startRow":0,"total":0}
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
         * endRow : 0
         * hasNextPage : true
         * hasPreviousPage : true
         * isFirstPage : true
         * isLastPage : true
         * list : [{"createDate":"","endDate":"","flag":0,"id":0,"isUseProp":0,"modifyDate":"","proHistory":{"businessDiaNum":0,"createDate":"","durationConsume":0,"flag":0,"id":0,"modifyDate":"","proId":0,"proName":"","proNum":0,"proType":0,"roomDiaNum":0,"roomId":0,"roomIncome":0,"userId":0,"uuId":""},"remark":"","roomId":0,"star":0,"startDate":"","title":"","type":0,"userId":0,"uuId":""}]
         * navigateFirstPage : 0
         * navigateLastPage : 0
         * navigatePages : 0
         * navigatepageNums : []
         * nextPage : 0
         * pageNum : 0
         * pageSize : 0
         * pages : 0
         * prePage : 0
         * size : 0
         * startRow : 0
         * total : 0
         */

        private int endRow;
        private boolean hasNextPage;
        private boolean hasPreviousPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private int navigateFirstPage;
        private int navigateLastPage;
        private int navigatePages;
        private int nextPage;
        private int pageNum;
        private int pageSize;
        private int pages;
        private int prePage;
        private int size;
        private int startRow;
        private int total;
        private List<ListBean> list;
        private List<?> navigatepageNums;

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
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

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
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

        public List<?> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<?> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public static class ListBean {
            /**
             * createDate :
             * endDate :
             * flag : 0
             * id : 0
             * isUseProp : 0
             * modifyDate :
             * proHistory : {"businessDiaNum":0,"createDate":"","durationConsume":0,"flag":0,"id":0,"modifyDate":"","proId":0,"proName":"","proNum":0,"proType":0,"roomDiaNum":0,"roomId":0,"roomIncome":0,"userId":0,"uuId":""}
             * remark :
             * roomId : 0
             * star : 0
             * startDate :
             * title :
             * type : 0
             * userId : 0
             * uuId :
             */

            private String createDate;
            private String endDate;
            private int flag;
            private int id;
            private int isUseProp;
            private String modifyDate;
            private ProHistoryBean proHistory;
            private String remark;
            private int roomId;
            private int star;
            private String startDate;
            private String title;
            private int type;
            private int userId;
            private String uuId;

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public int getFlag() {
                return flag;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIsUseProp() {
                return isUseProp;
            }

            public void setIsUseProp(int isUseProp) {
                this.isUseProp = isUseProp;
            }

            public String getModifyDate() {
                return modifyDate;
            }

            public void setModifyDate(String modifyDate) {
                this.modifyDate = modifyDate;
            }

            public ProHistoryBean getProHistory() {
                return proHistory;
            }

            public void setProHistory(ProHistoryBean proHistory) {
                this.proHistory = proHistory;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getRoomId() {
                return roomId;
            }

            public void setRoomId(int roomId) {
                this.roomId = roomId;
            }

            public int getStar() {
                return star;
            }

            public void setStar(int star) {
                this.star = star;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getUuId() {
                return uuId;
            }

            public void setUuId(String uuId) {
                this.uuId = uuId;
            }

            public static class ProHistoryBean {
                /**
                 * businessDiaNum : 0
                 * createDate :
                 * durationConsume : 0
                 * flag : 0
                 * id : 0
                 * modifyDate :
                 * proId : 0
                 * proName :
                 * proNum : 0
                 * proType : 0
                 * roomDiaNum : 0
                 * roomId : 0
                 * roomIncome : 0
                 * userId : 0
                 * uuId :
                 */

                private int businessDiaNum;
                private String createDate;
                private int durationConsume;
                private int flag;
                private int id;
                private String modifyDate;
                private int proId;
                private String proName;
                private int proNum;
                private int proType;
                private int roomDiaNum;
                private int roomId;
                private int roomIncome;
                private int userId;
                private String uuId;

                public int getBusinessDiaNum() {
                    return businessDiaNum;
                }

                public void setBusinessDiaNum(int businessDiaNum) {
                    this.businessDiaNum = businessDiaNum;
                }

                public String getCreateDate() {
                    return createDate;
                }

                public void setCreateDate(String createDate) {
                    this.createDate = createDate;
                }

                public int getDurationConsume() {
                    return durationConsume;
                }

                public void setDurationConsume(int durationConsume) {
                    this.durationConsume = durationConsume;
                }

                public int getFlag() {
                    return flag;
                }

                public void setFlag(int flag) {
                    this.flag = flag;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getModifyDate() {
                    return modifyDate;
                }

                public void setModifyDate(String modifyDate) {
                    this.modifyDate = modifyDate;
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

                public int getProNum() {
                    return proNum;
                }

                public void setProNum(int proNum) {
                    this.proNum = proNum;
                }

                public int getProType() {
                    return proType;
                }

                public void setProType(int proType) {
                    this.proType = proType;
                }

                public int getRoomDiaNum() {
                    return roomDiaNum;
                }

                public void setRoomDiaNum(int roomDiaNum) {
                    this.roomDiaNum = roomDiaNum;
                }

                public int getRoomId() {
                    return roomId;
                }

                public void setRoomId(int roomId) {
                    this.roomId = roomId;
                }

                public int getRoomIncome() {
                    return roomIncome;
                }

                public void setRoomIncome(int roomIncome) {
                    this.roomIncome = roomIncome;
                }

                public int getUserId() {
                    return userId;
                }

                public void setUserId(int userId) {
                    this.userId = userId;
                }

                public String getUuId() {
                    return uuId;
                }

                public void setUuId(String uuId) {
                    this.uuId = uuId;
                }
            }
        }
    }
}
