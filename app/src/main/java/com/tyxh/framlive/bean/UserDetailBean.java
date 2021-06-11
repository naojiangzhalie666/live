package com.tyxh.framlive.bean;

import java.io.Serializable;
import java.util.List;

public class UserDetailBean {

    /**
     * retCode : 0
     * retMsg : 操作成功!
     * retData : {"user":{"id":8,"type":3,"username":"1391548303782445056","ico":"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2F50%2Fv2-e73ebe5fb7fbae39d69ed94dcc82f145_hd.jpg&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620871698&t=d27011329fbd6b0ca7bc7aaf01f94f54","nickname":"乐观的海豚","countryCode":null,"mobile":null,"interest":"职场问题,个人成长,人际关系,","password":"$2a$10$ST.Ow3linhymuyIVwhvTtuxxTtKW3vFr2nW76LgK3GgtktJLWVsd6","paypassword":null,"gender":1,"paypassSetting":0,"age":1,"email":null,"realName":null,"idCardType":null,"authStatus":0,"idCard":null,"level":1,"authtime":null,"logins":null,"status":1,"inviteCode":"5T2IML3MWC7Z","inviteRelation":null,"directInviteid":null,"lastUpdateTime":"2021-05-10 08:19:04","created":"2021-05-10 08:19:04","unionId":"oYm5k63H48O5AijvmX4CBMXf6Ipo","iosId":null,"diamond":1.0E15,"balance":1.000001944E7,"parentAccount":null,"emergencyContact":"11558855555","fansNum":null,"inviteNum":null,"exp":null,"initials":"l","auditState":1},"couMechanism":{"id":1,"meName":"机构名称","meLogo":"机构logo","mePhone":"机构电话","meAddress":"结构地址","meIntroduce":"机构简介","shareRatio":null,"fansNum":null,"auditState":2,"userId":8,"isBan":null,"banReason":null,"rejectReason":null,"createDate":"2021-05-20 09:20:39","modifyDate":"2021-05-20 09:20:39","flag":2,"invitationNum":null},"counselorBeans":[{"id":13,"couName":"咨询师名字","couHeadImg":"咨询师头像","frontIdPhoto":null,"behindIdPhoto":null,"perIntroduce":null,"other":null,"auditState":2,"rejectReason":null,"meId":1,"userId":null,"shareRatio":null,"fansNum":null,"anchorState":null,"isBan":2,"banReason":null,"createDate":"2021-05-20 09:20:39","modifyDate":"2021-05-20 09:20:39","flag":2,"couPiclist":[],"invitationId":null,"invitationNum":null}],"servicePackages":[{"id":2,"proTitle":"商品标题","proPicImg":"商品图片url","reminder":"温馨提示","serviceFeature":"服务特色","serviceFeature2":"服务特色2","serviceIntroduce":"服务介绍","createDate":"2021-05-08 14:56:42","modifyDate":"2021-05-08 14:56:42","flag":2,"servicePackageSpecList":[{"id":2,"servicePackageId":2,"serviceTitle":"规格标题","conDuration":120,"chatNum":20,"proContent":"规格内容描述","originalPrice":60,"discountType":1,"discountPrice":40,"limitStartDate":"2021-05-10 10:46:06","limitEndDate":"2021-05-22 10:46:06","isDiscount":2,"createDate":"2021-05-08 14:56:42","modifyDate":"2021-05-08 14:56:42","flag":2}]}]}
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
         * user : {"id":8,"type":3,"username":"1391548303782445056","ico":"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2F50%2Fv2-e73ebe5fb7fbae39d69ed94dcc82f145_hd.jpg&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620871698&t=d27011329fbd6b0ca7bc7aaf01f94f54","nickname":"乐观的海豚","countryCode":null,"mobile":null,"interest":"职场问题,个人成长,人际关系,","password":"$2a$10$ST.Ow3linhymuyIVwhvTtuxxTtKW3vFr2nW76LgK3GgtktJLWVsd6","paypassword":null,"gender":1,"paypassSetting":0,"age":1,"email":null,"realName":null,"idCardType":null,"authStatus":0,"idCard":null,"level":1,"authtime":null,"logins":null,"status":1,"inviteCode":"5T2IML3MWC7Z","inviteRelation":null,"directInviteid":null,"lastUpdateTime":"2021-05-10 08:19:04","created":"2021-05-10 08:19:04","unionId":"oYm5k63H48O5AijvmX4CBMXf6Ipo","iosId":null,"diamond":1.0E15,"balance":1.000001944E7,"parentAccount":null,"emergencyContact":"11558855555","fansNum":null,"inviteNum":null,"exp":null,"initials":"l","auditState":1}
         * couMechanism : {"id":1,"meName":"机构名称","meLogo":"机构logo","mePhone":"机构电话","meAddress":"结构地址","meIntroduce":"机构简介","shareRatio":null,"fansNum":null,"auditState":2,"userId":8,"isBan":null,"banReason":null,"rejectReason":null,"createDate":"2021-05-20 09:20:39","modifyDate":"2021-05-20 09:20:39","flag":2,"invitationNum":null}
         * counselorBeans : [{"id":13,"couName":"咨询师名字","couHeadImg":"咨询师头像","frontIdPhoto":null,"behindIdPhoto":null,"perIntroduce":null,"other":null,"auditState":2,"rejectReason":null,"meId":1,"userId":null,"shareRatio":null,"fansNum":null,"anchorState":null,"isBan":2,"banReason":null,"createDate":"2021-05-20 09:20:39","modifyDate":"2021-05-20 09:20:39","flag":2,"couPiclist":[],"invitationId":null,"invitationNum":null}]
         * servicePackages : [{"id":2,"proTitle":"商品标题","proPicImg":"商品图片url","reminder":"温馨提示","serviceFeature":"服务特色","serviceFeature2":"服务特色2","serviceIntroduce":"服务介绍","createDate":"2021-05-08 14:56:42","modifyDate":"2021-05-08 14:56:42","flag":2,"servicePackageSpecList":[{"id":2,"servicePackageId":2,"serviceTitle":"规格标题","conDuration":120,"chatNum":20,"proContent":"规格内容描述","originalPrice":60,"discountType":1,"discountPrice":40,"limitStartDate":"2021-05-10 10:46:06","limitEndDate":"2021-05-22 10:46:06","isDiscount":2,"createDate":"2021-05-08 14:56:42","modifyDate":"2021-05-08 14:56:42","flag":2}]}]
         */

        private UserBean user;
        private CouMechanismBean couMechanism;
        private List<CounselorBeansBean> counselorBeans;
        private List<ServicePackagesBean> servicePackages;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public CouMechanismBean getCouMechanism() {
            return couMechanism;
        }

        public void setCouMechanism(CouMechanismBean couMechanism) {
            this.couMechanism = couMechanism;
        }

        public List<CounselorBeansBean> getCounselorBeans() {
            return counselorBeans;
        }

        public void setCounselorBeans(List<CounselorBeansBean> counselorBeans) {
            this.counselorBeans = counselorBeans;
        }

        public List<ServicePackagesBean> getServicePackages() {
            return servicePackages;
        }

        public void setServicePackages(List<ServicePackagesBean> servicePackages) {
            this.servicePackages = servicePackages;
        }

        public static class UserBean {
            /**
             * id : 8
             * type : 3
             * username : 1391548303782445056
             * ico : https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2F50%2Fv2-e73ebe5fb7fbae39d69ed94dcc82f145_hd.jpg&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620871698&t=d27011329fbd6b0ca7bc7aaf01f94f54
             * nickname : 乐观的海豚
             * countryCode : null
             * mobile : null
             * interest : 职场问题,个人成长,人际关系,
             * password : $2a$10$ST.Ow3linhymuyIVwhvTtuxxTtKW3vFr2nW76LgK3GgtktJLWVsd6
             * paypassword : null
             * gender : 1
             * paypassSetting : 0
             * age : 1
             * email : null
             * realName : null
             * idCardType : null
             * authStatus : 0
             * idCard : null
             * level : 1
             * authtime : null
             * logins : null
             * status : 1
             * inviteCode : 5T2IML3MWC7Z
             * inviteRelation : null
             * directInviteid : null
             * lastUpdateTime : 2021-05-10 08:19:04
             * created : 2021-05-10 08:19:04
             * unionId : oYm5k63H48O5AijvmX4CBMXf6Ipo
             * iosId : null
             * diamond : 1.0E15
             * balance : 1.000001944E7
             * parentAccount : null
             * emergencyContact : 11558855555
             * fansNum : null
             * inviteNum : null
             * exp : null
             * initials : l
             * auditState : 1
             */

            private int id;
            private int type;
            private String username;
            private String ico;
            private String regDays;
            private String connectionIncomeValue;
            private String nickname;
            private Object countryCode;
            private Object mobile;
            private String interest;
            private String password;
            private Object paypassword;
            private int gender;
            private int paypassSetting;
            private int age;
            private String ages;
            private String interests;
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
            private String diamond;
            private String balance;
            private Object parentAccount;
            private String emergencyContact;
            private Object fansNum;
            private Object inviteNum;
            private Object exp;
            private String initials;
            private int auditState;

            public String getRegDays() {
                return regDays;
            }

            public void setRegDays(String regDays) {
                this.regDays = regDays;
            }

            public String getConnectionIncomeValue() {
                return connectionIncomeValue;
            }

            public void setConnectionIncomeValue(String connectionIncomeValue) {
                this.connectionIncomeValue = connectionIncomeValue;
            }

            public String getAges() {
                return ages;
            }

            public void setAges(String ages) {
                this.ages = ages;
            }

            public String getInterests() {
                return interests;
            }

            public void setInterests(String interests) {
                this.interests = interests;
            }

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

            public Object getMobile() {
                return mobile;
            }

            public void setMobile(Object mobile) {
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

            public String getDiamond() {
                return diamond;
            }

            public void setDiamond(String diamond) {
                this.diamond = diamond;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public Object getParentAccount() {
                return parentAccount;
            }

            public void setParentAccount(Object parentAccount) {
                this.parentAccount = parentAccount;
            }

            public String getEmergencyContact() {
                return emergencyContact;
            }

            public void setEmergencyContact(String emergencyContact) {
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

            public Object getExp() {
                return exp;
            }

            public void setExp(Object exp) {
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
        }

        public static class CouMechanismBean {
            /**
             * id : 1
             * meName : 机构名称
             * meLogo : 机构logo
             * mePhone : 机构电话
             * meAddress : 结构地址
             * meIntroduce : 机构简介
             * shareRatio : null
             * fansNum : null
             * auditState : 2
             * userId : 8
             * isBan : null
             * banReason : null
             * rejectReason : null
             * createDate : 2021-05-20 09:20:39
             * modifyDate : 2021-05-20 09:20:39
             * flag : 2
             * invitationNum : null
             */

            private int id;
            private String meName;
            private String meLogo;
            private String mePhone;
            private String meAddress;
            private String meIntroduce;
            private Object shareRatio;
            private Object fansNum;
            private int auditState;
            private int userId;
            private Object isBan;
            private Object banReason;
            private Object rejectReason;
            private String createDate;
            private String modifyDate;
            private int flag;
            private Object invitationNum;
            private List<CouPicBean> couPicList;

            public List<CouPicBean> getCouPiclist() {
                return couPicList;
            }

            public void setCouPiclist(List<CouPicBean> couPiclist) {
                this.couPicList = couPiclist;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMeName() {
                return meName;
            }

            public void setMeName(String meName) {
                this.meName = meName;
            }

            public String getMeLogo() {
                return meLogo;
            }

            public void setMeLogo(String meLogo) {
                this.meLogo = meLogo;
            }

            public String getMePhone() {
                return mePhone;
            }

            public void setMePhone(String mePhone) {
                this.mePhone = mePhone;
            }

            public String getMeAddress() {
                return meAddress;
            }

            public void setMeAddress(String meAddress) {
                this.meAddress = meAddress;
            }

            public String getMeIntroduce() {
                return meIntroduce;
            }

            public void setMeIntroduce(String meIntroduce) {
                this.meIntroduce = meIntroduce;
            }

            public Object getShareRatio() {
                return shareRatio;
            }

            public void setShareRatio(Object shareRatio) {
                this.shareRatio = shareRatio;
            }

            public Object getFansNum() {
                return fansNum;
            }

            public void setFansNum(Object fansNum) {
                this.fansNum = fansNum;
            }

            public int getAuditState() {
                return auditState;
            }

            public void setAuditState(int auditState) {
                this.auditState = auditState;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public Object getIsBan() {
                return isBan;
            }

            public void setIsBan(Object isBan) {
                this.isBan = isBan;
            }

            public Object getBanReason() {
                return banReason;
            }

            public void setBanReason(Object banReason) {
                this.banReason = banReason;
            }

            public Object getRejectReason() {
                return rejectReason;
            }

            public void setRejectReason(Object rejectReason) {
                this.rejectReason = rejectReason;
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

            public Object getInvitationNum() {
                return invitationNum;
            }

            public void setInvitationNum(Object invitationNum) {
                this.invitationNum = invitationNum;
            }

            public static class CouPicBean{
                private String imgUrl;

                public String getImgUrl() {
                    return imgUrl;
                }

                public void setImgUrl(String imgUrl) {
                    this.imgUrl = imgUrl;
                }
            }

        }

        public static class CounselorBeansBean {
            /**
             * id : 13
             * couName : 咨询师名字
             * couHeadImg : 咨询师头像
             * frontIdPhoto : null
             * behindIdPhoto : null
             * perIntroduce : null
             * other : null
             * auditState : 2
             * rejectReason : null
             * meId : 1
             * userId : null
             * shareRatio : null
             * fansNum : null
             * anchorState : null
             * isBan : 2
             * banReason : null
             * createDate : 2021-05-20 09:20:39
             * modifyDate : 2021-05-20 09:20:39
             * flag : 2
             * couPiclist : []
             * invitationId : null
             * invitationNum : null
             */

            private int id;
            private String couName;
            private String couHeadImg;
            private Object frontIdPhoto;
            private Object behindIdPhoto;
            private String perIntroduce;
            private Object other;
            private int auditState;
            private Object rejectReason;
            private int meId;
            private Object userId;
            private Object shareRatio;
            private Object fansNum;
            private Object anchorState;
            private int isBan;
            private Object banReason;
            private String createDate;
            private String modifyDate;
            private int flag;
            private Object invitationId;
            private Object invitationNum;
            private List<CouPicBean> couPiclist;
            private String interests;

            public String getInterests() {
                return interests;
            }

            public void setInterests(String interests) {
                this.interests = interests;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCouName() {
                return couName;
            }

            public void setCouName(String couName) {
                this.couName = couName;
            }

            public String getCouHeadImg() {
                return couHeadImg;
            }

            public void setCouHeadImg(String couHeadImg) {
                this.couHeadImg = couHeadImg;
            }

            public Object getFrontIdPhoto() {
                return frontIdPhoto;
            }

            public void setFrontIdPhoto(Object frontIdPhoto) {
                this.frontIdPhoto = frontIdPhoto;
            }

            public Object getBehindIdPhoto() {
                return behindIdPhoto;
            }

            public void setBehindIdPhoto(Object behindIdPhoto) {
                this.behindIdPhoto = behindIdPhoto;
            }

            public String getPerIntroduce() {
                return perIntroduce;
            }

            public void setPerIntroduce(String perIntroduce) {
                this.perIntroduce = perIntroduce;
            }

            public Object getOther() {
                return other;
            }

            public void setOther(Object other) {
                this.other = other;
            }

            public int getAuditState() {
                return auditState;
            }

            public void setAuditState(int auditState) {
                this.auditState = auditState;
            }

            public Object getRejectReason() {
                return rejectReason;
            }

            public void setRejectReason(Object rejectReason) {
                this.rejectReason = rejectReason;
            }

            public int getMeId() {
                return meId;
            }

            public void setMeId(int meId) {
                this.meId = meId;
            }

            public Object getUserId() {
                return userId;
            }

            public void setUserId(Object userId) {
                this.userId = userId;
            }

            public Object getShareRatio() {
                return shareRatio;
            }

            public void setShareRatio(Object shareRatio) {
                this.shareRatio = shareRatio;
            }

            public Object getFansNum() {
                return fansNum;
            }

            public void setFansNum(Object fansNum) {
                this.fansNum = fansNum;
            }

            public Object getAnchorState() {
                return anchorState;
            }

            public void setAnchorState(Object anchorState) {
                this.anchorState = anchorState;
            }

            public int getIsBan() {
                return isBan;
            }

            public void setIsBan(int isBan) {
                this.isBan = isBan;
            }

            public Object getBanReason() {
                return banReason;
            }

            public void setBanReason(Object banReason) {
                this.banReason = banReason;
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

            public Object getInvitationId() {
                return invitationId;
            }

            public void setInvitationId(Object invitationId) {
                this.invitationId = invitationId;
            }

            public Object getInvitationNum() {
                return invitationNum;
            }

            public void setInvitationNum(Object invitationNum) {
                this.invitationNum = invitationNum;
            }

            public List<CouPicBean> getCouPiclist() {
                return couPiclist;
            }

            public void setCouPiclist(List<CouPicBean> couPiclist) {
                this.couPiclist = couPiclist;
            }

            public static class CouPicBean{
                private String imgUrl;

                public String getImgUrl() {
                    return imgUrl;
                }

                public void setImgUrl(String imgUrl) {
                    this.imgUrl = imgUrl;
                }
            }



        }

        public static class ServicePackagesBean implements Serializable {
            /**
             * id : 2
             * proTitle : 商品标题
             * proPicImg : 商品图片url
             * reminder : 温馨提示
             * serviceFeature : 服务特色
             * serviceFeature2 : 服务特色2
             * serviceIntroduce : 服务介绍
             * createDate : 2021-05-08 14:56:42
             * modifyDate : 2021-05-08 14:56:42
             * flag : 2
             * servicePackageSpecList : [{"id":2,"servicePackageId":2,"serviceTitle":"规格标题","conDuration":120,"chatNum":20,"proContent":"规格内容描述","originalPrice":60,"discountType":1,"discountPrice":40,"limitStartDate":"2021-05-10 10:46:06","limitEndDate":"2021-05-22 10:46:06","isDiscount":2,"createDate":"2021-05-08 14:56:42","modifyDate":"2021-05-08 14:56:42","flag":2}]
             */

            private int id;
            private String proTitle;
            private String proPicImg;
            private String reminder;
            private String serviceFeature;
            private String serviceFeature2;
            private String serviceIntroduce;
            private String createDate;
            private String modifyDate;
            private int lowestPrice;
            private int flag;
            private List<ServicePackageSpecListBean> servicePackageSpecList;
            private boolean select = true;

            public int getLowestPrice() {
                return lowestPrice;
            }

            public void setLowestPrice(int lowestPrice) {
                this.lowestPrice = lowestPrice;
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

            public String getProTitle() {
                return proTitle;
            }

            public void setProTitle(String proTitle) {
                this.proTitle = proTitle;
            }

            public String getProPicImg() {
                return proPicImg;
            }

            public void setProPicImg(String proPicImg) {
                this.proPicImg = proPicImg;
            }

            public String getReminder() {
                return reminder;
            }

            public void setReminder(String reminder) {
                this.reminder = reminder;
            }

            public String getServiceFeature() {
                return serviceFeature;
            }

            public void setServiceFeature(String serviceFeature) {
                this.serviceFeature = serviceFeature;
            }

            public String getServiceFeature2() {
                return serviceFeature2;
            }

            public void setServiceFeature2(String serviceFeature2) {
                this.serviceFeature2 = serviceFeature2;
            }

            public String getServiceIntroduce() {
                return serviceIntroduce;
            }

            public void setServiceIntroduce(String serviceIntroduce) {
                this.serviceIntroduce = serviceIntroduce;
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

            public List<ServicePackageSpecListBean> getServicePackageSpecList() {
                return servicePackageSpecList;
            }

            public void setServicePackageSpecList(List<ServicePackageSpecListBean> servicePackageSpecList) {
                this.servicePackageSpecList = servicePackageSpecList;
            }

            public static class ServicePackageSpecListBean implements Serializable{
                /**
                 * id : 2
                 * servicePackageId : 2
                 * serviceTitle : 规格标题
                 * conDuration : 120
                 * chatNum : 20
                 * proContent : 规格内容描述
                 * originalPrice : 60
                 * discountType : 1
                 * discountPrice : 40
                 * limitStartDate : 2021-05-10 10:46:06
                 * limitEndDate : 2021-05-22 10:46:06
                 * isDiscount : 2
                 * createDate : 2021-05-08 14:56:42
                 * modifyDate : 2021-05-08 14:56:42
                 * flag : 2
                 */

                private int id;
                private int servicePackageId;
                private String serviceTitle;
                private int conDuration;
                private int chatNum;
                private String proContent;
                private int originalPrice;
                private int discountType;
                private int discountPrice;
                private String limitStartDate;
                private String limitEndDate;
                private int isDiscount;
                private String createDate;
                private String modifyDate;
                private int flag;
                private boolean select;

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

                public int getServicePackageId() {
                    return servicePackageId;
                }

                public void setServicePackageId(int servicePackageId) {
                    this.servicePackageId = servicePackageId;
                }

                public String getServiceTitle() {
                    return serviceTitle;
                }

                public void setServiceTitle(String serviceTitle) {
                    this.serviceTitle = serviceTitle;
                }

                public int getConDuration() {
                    return conDuration;
                }

                public void setConDuration(int conDuration) {
                    this.conDuration = conDuration;
                }

                public int getChatNum() {
                    return chatNum;
                }

                public void setChatNum(int chatNum) {
                    this.chatNum = chatNum;
                }

                public String getProContent() {
                    return proContent;
                }

                public void setProContent(String proContent) {
                    this.proContent = proContent;
                }

                public int getOriginalPrice() {
                    return originalPrice;
                }

                public void setOriginalPrice(int originalPrice) {
                    this.originalPrice = originalPrice;
                }

                public int getDiscountType() {
                    return discountType;
                }

                public void setDiscountType(int discountType) {
                    this.discountType = discountType;
                }

                public int getDiscountPrice() {
                    return discountPrice;
                }

                public void setDiscountPrice(int discountPrice) {
                    this.discountPrice = discountPrice;
                }

                public String getLimitStartDate() {
                    return limitStartDate;
                }

                public void setLimitStartDate(String limitStartDate) {
                    this.limitStartDate = limitStartDate;
                }

                public String getLimitEndDate() {
                    return limitEndDate;
                }

                public void setLimitEndDate(String limitEndDate) {
                    this.limitEndDate = limitEndDate;
                }

                public int getIsDiscount() {
                    return isDiscount;
                }

                public void setIsDiscount(int isDiscount) {
                    this.isDiscount = isDiscount;
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
