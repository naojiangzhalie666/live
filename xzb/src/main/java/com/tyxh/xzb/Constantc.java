package com.tyxh.xzb;

public class Constantc {

    /*直播跟im聊天用相同的SDKAPPID  签名使用同一个*/
    public static final boolean use_old = false;     //是否用原来可连麦的数据
    //有连麦功能的 1400188366
    //现在用的 1400515936
    public static final int SDKAPPID = use_old?1400188366:1400515936;//原  测试
    public static final String SECRETKEY = "218c0d7f016d2b946d3cf64128e76a23f85a7b00129bbd623d4deab2ff0ed292";
    public static final long test_sdkAppID = SDKAPPID;

    public static  String LX_HEAD = "";             //连线时观众的头像
    public static boolean mlvb_login = false;       //MLVB是否登录成功


    /*vivo plus9*/
    public static  String USER_NAME = "vivo_plus9";
    public static  String test_USERID = "123";
    public static  String test_userSig = "eJxtjMtSwjAAAP8l1zqaRw2tN0VGA0LGoaJyyZQ8aFptMyHaFsd-F7DevO7O7hfIHpbnuXNWiTwI4hW4AiiGECUJoRSc-Xopm486iNA7ffCEJhQPyipdB2us9scQkwHvVCVO1-92wb4fN4hihNGIUjhw3TnrtchNON3wZYoh-JM7uz2w*eR1zB7H2-Y2flvJ0k443SvYRGvSrzymijebOb*JssiVfPn8WZmWFddsdDGTC9nPyuqJ*Y4VazMt6tiG*w2a8rTVaWxeuuzOLSj4-gGPmVHX";
    public static  String USER_UserAvatar= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2F50%2Fv2-e73ebe5fb7fbae39d69ed94dcc82f145_hd.jpg&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620871698&t=d27011329fbd6b0ca7bc7aaf01f94f54";
    public static  String USER_CoverPic= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fc%2F55bf0900d0a2e.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620871753&t=c6d2d2c02ecc25d3e0627178af755008";

    /*vivo x23*/
   /* public static  String USER_NAME = "vivo_x23";
    public static  String test_USERID = "124";//
    public static  String test_userSig = "eJxtzEFvgjAYxvHv0ivL7PsipSzZgRAYmClrGGq8NAyqNE5AVreRZd996vTm9fnn*f2Q1*fsvug6XcnCSLuvyAOBMaXAuc0YufvvZdkeGiPN0KljtxlneEm6Uo3Ra6360xHHl-mj2sqzeoszendigCEguC5ed-Xd6V7JYm3OGjoeUkqvot4ct2mYB0kQzUPeDBmdxSUETuvWpfjCdhCfeXpY1bEzcSZQWPNEGV*HvhXVozR3lxaMhABvuZoF75m330WYUfGSgg7jtyefLxLjP5LfPyF4Tus_";
    public static  String USER_UserAvatar= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fdiy.qqjay.com%2Fu%2Ffiles%2F2012%2F0812%2F07b81c63c37880a882b32b5e4ec5c028.jpg&refer=http%3A%2F%2Fdiy.qqjay.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622345766&t=67defb8b061bf0bfdc035dc72190e03e";
    public static  String USER_CoverPic= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg1001.pocoimg.cn%2Fimage%2Fpoco%2Fworks%2F79%2F2012%2F0322%2F20%2F62853829201203222023101923154827283_004_62853829.jpg&refer=http%3A%2F%2Fimg1001.pocoimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622345814&t=cf97ed5f962610d54eb0e4d8b7a011b5";*/

    /*红米*/
   /* public static  String USER_NAME = "hongmi";
    public static  String test_USERID = "125";
    public static  String test_userSig = "eJxtzM2OgjAYheF76dpo*yG1Y*LCYWDEMAgq0XHTEFpJoyIDZRSN9*4f7tyeN*c5o7k3a8d5rgSPNTcKgfqIdDEmjBmUotazJ8m*yjTXdS5v3aCMQpOUkJlWayWL*xHMZi7Fhj-Ud5xWuztDKBBi9ChhzS6PuSokj9f6oYH5ARjjl6jS2-ZjR5YbWnJiwmIsPFGx2v*2Vssk7Nh*MgnSrIL9*A*G3iqYMTn6PLhpuMmqKZjBMP3CrnD*Q5tsD5Hj7mz-2KnnATt1l86pLKPyd4AuV2DaUTE_";
    public static  String USER_UserAvatar= "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01a9d85ecf6bdba80120662104b791.jpg%402o.jpg&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622345766&t=4df91a816a2c89c918c956b34a392052";
    public static  String USER_CoverPic= "https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/0824ab18972bd40797d8db1179899e510fb3093a.jpg";*/

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
