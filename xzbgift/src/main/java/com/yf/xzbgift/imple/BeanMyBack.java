package com.yf.xzbgift.imple;

import java.util.List;

public class BeanMyBack {

    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : [{"id":1,"imgUrl":"http://127.0.0.1:8081/doc.html","proName":"大拇哥","giftPrice":13,"giftType":null,"lottieUrl":null,"count":3}]
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
         * imgUrl : http://127.0.0.1:8081/doc.html
         * proName : 大拇哥
         * giftPrice : 13
         * giftType : null
         * lottieUrl : null
         * count : 3
         */

        private String id;
        private String imgUrl;
        private String proName;
        private int giftPrice;
        private int giftType;
        private String lottieUrl;
        private int count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getProName() {
            return proName;
        }

        public void setProName(String proName) {
            this.proName = proName;
        }

        public int getGiftPrice() {
            return giftPrice;
        }

        public void setGiftPrice(int giftPrice) {
            this.giftPrice = giftPrice;
        }

        public int getGiftType() {
            return giftType;
        }

        public void setGiftType(int giftType) {
            this.giftType = giftType;
        }

        public String getLottieUrl() {
            return lottieUrl;
        }

        public void setLottieUrl(String lottieUrl) {
            this.lottieUrl = lottieUrl;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
