package com.tyxh.framlive.utils.httputil.old;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSASignature {

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * RSA签名
     * @param content 待签名数据
     * @param privateKey 商户私钥
     * @param encode 字符集编码
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String encode)
    {
        try
        {
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec( Base64.decode(privateKey) );

            KeyFactory keyf                 = KeyFactory.getInstance("RSA");
            PrivateKey priKey               = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update( content.getBytes(encode));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static String sign(String content, String privateKey)
    {
        try
        {
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec( Base64.decode(privateKey) );
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update( content.getBytes());
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA验签名检查
     * @param content 待签名数据
     * @param sign 签名值
     * @param publicKey 分配给开发商公钥
     * @param encode 字符集编码
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey, String encode)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update( content.getBytes(encode) );

            boolean bverify = signature.verify( Base64.decode(sign) );
            return bverify;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean doCheck(String content, String sign, String publicKey)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update( content.getBytes() );

            boolean bverify = signature.verify( Base64.decode(sign) );
            return bverify;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }


    public static void main(String[] args) throws Exception {
        String filepath="D://";

        //生成公钥和私钥文件
        CRTUtils.genKeyPair(filepath);

        System.out.println("--------------公钥加密私钥解密过程-------------------");
        String plainText="{\n" +
                "    \"userid\": \"65528\",\n" +
                "    \"shopid\": \"3023\",\n" +
                "    \"amount\":\"55.50\",\n" +
                "    \"type\": \"2\"\n" +
                "}";
        //公钥加密过程
        byte[] cipherData=CRTUtils.encrypt(CRTUtils.loadPublicKeyByStr(CRTUtils.loadPublicKeyByFile(filepath)),plainText.getBytes());
        String cipher=Base64.encode(cipherData);
        //私钥解密过程
//        byte[] res=CRTUtils.decrypt(CRTUtils.loadPrivateKeyByStr(CRTUtils.loadPrivateKeyByFile(filepath)), Base64.decode(cipher));
//        String restr=new String(res);
//        System.out.println("原文："+plainText);
//        System.out.println("加密："+cipher);
//        System.out.println("解密："+restr);
//        System.out.println();
//
//        System.out.println("--------------私钥加密公钥解密过程-------------------");
//        plainText="{\n" +
//                "    \"userid\": \"65528\",\n" +
//                "    \"shopid\": \"3023\",\n" +
//                "    \"amount\":\"55.50\",\n" +
//                "    \"type\": \"2\"\n" +
//                "}";
//        //私钥加密过程
//        cipherData=CRTUtils.encrypt(CRTUtils.loadPrivateKeyByStr(CRTUtils.loadPrivateKeyByFile(filepath)),plainText.getBytes());
//        cipher=Base64.encode(cipherData);
//        //公钥解密过程
//        res=CRTUtils.decrypt(CRTUtils.loadPublicKeyByStr(CRTUtils.loadPublicKeyByFile(filepath)), Base64.decode(cipher));
//        restr=new String(res);
//        System.out.println("原文："+plainText);
//        System.out.println("加密："+cipher);
//        System.out.println("解密："+restr);
//        System.out.println();
//
//        System.out.println("---------------私钥签名过程------------------");
//        String content="{\n" +
//                "    \"userid\": \"65528\",\n" +
//                "    \"shopid\": \"3023\",\n" +
//                "    \"amount\":\"55.50\",\n" +
//                "    \"type\": \"2\"\n" +
//                "}";
//        String signstr=RSASignature.sign(content,CRTUtils.loadPrivateKeyByFile(filepath));
//        System.out.println("签名原串："+content);
//        System.out.println("签名串："+signstr);
//        System.out.println();
//
//        System.out.println("---------------公钥校验签名------------------");
//        System.out.println("签名原串："+content);
//        System.out.println("签名串："+signstr);
//
//        System.out.println("验签结果："+RSASignature.doCheck(content, signstr, CRTUtils.loadPublicKeyByFile(filepath)));
//        System.out.println();

    }



}