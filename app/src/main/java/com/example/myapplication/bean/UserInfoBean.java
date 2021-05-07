package com.example.myapplication.bean;

public class UserInfoBean {

    /**
     * retCode : 0000
     * retMsg : 操作成功!
     * retData : {"id":"1024859055654637581","type":0,"username":"1387341009955848192","nickname":"无忧的海胆","ico":"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201712%2F14%2F20171214080141_mREZz.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622185047&t=5397b1436eda1c1c1c0a9c86b2e24d95","countryCode":null,"mobile":null,"password":"$2a$10$KCBDom6DARln0lks6hZ7IeRemHjyzacGUDqYXrvT5MuJ2BX2BNxFy","paypassword":null,"paypassSetting":"0","email":null,"realName":null,"idCardType":null,"authStatus":0,"idCard":null,"level":1,"authtime":null,"logins":null,"status":1,"inviteCode":"LFFD11UDGM28","inviteRelation":null,"directInviteid":null,"lastUpdateTime":"2021-05-05 09:17:45","created":"2021-04-28 17:40:47","UnionID":"oYm5k63H48O5AijvmX4CBMXf6Ipo","IOSID":null,"gender":null,"age":null,"interest":null,"diamond":0,"Balance":0,"ParentAccount":null,"emergencyContact":null,"fansNum":null,"inviteNum":null,"exp":null,"initials":"w","balance":0,"parentAccount":null,"iosid":null,"unionID":"oYm5k63H48O5AijvmX4CBMXf6Ipo"}
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
         * id : 10248590        就是userid
         * type : 1             用户类型：1-普通用户；2-咨询师;3-主机构;4-子机构
         * username : 1387      用户名
         * nickname : 无        昵称
         * ico : https:/95      头像
         * countryCode : null   国际电话区号
         * mobile : null        手机号
         * password : $2a$10$   密码
         * paypassword : null   支付密码
         * paypassSetting : 0   交易密码设置状态
         * email : null         邮箱
         * realName : null      真实姓名
         * authStatus : 0       认证状态：0-未认证；1-实名认证
         * idCardType : null    证件类型:1，身份证；2，军官证；3，护照；4，台湾居民通行证；5，港澳居民通行证；9，其他；
         * idCard : null        身份证号
         * level : 1            用户等级
         * authtime : null      认证时间
         * logins : null        登录数
         * status : 1           状态：0，禁用；1，启用；
         * inviteCode : LFFD    邀请码
         * inviteRelation : nu  邀请关系
         * directInviteid : nu  直接邀请人ID
         * lastUpdateTime : 2   直接邀请人ID
         * created : 2021-0     创建时间
         * gender : null        性别1：男；2:女
         * age : null           年龄
         * interest : null      兴趣
         * diamond : 0.0        钻石
         * Balance : 0.0        余额
         * ParentAccount : nll  父账号
         * emergencyContact :   应急联系人
         * fansNum : null       粉丝数
         * inviteNum : null     邀约数
         * exp : null           经验值
         * initials : w         昵称首字母
         * unionID : oYm
         * UnionID : oYm5
         * IOSID : null
         */

        private String id;
        private int type;
        private String username;
        private String nickname;
        private String ico;
        private Object countryCode;
        private Object mobile;
        private String password;
        private Object paypassword;
        private String paypassSetting;
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
        private Object IOSID;
        private int gender;
        private int age;
        private String interest;
        private double diamond;
        private Object emergencyContact;
        private Object fansNum;
        private Object inviteNum;
        private Object exp;
        private String initials;
        private double balance;
        private Object parentAccount;
        private Object iosid;
        private String unionID;

        public String getId() {
            return id;
        }

        public void setId(String id) {
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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getIco() {
            return ico;
        }

        public void setIco(String ico) {
            this.ico = ico;
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

        public String getPaypassSetting() {
            return paypassSetting;
        }

        public void setPaypassSetting(String paypassSetting) {
            this.paypassSetting = paypassSetting;
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

        public Object getIOSID() {
            return IOSID;
        }

        public void setIOSID(Object IOSID) {
            this.IOSID = IOSID;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public double getDiamond() {
            return diamond;
        }

        public void setDiamond(double diamond) {
            this.diamond = diamond;
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

        public Object getIosid() {
            return iosid;
        }

        public void setIosid(Object iosid) {
            this.iosid = iosid;
        }

        public String getUnionID() {
            return unionID;
        }

        public void setUnionID(String unionID) {
            this.unionID = unionID;
        }
    }
}
