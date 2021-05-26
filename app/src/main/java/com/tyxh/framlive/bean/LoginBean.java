package com.tyxh.framlive.bean;

import java.util.List;

public class LoginBean {
    /**
     * retCode : 0000
     * retData : {"token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjIwNTE1ODE3MDEsInVzZXJfbmFtZSI6IjEwMTQwNjY5MDkyODAzNzQ3ODUiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiMTUyODFiZjMtZGI2MC00YTI3LTg2ZjktMTkxODFhMjAyZjk5IiwiY2xpZW50X2lkIjoiY2hhbmdnb3UiLCJzY29wZSI6WyJhcHAiXX0.aZlRGF4Mr6JpZrQbNmSHKTnXwgMaLFs7U0RYikljVLBhJBQlzSvNQJP866cSRajSgmhj34up7YkL6lTWLq5LY5cncY5UYFtLpEmN6SzcpyUaE8oRKxceBO3vyZO4ylvgSY_otOzd13jMEVgvTksjx5uBnD5GyuufJO5GUIWUHLtYz6GGh1V0UUssdWBrcbCIlUYOvPjfvBeajDO3a_d7OMPnoKojNd_HtZ2FqA5iThJ3OCZ0uJ_XfBxLeS_iIBT5ecrgLetGoIWX5M7IFiTJVFRJeHcBXIkz_xIIE9sG2ofkfvgX6rfii11DjuqLGi2EkSeBwUuH2bcLb3LdsUzDvw","authorities":[{"role":"ROLE_USER","authority":"ROLE_USER"}]}
     * retMsg : 操作成功!
     */

    private int retCode;
    private RetDataBean retData;
    private String retMsg;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public RetDataBean getRetData() {
        return retData;
    }

    public void setRetData(RetDataBean retData) {
        this.retData = retData;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public static class RetDataBean {
        /**
         * token : eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjIwNTE1ODE3MDEsInVzZXJfbmFtZSI6IjEwMTQwNjY5MDkyODAzNzQ3ODUiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiMTUyODFiZjMtZGI2MC00YTI3LTg2ZjktMTkxODFhMjAyZjk5IiwiY2xpZW50X2lkIjoiY2hhbmdnb3UiLCJzY29wZSI6WyJhcHAiXX0.aZlRGF4Mr6JpZrQbNmSHKTnXwgMaLFs7U0RYikljVLBhJBQlzSvNQJP866cSRajSgmhj34up7YkL6lTWLq5LY5cncY5UYFtLpEmN6SzcpyUaE8oRKxceBO3vyZO4ylvgSY_otOzd13jMEVgvTksjx5uBnD5GyuufJO5GUIWUHLtYz6GGh1V0UUssdWBrcbCIlUYOvPjfvBeajDO3a_d7OMPnoKojNd_HtZ2FqA5iThJ3OCZ0uJ_XfBxLeS_iIBT5ecrgLetGoIWX5M7IFiTJVFRJeHcBXIkz_xIIE9sG2ofkfvgX6rfii11DjuqLGi2EkSeBwUuH2bcLb3LdsUzDvw
         * authorities : [{"role":"ROLE_USER","authority":"ROLE_USER"}]
         */

        private String token;
        private List<AuthoritiesBean> authorities;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<AuthoritiesBean> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<AuthoritiesBean> authorities) {
            this.authorities = authorities;
        }

        public static class AuthoritiesBean {
            /**
             * role : ROLE_USER
             * authority : ROLE_USER
             */

            private String role;
            private String authority;

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getAuthority() {
                return authority;
            }

            public void setAuthority(String authority) {
                this.authority = authority;
            }
        }
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "retCode='" + retCode + '\'' +
                ", retData=" + retData +
                ", retMsg='" + retMsg + '\'' +
                '}';
    }
}
