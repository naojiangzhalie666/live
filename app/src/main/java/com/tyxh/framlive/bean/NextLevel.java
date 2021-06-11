package com.tyxh.framlive.bean;

public class NextLevel {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"id":1,"level":1,"exp":25,"rewardDiaNum":5,"releaseId":2,"rewardReleaseNum":1,"specialEffects":"","specialEffectsType":null,"giftId":1,"rewardGiftNum":1,"createDate":"2021-06-01 10:38:25","modifyDate":"2021-06-01 10:38:25","flag":2,"releaseICO":"http://172.16.66.225/files/mall/20210518/a977f42efdb34057b16102319df2de03.jpg","giftICO":null,"rewardDiaICO":"http://diamond.ico","releaseName":"5分钟咨询卡","giftName":"大拇哥","rewardDiaName":"钻石","curExp":5}
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
         * id : 1
         * level : 1
         * exp : 25
         * rewardDiaNum : 5
         * releaseId : 2
         * rewardReleaseNum : 1
         * specialEffects :
         * specialEffectsType : null
         * giftId : 1
         * rewardGiftNum : 1
         * createDate : 2021-06-01 10:38:25
         * modifyDate : 2021-06-01 10:38:25
         * flag : 2
         * releaseICO : http://172.16.66.225/files/mall/20210518/a977f42efdb34057b16102319df2de03.jpg
         * giftICO : null
         * rewardDiaICO : http://diamond.ico
         * releaseName : 5分钟咨询卡
         * giftName : 大拇哥
         * rewardDiaName : 钻石
         * curExp : 5
         */

        private int id;
        private int level;
        private double exp;
        private int rewardDiaNum;
        private int releaseId;
        private int rewardReleaseNum;
        private String specialEffects;
        private Object specialEffectsType;
        private int giftId;
        private int rewardGiftNum;
        private String createDate;
        private String modifyDate;
        private int flag;
        private String releaseICO;
        private Object giftICO;
        private String rewardDiaICO;
        private String releaseName;
        private String giftName;
        private String rewardDiaName;
        private double curExp;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public double getExp() {
            return exp;
        }

        public void setExp(double exp) {
            this.exp = exp;
        }

        public int getRewardDiaNum() {
            return rewardDiaNum;
        }

        public void setRewardDiaNum(int rewardDiaNum) {
            this.rewardDiaNum = rewardDiaNum;
        }

        public int getReleaseId() {
            return releaseId;
        }

        public void setReleaseId(int releaseId) {
            this.releaseId = releaseId;
        }

        public int getRewardReleaseNum() {
            return rewardReleaseNum;
        }

        public void setRewardReleaseNum(int rewardReleaseNum) {
            this.rewardReleaseNum = rewardReleaseNum;
        }

        public String getSpecialEffects() {
            return specialEffects;
        }

        public void setSpecialEffects(String specialEffects) {
            this.specialEffects = specialEffects;
        }

        public Object getSpecialEffectsType() {
            return specialEffectsType;
        }

        public void setSpecialEffectsType(Object specialEffectsType) {
            this.specialEffectsType = specialEffectsType;
        }

        public int getGiftId() {
            return giftId;
        }

        public void setGiftId(int giftId) {
            this.giftId = giftId;
        }

        public int getRewardGiftNum() {
            return rewardGiftNum;
        }

        public void setRewardGiftNum(int rewardGiftNum) {
            this.rewardGiftNum = rewardGiftNum;
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

        public String getReleaseICO() {
            return releaseICO;
        }

        public void setReleaseICO(String releaseICO) {
            this.releaseICO = releaseICO;
        }

        public Object getGiftICO() {
            return giftICO;
        }

        public void setGiftICO(Object giftICO) {
            this.giftICO = giftICO;
        }

        public String getRewardDiaICO() {
            return rewardDiaICO;
        }

        public void setRewardDiaICO(String rewardDiaICO) {
            this.rewardDiaICO = rewardDiaICO;
        }

        public String getReleaseName() {
            return releaseName;
        }

        public void setReleaseName(String releaseName) {
            this.releaseName = releaseName;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public String getRewardDiaName() {
            return rewardDiaName;
        }

        public void setRewardDiaName(String rewardDiaName) {
            this.rewardDiaName = rewardDiaName;
        }

        public double getCurExp() {
            return curExp;
        }

        public void setCurExp(double curExp) {
            this.curExp = curExp;
        }
    }
}
