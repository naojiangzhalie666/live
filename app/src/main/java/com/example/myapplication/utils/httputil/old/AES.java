package com.example.myapplication.utils.httputil.old;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	/*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    public static final String sKey = "EDACDD5B3000392207DCFBE5023E4063";//key，可自行修改
    private String ivParameter = "QbNNKiCVbnnsuj@n";//偏移量,可自行修改
    private static AES instance = null;

    private AES() {

    }

    public static AES getInstance() {
        if (instance == null)
            instance = new AES();
        return instance;
    }
    /**
     * 加密
     * @param newKey
     * @return
     * @throws Exception
     */
    public String baseencrypt(String newKey, String sSrc) throws Exception {
        //MessageUtils.ShowLogInfo("新密钥"+newKey);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = newKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
//        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return Base64.encode(encrypted);// 此处使用BASE64做转码。
    }

    // 解密
    /*public String decrypt(String sSrc) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1=Base64.decode(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }*/
    
    // 解密
    public String decrypt(String sSrc, String newKey) throws Exception {
        try {
        	int keyLen = newKey.length();
            byte[] raw = newKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
//            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1=Base64.decode(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }
    /*------------old------------*/
    public String getNewKey(String time){
        String sKeySub = sKey.substring(0, sKey.length() - 5);
        String newKey = sKeySub + time.substring(time.length() - 5, time.length());
        return newKey;
    }

    /**
     * 加密
     * @param sSrc 适用于首次加密公钥
     * @return
     * @throws Exception
     */
    public String encrypt(String sSrc, String time) throws Exception {

        String sKeySub = sKey.substring(0, sKey.length() - 5);
        String newKey = sKeySub + time.substring(time.length() - 5, time.length());
        return baseencrypt(newKey,sSrc);
    }
}
