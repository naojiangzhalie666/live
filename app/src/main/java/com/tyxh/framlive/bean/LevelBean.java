package com.tyxh.framlive.bean;

import java.util.List;

public class LevelBean {
    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"user":{"id":11114,"type":2,"username":"1400330500043702272","ico":"http://172.16.66.225/files/mall/20210603/2cd4eb3e60f2420991ccbf198c60fd1d.jpg","nickname":"别致的熊猫","countryCode":null,"mobile":"18920435270","interest":"1,2,4,3,5","password":"$2a$10$bqTlE1ug1tOvwApRrTN5UOp1yuVEgx1J.oLupRmTfgFXldqu6GU/6","paypassword":null,"gender":1,"paypassSetting":0,"age":4,"email":null,"realName":null,"idCardType":null,"authStatus":0,"idCard":null,"level":1,"authtime":null,"logins":null,"status":1,"inviteCode":"W4CLVIAUXIW1","inviteRelation":null,"directInviteid":null,"lastUpdateTime":"2021-06-03 15:40:07","created":"2021-06-03 13:56:23","unionId":"oYm5k69Ij9_KHvoDzZMD4hnRWMl8","iosId":null,"diamond":10,"balance":0.08,"parentAccount":null,"emergencyContact":null,"fansNum":null,"inviteNum":null,"exp":5,"initials":"b","auditState":2,"bankCard":null},"nextLevel":{"id":1,"level":1,"exp":25,"rewardDiaNum":5,"releaseId":2,"rewardReleaseNum":1,"specialEffects":"","specialEffectsType":null,"giftId":1,"rewardGiftNum":1,"createDate":"2021-06-01 10:38:25","modifyDate":"2021-06-01 10:38:25","flag":2},"rankPrivilegeBeans":[{"id":1,"level":1,"exp":25,"rewardDiaNum":5,"releaseId":2,"rewardReleaseNum":1,"specialEffects":"","specialEffectsType":null,"giftId":1,"rewardGiftNum":1,"createDate":"2021-06-01 10:38:25","modifyDate":"2021-06-01 10:38:25","flag":2,"releaseICO":"http://172.16.66.225/files/mall/20210518/a977f42efdb34057b16102319df2de03.jpg","giftICO":null,"rewardDiaICO":"http://diamond.ico"},{"id":2,"level":2,"exp":50,"rewardDiaNum":8,"releaseId":3,"rewardReleaseNum":1,"specialEffects":"","specialEffectsType":null,"giftId":2,"rewardGiftNum":1,"createDate":"2021-06-01 10:39:15","modifyDate":"2021-06-01 10:39:15","flag":2,"releaseICO":"http://172.16.66.225/files/mall/20210513/7023c4b742a540bd9bc674112768e7d0.jpg","giftICO":null,"rewardDiaICO":"http://diamond.ico"},{"id":3,"level":3,"exp":10000000,"rewardDiaNum":8,"releaseId":3,"rewardReleaseNum":1,"specialEffects":"","specialEffectsType":null,"giftId":2,"rewardGiftNum":1,"createDate":"2021-06-01 10:39:41","modifyDate":"2021-06-01 10:39:41","flag":2,"releaseICO":"http://172.16.66.225/files/mall/20210513/7023c4b742a540bd9bc674112768e7d0.jpg","giftICO":null,"rewardDiaICO":"http://diamond.ico"}]}
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
         * user : {"id":11114,"type":2,"username":"1400330500043702272","ico":"http://172.16.66.225/files/mall/20210603/2cd4eb3e60f2420991ccbf198c60fd1d.jpg","nickname":"别致的熊猫","countryCode":null,"mobile":"18920435270","interest":"1,2,4,3,5","password":"$2a$10$bqTlE1ug1tOvwApRrTN5UOp1yuVEgx1J.oLupRmTfgFXldqu6GU/6","paypassword":null,"gender":1,"paypassSetting":0,"age":4,"email":null,"realName":null,"idCardType":null,"authStatus":0,"idCard":null,"level":1,"authtime":null,"logins":null,"status":1,"inviteCode":"W4CLVIAUXIW1","inviteRelation":null,"directInviteid":null,"lastUpdateTime":"2021-06-03 15:40:07","created":"2021-06-03 13:56:23","unionId":"oYm5k69Ij9_KHvoDzZMD4hnRWMl8","iosId":null,"diamond":10,"balance":0.08,"parentAccount":null,"emergencyContact":null,"fansNum":null,"inviteNum":null,"exp":5,"initials":"b","auditState":2,"bankCard":null}
         * nextLevel : {"id":1,"level":1,"exp":25,"rewardDiaNum":5,"releaseId":2,"rewardReleaseNum":1,"specialEffects":"","specialEffectsType":null,"giftId":1,"rewardGiftNum":1,"createDate":"2021-06-01 10:38:25","modifyDate":"2021-06-01 10:38:25","flag":2}
         * rankPrivilegeBeans : [{"id":1,"level":1,"exp":25,"rewardDiaNum":5,"releaseId":2,"rewardReleaseNum":1,"specialEffects":"","specialEffectsType":null,"giftId":1,"rewardGiftNum":1,"createDate":"2021-06-01 10:38:25","modifyDate":"2021-06-01 10:38:25","flag":2,"releaseICO":"http://172.16.66.225/files/mall/20210518/a977f42efdb34057b16102319df2de03.jpg","giftICO":null,"rewardDiaICO":"http://diamond.ico"},{"id":2,"level":2,"exp":50,"rewardDiaNum":8,"releaseId":3,"rewardReleaseNum":1,"specialEffects":"","specialEffectsType":null,"giftId":2,"rewardGiftNum":1,"createDate":"2021-06-01 10:39:15","modifyDate":"2021-06-01 10:39:15","flag":2,"releaseICO":"http://172.16.66.225/files/mall/20210513/7023c4b742a540bd9bc674112768e7d0.jpg","giftICO":null,"rewardDiaICO":"http://diamond.ico"},{"id":3,"level":3,"exp":10000000,"rewardDiaNum":8,"releaseId":3,"rewardReleaseNum":1,"specialEffects":"","specialEffectsType":null,"giftId":2,"rewardGiftNum":1,"createDate":"2021-06-01 10:39:41","modifyDate":"2021-06-01 10:39:41","flag":2,"releaseICO":"http://172.16.66.225/files/mall/20210513/7023c4b742a540bd9bc674112768e7d0.jpg","giftICO":null,"rewardDiaICO":"http://diamond.ico"}]
         */

        private UserBean user;
        private NextLevelBean nextLevel;
        private List<RankPrivilegeBeansBean> rankPrivilegeBeans;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public NextLevelBean getNextLevel() {
            return nextLevel;
        }

        public void setNextLevel(NextLevelBean nextLevel) {
            this.nextLevel = nextLevel;
        }

        public List<RankPrivilegeBeansBean> getRankPrivilegeBeans() {
            return rankPrivilegeBeans;
        }

        public void setRankPrivilegeBeans(List<RankPrivilegeBeansBean> rankPrivilegeBeans) {
            this.rankPrivilegeBeans = rankPrivilegeBeans;
        }

        public static class UserBean {
            /**
             * id : 11114
             * type : 2
             * username : 1400330500043702272
             * ico : http://172.16.66.225/files/mall/20210603/2cd4eb3e60f2420991ccbf198c60fd1d.jpg
             * nickname : 别致的熊猫
             * countryCode : null
             * mobile : 18920435270
             * interest : 1,2,4,3,5
             * password : $2a$10$bqTlE1ug1tOvwApRrTN5UOp1yuVEgx1J.oLupRmTfgFXldqu6GU/6
             * paypassword : null
             * gender : 1
             * paypassSetting : 0
             * age : 4
             * email : null
             * realName : null
             * idCardType : null
             * authStatus : 0
             * idCard : null
             * level : 1
             * authtime : null
             * logins : null
             * status : 1
             * inviteCode : W4CLVIAUXIW1
             * inviteRelation : null
             * directInviteid : null
             * lastUpdateTime : 2021-06-03 15:40:07
             * created : 2021-06-03 13:56:23
             * unionId : oYm5k69Ij9_KHvoDzZMD4hnRWMl8
             * iosId : null
             * diamond : 10
             * balance : 0.08
             * parentAccount : null
             * emergencyContact : null
             * fansNum : null
             * inviteNum : null
             * exp : 5
             * initials : b
             * auditState : 2
             * bankCard : null
             */

            private int id;
            private int type;
            private String username;
            private String ico;
            private String nickname;
            private Object countryCode;
            private String mobile;
            private String interest;
            private String password;
            private Object paypassword;
            private int gender;
            private int paypassSetting;
            private int age;
            private Object email;
            private Object realName;
            private Object idCardType;
            private int authStatus;
            private Object idCard;
            private int level;
            private Object authtime;
            private Object logins;
            private int status;
            private String inviteCode;
            private Object inviteRelation;
            private Object directInviteid;
            private String lastUpdateTime;
            private String created;
            private String unionId;
            private Object iosId;
            private int diamond;
            private double balance;
            private Object parentAccount;
            private Object emergencyContact;
            private Object fansNum;
            private Object inviteNum;
            private String exp;
            private String initials;
            private int auditState;
            private Object bankCard;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getIco() {
                return ico;
            }

            public void setIco(String ico) {
                this.ico = ico;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public Object getCountryCode() {
                return countryCode;
            }

            public void setCountryCode(Object countryCode) {
                this.countryCode = countryCode;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getInterest() {
                return interest;
            }

            public void setInterest(String interest) {
                this.interest = interest;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public Object getPaypassword() {
                return paypassword;
            }

            public void setPaypassword(Object paypassword) {
                this.paypassword = paypassword;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public int getPaypassSetting() {
                return paypassSetting;
            }

            public void setPaypassSetting(int paypassSetting) {
                this.paypassSetting = paypassSetting;
            }

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }

            public Object getEmail() {
                return email;
            }

            public void setEmail(Object email) {
                this.email = email;
            }

            public Object getRealName() {
                return realName;
            }

            public void setRealName(Object realName) {
                this.realName = realName;
            }

            public Object getIdCardType() {
                return idCardType;
            }

            public void setIdCardType(Object idCardType) {
                this.idCardType = idCardType;
            }

            public int getAuthStatus() {
                return authStatus;
            }

            public void setAuthStatus(int authStatus) {
                this.authStatus = authStatus;
            }

            public Object getIdCard() {
                return idCard;
            }

            public void setIdCard(Object idCard) {
                this.idCard = idCard;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public Object getAuthtime() {
                return authtime;
            }

            public void setAuthtime(Object authtime) {
                this.authtime = authtime;
            }

            public Object getLogins() {
                return logins;
            }

            public void setLogins(Object logins) {
                this.logins = logins;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getInviteCode() {
                return inviteCode;
            }

            public void setInviteCode(String inviteCode) {
                this.inviteCode = inviteCode;
            }

            public Object getInviteRelation() {
                return inviteRelation;
            }

            public void setInviteRelation(Object inviteRelation) {
                this.inviteRelation = inviteRelation;
            }

            public Object getDirectInviteid() {
                return directInviteid;
            }

            public void setDirectInviteid(Object directInviteid) {
                this.directInviteid = directInviteid;
            }

            public String getLastUpdateTime() {
                return lastUpdateTime;
            }

            public void setLastUpdateTime(String lastUpdateTime) {
                this.lastUpdateTime = lastUpdateTime;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public String getUnionId() {
                return unionId;
            }

            public void setUnionId(String unionId) {
                this.unionId = unionId;
            }

            public Object getIosId() {
                return iosId;
            }

            public void setIosId(Object iosId) {
                this.iosId = iosId;
            }

            public int getDiamond() {
                return diamond;
            }

            public void setDiamond(int diamond) {
                this.diamond = diamond;
            }

            public double getBalance() {
                return balance;
            }

            public void setBalance(double balance) {
                this.balance = balance;
            }

            public Object getParentAccount() {
                return parentAccount;
            }

            public void setParentAccount(Object parentAccount) {
                this.parentAccount = parentAccount;
            }

            public Object getEmergencyContact() {
                return emergencyContact;
            }

            public void setEmergencyContact(Object emergencyContact) {
                this.emergencyContact = emergencyContact;
            }

            public Object getFansNum() {
                return fansNum;
            }

            public void setFansNum(Object fansNum) {
                this.fansNum = fansNum;
            }

            public Object getInviteNum() {
                return inviteNum;
            }

            public void setInviteNum(Object inviteNum) {
                this.inviteNum = inviteNum;
            }

            public String getExp() {
                return exp;
            }

            public void setExp(String exp) {
                this.exp = exp;
            }

            public String getInitials() {
                return initials;
            }

            public void setInitials(String initials) {
                this.initials = initials;
            }

            public int getAuditState() {
                return auditState;
            }

            public void setAuditState(int auditState) {
                this.auditState = auditState;
            }

            public Object getBankCard() {
                return bankCard;
            }

            public void setBankCard(Object bankCard) {
                this.bankCard = bankCard;
            }
        }

        public static class NextLevelBean {

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
             */

            private int id;
            private int level;
            private String exp;
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
            private String giftICO;
            private String rewardDiaICO;
            private String releaseName;
            private String giftName;
            private String rewardDiaName;

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

            public String getExp() {
                return exp;
            }

            public void setExp(String exp) {
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

            public String getGiftICO() {
                return giftICO;
            }

            public void setGiftICO(String giftICO) {
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
        }

        public static class RankPrivilegeBeansBean {
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
             */

            private int id;
            private int level;
            private int exp;
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
            private String giftICO;
            private String rewardDiaICO;

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

            public int getExp() {
                return exp;
            }

            public void setExp(int exp) {
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

            public String getGiftICO() {
                return giftICO;
            }

            public void setGiftICO(String giftICO) {
                this.giftICO = giftICO;
            }

            public String getRewardDiaICO() {
                return rewardDiaICO;
            }

            public void setRewardDiaICO(String rewardDiaICO) {
                this.rewardDiaICO = rewardDiaICO;
            }
        }
    }
}
