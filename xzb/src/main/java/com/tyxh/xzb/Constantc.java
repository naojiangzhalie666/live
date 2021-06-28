package com.tyxh.xzb;

public class Constantc {

    /*直播跟im聊天用相同的SDKAPPID  签名使用同一个*/
    public static final boolean use_old = false;     //是否用原来可连麦的数据
    //有连麦功能的 1400188366  小直播的appid
    //现在用的 1400522274       正式的appid
    public static final int SDKAPPID = use_old?1400188366:1400522274;//原  正式
    public static final String SECRETKEY = "5f0edf244ede892ff43f701c0cfab05085ced7c5eef4265c54e3ed07558a87d8";
    public static final long test_sdkAppID = SDKAPPID;

    public static  String LX_HEAD = "";             //连线时观众的头像
    public static boolean mlvb_login = false;       //MLVB是否登录成功


    /*vivo plus9*/
    /*public static  String USER_NAME = "vivo_plus9_new";
    public static  String test_USERID = "9";
    public static  String test_userSig = "eJxtzEFPgzAYgOH-0rPRry2rYOLBleFGRuIEYvTSNLSQBgdNVzaH8b*LE29e3zd5PlGxza*ltUYJ6QV1Ct0hHADgMKSMoavfX1X90Hnhz1ZPn7KQkXkZpTtvaqPdNKI5HlQrLuZ-mDf7HwQzQhgEgBdz1x-WOC1k7S8WWUQEAP5E00wtW5V8s*Mm5TV3A9wWVobt7rVMzodTmyT*6Tiu5LAselgH2XOKbbNp3vjIXrDaEzi*jyMZ*tLl3UO8zmK-JLSPoyZPbx4ZPW2re-T1DSUFUPw_";
    public static  String USER_UserAvatar= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2F50%2Fv2-e73ebe5fb7fbae39d69ed94dcc82f145_hd.jpg&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620871698&t=d27011329fbd6b0ca7bc7aaf01f94f54";
    public static  String USER_CoverPic= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fc%2F55bf0900d0a2e.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620871753&t=c6d2d2c02ecc25d3e0627178af755008";
*/
    /*vivo x23*/
   /* public static  String USER_NAME = "vivo_x23";
    public static  String test_USERID = "7";//
    public static  String test_userSig = "eJxtzEFPwjAYgOH-0isGvrbzY5hwWGDCcOKiQIRLM2iBrmEdoxoW4n93wLx5fd-kuZBZ-NFOi0JLkTrBS0meCPUAqO9zRPJw-5uN-cqdcFWh6s-RR9YsLVXu9Farsh7dJp6kETfzP8zpwxWhyBgCR482XZ0LXSqRbt3NYo89BgB-ot7V7TWcD6LBMJosVxgkGcLL-LjCcae1yEbHc5JZb2RMvl8zFyiZLPeBDoPvz*fFjoa2Or1XU9OFTk*-lbM45raVm-U0Alel2cRLhqHtk59f42JQnQ__";
    public static  String USER_UserAvatar= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fdiy.qqjay.com%2Fu%2Ffiles%2F2012%2F0812%2F07b81c63c37880a882b32b5e4ec5c028.jpg&refer=http%3A%2F%2Fdiy.qqjay.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622345766&t=67defb8b061bf0bfdc035dc72190e03e";
    public static  String USER_CoverPic= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg1001.pocoimg.cn%2Fimage%2Fpoco%2Fworks%2F79%2F2012%2F0322%2F20%2F62853829201203222023101923154827283_004_62853829.jpg&refer=http%3A%2F%2Fimg1001.pocoimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622345814&t=cf97ed5f962610d54eb0e4d8b7a011b5";*/

    /*红米*/
    public static  String USER_NAME = "别致的熊猫";
    public static  String test_USERID = "11114";
    public static  String test_userSig = "eJxtzEFPgzAYxvHv0usWLS22xWSHhZCBUUkDmuClwbaQyoSuVicu**6yyW6*x*ef93cA5X1xVVtrlKi9wE6BWxCEEAaMYULA8q9LOXz2XvjR6qljwgiak1G696Yx2p0epwvn8KE6cXb-A715P0EBQYjRKMJ43vW3NU6LuvFnD91ECEJ4EU07bQ-JU5zxGHXPefl6N1JHK6XT4We0nMtIbjIVvi0q07oy33y9pE3cZm3FFv0uJHhbNJbjzCWP3Z4zHJPEFTSn11s8yJTu9o6vV*D4Cw3pUY0_";
    public static  String USER_UserAvatar= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01a9d85ecf6bdba80120662104b791.jpg%402o.jpg&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622345766&t=4df91a816a2c89c918c956b34a392052";
    public static  String USER_CoverPic= "https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/0824ab18972bd40797d8db1179899e510fb3093a.jpg";

    /*小米*/
   /* public static  String USER_NAME = "xiaomi";
    public static  String test_USERID = "126";
    public static  String test_userSig = "eJxtjEFPwjAYQP9LrxjSdqOsJh4KQ2e2KcShhEszacs*ZbObNWMa-rsD4eb1vbz3g7LkaZhbC0rmTnqNQteI*BiTIPAYQ1d-frP5*KqcdJ3VvfdYwOhZgdKVAwO6OYb0Unyqd3m6-rdzUB43hNGec*7zM9d7C42WuXGnGx1xijG*HGHbs3S2nN5HdUbUqhX1uGCFMdVuy8ZhvR7MIy*FOyKcr*iuHIUm4QJmon3rXjK9jyfhs4WM6nlZpH5sHvVyUcXtoJtE6-bhO3ktbsUNOvwCg3NRig__";
    public static  String USER_UserAvatar= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fdiy.qqjay.com%2Fu%2Ffiles%2F2012%2F0217%2Fb693a3b6d232ffe861da22287c888729.jpg&refer=http%3A%2F%2Fdiy.qqjay.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622782806&t=7114950f5b6a405d01888fc354a4ad78";
    public static  String USER_CoverPic= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.51yuansu.com%2Fpic3%2Fcover%2F01%2F38%2F18%2F5926c0d562656_610.jpg&refer=http%3A%2F%2Fpic.51yuansu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622782095&t=7540049245c5b94394988159182c0ec0";*/

    /*小米*/
   /* public static  String USER_NAME = "xiaomi";
    public static  String test_USERID = "127";
    public static  String test_userSig = "eJxtzF1PgzAYhuH-0lPNaF9YAZMdMJyKsM3N6XQnDYFO30xqA4WsGv-7vs88fe481y9ZZM*9XGssRW6EW5fkhjCPUhYELufk*tSL4rtVRhir5b67POBwTlhKZXCNsj4cwT-PTbkRR-U-zmB1YBgHBmEI7sWSW421FPnaHDXoh0ApvYj4sd-Go5c4uZfTmN5m7*OFn3ZX-g9Hp1pxJ8u9dNZVycRO5IPt7vpD3UY4ijqm3*ZF6nxG7PUJlJKbmfFsW2Sxjpf263GptlXTeAms6ID87QBWN1E9";
    public static  String USER_UserAvatar= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fdiy.qqjay.com%2Fu%2Ffiles%2F2012%2F0217%2Fb693a3b6d232ffe861da22287c888729.jpg&refer=http%3A%2F%2Fdiy.qqjay.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622782806&t=7114950f5b6a405d01888fc354a4ad78";
    public static  String USER_CoverPic= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.51yuansu.com%2Fpic3%2Fcover%2F01%2F38%2F18%2F5926c0d562656_610.jpg&refer=http%3A%2F%2Fpic.51yuansu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622782095&t=7540049245c5b94394988159182c0ec0";*/

}
