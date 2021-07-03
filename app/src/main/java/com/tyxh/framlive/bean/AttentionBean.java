package com.tyxh.framlive.bean;

import com.tyxh.framlive.utils.indelistview.Cn2Spell;

import java.util.List;

public class AttentionBean {

    /**
     * retCode : 0000
     * retMsg : 操作成功!
     * retData : [{"initials":"s","datas":[{"userId":"1024859055654637600","attId":"1024859055654637587","username":"1390508667756871680","nickname":"洒脱的瓜叶菊","ico":"","initials":"s"}]},{"initials":"x","datas":[{"userId":"1024859055654637600","attId":"1024859055654637586","username":"1390464974505115648","nickname":"心地善良的薰衣草","ico":"","initials":"x"}]}]
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
         * initials : s
         * datas : [{"userId":"1024859055654637600","attId":"1024859055654637587","username":"1390508667756871680","nickname":"洒脱的瓜叶菊","ico":"","initials":"s"}]
         */

        private String initials;
        private List<DatasBean> datas;

        public String getInitials() {
            return initials;
        }

        public void setInitials(String initials) {
            this.initials = initials;
        }

        public List<DatasBean> getDatas() {
            return datas;
        }

        public void setDatas(List<DatasBean> datas) {
            this.datas = datas;
        }

        public static class DatasBean implements Comparable<DatasBean>{
            /**
             * userId : 1024859055654637600  //自己的id
             * attId : 1024859055654637587  //被关注人id
             * username : 1390508667756871680
             * nickname : 洒脱的瓜叶菊
             * ico :
             * initials : s
             */

            private String userId;
            private String attId;
            private String username;
            private String nickname;
            private String ico;
            private String initials;
            private String pinyin; // 姓名对应的拼音
            private int state ;

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getAttId() {
                return attId;
            }

            public void setAttId(String attId) {
                this.attId = attId;
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
                pinyin = Cn2Spell.getPinYin(nickname); // 根据姓名获取拼音
            }
            public String getPinyin() {
                return pinyin;
            }

            public String getIco() {
                return ico;
            }

            public void setIco(String ico) {
                this.ico = ico;
            }

            public String getInitials() {
                return initials;
            }

            public void setInitials(String initials) {
                this.initials = initials;
            }
            @Override
            public int compareTo(DatasBean another) {
                if (initials.equals("#") && !another.getInitials().equals("#")) {
                    return 1;
                } else if (!initials.equals("#") && another.getInitials().equals("#")){
                    return -1;
                } else {
                    return pinyin.compareToIgnoreCase(another.getPinyin());
                }
            }
        }
    }
}
