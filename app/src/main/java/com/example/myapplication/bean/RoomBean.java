package com.example.myapplication.bean;

import java.util.List;

public class RoomBean {

    /**
     * retCode : 0000
     * retMsg : 操作成功!
     * retData : [{"mixedPlayURL":"http://bkplay.aininongmu.com/live/1400515936_1024859055654637588.flv","userInfo":{"id":"1024859055654637588","type":0,"username":"1390546019258728448","ico":"","nickname":"英姿焕发的天鹅","countryCode":null,"mobile":"18920435270","interest":"职场问题,个人成长,","password":"$2a$10$LIMVHih1QmwdOpP9GNH4IuYLqnmlGK6FgVxcnHxGeZ1KmdKUoWBK2","paypassword":null,"gender":1,"paypassSetting":0,"age":7,"email":null,"realName":null,"idCardType":null,"authStatus":0,"idCard":null,"level":1,"authtime":null,"logins":null,"status":1,"inviteCode":"6CLMPRQMUI47","inviteRelation":null,"directInviteid":null,"lastUpdateTime":"2021-05-07 13:56:21","created":"2021-05-07 13:56:21","unionId":null,"iosId":null,"diamond":0,"balance":0,"parentAccount":null,"emergencyContact":null,"fansNum":null,"inviteNum":null,"exp":null,"initials":"y"},"roomCreator":"1024859055654637588","roomStatusCode":1,"custom":"{}","audienceCount":0,"pushers":[{"userAvatar":"","userName":"英姿焕发的天鹅","userID":"1024859055654637588"}],"roomID":"room_1024859055654637588","roomInfo":"{\"title\":\"婚前昏后大变样，教你十一招回到解放前！\",\"frontcover\":\"\",\"location\":\"天津\"}"}]
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
         * mixedPlayURL : http://bkplay.aininongmu.com/live/1400515936_1024859055654637588.flv
         * userInfo : {"id":"1024859055654637588","type":0,"username":"1390546019258728448","ico":"","nickname":"英姿焕发的天鹅","countryCode":null,"mobile":"18920435270","interest":"职场问题,个人成长,","password":"$2a$10$LIMVHih1QmwdOpP9GNH4IuYLqnmlGK6FgVxcnHxGeZ1KmdKUoWBK2","paypassword":null,"gender":1,"paypassSetting":0,"age":7,"email":null,"realName":null,"idCardType":null,"authStatus":0,"idCard":null,"level":1,"authtime":null,"logins":null,"status":1,"inviteCode":"6CLMPRQMUI47","inviteRelation":null,"directInviteid":null,"lastUpdateTime":"2021-05-07 13:56:21","created":"2021-05-07 13:56:21","unionId":null,"iosId":null,"diamond":0,"balance":0,"parentAccount":null,"emergencyContact":null,"fansNum":null,"inviteNum":null,"exp":null,"initials":"y"}
         * roomCreator : 1024859055654637588
         * roomStatusCode : 1
         * custom : {}
         * audienceCount : 0
         * pushers : [{"userAvatar":"","userName":"英姿焕发的天鹅","userID":"1024859055654637588"}]
         * roomID : room_1024859055654637588
         * roomInfo : {"title":"婚前昏后大变样，教你十一招回到解放前！","frontcover":"","location":"天津"}
         */

       public String mixedPlayURL;
       public UserInfoBean userInfo;
       public String roomCreator;
       public int roomStatusCode;
       public String custom;
       public int audienceCount;
       public String roomID;
       public String roomName;
       public String roomInfo;
        public List<PushersBean> pushers;

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getMixedPlayURL() {
            return mixedPlayURL;
        }

        public void setMixedPlayURL(String mixedPlayURL) {
            this.mixedPlayURL = mixedPlayURL;
        }

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public String getRoomCreator() {
            return roomCreator;
        }

        public void setRoomCreator(String roomCreator) {
            this.roomCreator = roomCreator;
        }

        public int getRoomStatusCode() {
            return roomStatusCode;
        }

        public void setRoomStatusCode(int roomStatusCode) {
            this.roomStatusCode = roomStatusCode;
        }

        public String getCustom() {
            return custom;
        }

        public void setCustom(String custom) {
            this.custom = custom;
        }

        public int getAudienceCount() {
            return audienceCount;
        }

        public void setAudienceCount(int audienceCount) {
            this.audienceCount = audienceCount;
        }

        public String getRoomID() {
            return roomID;
        }

        public void setRoomID(String roomID) {
            this.roomID = roomID;
        }

        public String getRoomInfo() {
            return roomInfo;
        }

        public void setRoomInfo(String roomInfo) {
            this.roomInfo = roomInfo;
        }

        public List<PushersBean> getPushers() {
            return pushers;
        }

        public void setPushers(List<PushersBean> pushers) {
            this.pushers = pushers;
        }

        public static class UserInfoBean {
            /**
             * id : 1024859055654637588
             * type : 0
             * username : 1390546019258728448
             * ico :
             * nickname : 英姿焕发的天鹅
             * countryCode : null
             * mobile : 18920435270
             * interest : 职场问题,个人成长,
             * password : $2a$10$LIMVHih1QmwdOpP9GNH4IuYLqnmlGK6FgVxcnHxGeZ1KmdKUoWBK2
             * paypassword : null
             * gender : 1
             * paypassSetting : 0
             * age : 7
             * email : null
             * realName : null
             * idCardType : null
             * authStatus : 0
             * idCard : null
             * level : 1
             * authtime : null
             * logins : null
             * status : 1
             * inviteCode : 6CLMPRQMUI47
             * inviteRelation : null
             * directInviteid : null
             * lastUpdateTime : 2021-05-07 13:56:21
             * created : 2021-05-07 13:56:21
             * unionId : null
             * iosId : null
             * diamond : 0.0
             * balance : 0.0
             * parentAccount : null
             * emergencyContact : null
             * fansNum : null
             * inviteNum : null
             * exp : null
             * initials : y
             */

            private String id;
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
            private Object unionId;
            private Object iosId;
            private double diamond;
            private double balance;
            private Object parentAccount;
            private Object emergencyContact;
            private Object fansNum;
            private Object inviteNum;
            private Object exp;
            private String initials;

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

            public Object getUnionId() {
                return unionId;
            }

            public void setUnionId(Object unionId) {
                this.unionId = unionId;
            }

            public Object getIosId() {
                return iosId;
            }

            public void setIosId(Object iosId) {
                this.iosId = iosId;
            }

            public double getDiamond() {
                return diamond;
            }

            public void setDiamond(double diamond) {
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
        }

        public static class PushersBean {
            /**
             * userAvatar :                 推流人的头像地址
             * userName : 英姿焕发的天鹅
             * userID : 1024859055654637588
             */

           public String userAvatar;
           public String userName;
           public String userID;

            public String getUserAvatar() {
                return userAvatar;
            }

            public void setUserAvatar(String userAvatar) {
                this.userAvatar = userAvatar;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserID() {
                return userID;
            }

            public void setUserID(String userID) {
                this.userID = userID;
            }
        }
    }
}
