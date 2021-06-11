package com.tyxh.framlive.bean;

import java.util.List;

public class TaskBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : [{"taskId":1,"taskName":"触发条件","taskCondition":"1","taskNum":"1","awardJewel":10,"awardEXP":60,"propName":"5分钟咨询卡","awardPropNum":1,"userId":null,"state":1}]
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
         * taskId : 1
         * taskName : 触发条件
         * taskCondition : 1
         * taskNum : 1
         * awardJewel : 10
         * awardEXP : 60
         * propName : 5分钟咨询卡
         * awardPropNum : 1
         * userId : null
         * state : 1
         */

        private int taskId;
        private String taskName;
        private String taskCondition;
        private String taskNum;
        private int awardJewel;
        private int awardEXP;
        private int rewardGiftNum;
        private String propName;
        private String giftName;
        private String taskAwardMoney;
        private int awardPropNum;
        private Object userId;
        private int state;
        private int triggerType;

        public int getTriggerType() {
            return triggerType;
        }

        public void setTriggerType(int triggerType) {
            this.triggerType = triggerType;
        }

        public int getRewardGiftNum() {
            return rewardGiftNum;
        }

        public void setRewardGiftNum(int rewardGiftNum) {
            this.rewardGiftNum = rewardGiftNum;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public String getTaskAwardMoney() {
            return taskAwardMoney;
        }

        public void setTaskAwardMoney(String taskAwardMoney) {
            this.taskAwardMoney = taskAwardMoney;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String getTaskCondition() {
            return taskCondition;
        }

        public void setTaskCondition(String taskCondition) {
            this.taskCondition = taskCondition;
        }

        public String getTaskNum() {
            return taskNum;
        }

        public void setTaskNum(String taskNum) {
            this.taskNum = taskNum;
        }

        public int getAwardJewel() {
            return awardJewel;
        }

        public void setAwardJewel(int awardJewel) {
            this.awardJewel = awardJewel;
        }

        public int getAwardEXP() {
            return awardEXP;
        }

        public void setAwardEXP(int awardEXP) {
            this.awardEXP = awardEXP;
        }

        public String getPropName() {
            return propName;
        }

        public void setPropName(String propName) {
            this.propName = propName;
        }

        public int getAwardPropNum() {
            return awardPropNum;
        }

        public void setAwardPropNum(int awardPropNum) {
            this.awardPropNum = awardPropNum;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
