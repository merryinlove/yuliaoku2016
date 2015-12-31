package com.xya.csu.utility;

import java.security.Key;  
        
import javax.crypto.Cipher;  
import javax.crypto.SecretKeyFactory;  
import javax.crypto.spec.DESedeKeySpec;  
import javax.crypto.spec.IvParameterSpec;  
        
/** 
  * 3DES加密工具类
  */ 
public class Des3 {  
     private final static String secretKey = "g#h%ujIdLyzUgXzg40^48JZ2" ;
     private final static String iv = "06174950" ;
     private final static String encoding = "utf-8" ;  


     public static String encode(String plainText) throws Exception {  
         Key deskey;
         DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());  
         SecretKeyFactory keyfactory = SecretKeyFactory.getInstance( "desede" );  
         deskey = keyfactory.generateSecret(spec);
         Cipher cipher = Cipher.getInstance( "desede/CBC/PKCS5Padding" );  
         IvParameterSpec ips = new IvParameterSpec(iv.getBytes());  
         cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);  
         byte [] encryptData = cipher.doFinal(plainText.getBytes(encoding));  
         return Base64.encode(encryptData);  
     }

     public static String decode(String encryptText) throws Exception {  
         Key deskey;
         DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());  
         SecretKeyFactory keyfactory = SecretKeyFactory.getInstance( "desede" );  
         deskey = keyfactory.generateSecret(spec);  
         Cipher cipher = Cipher.getInstance( "desede/CBC/PKCS5Padding" );  
         IvParameterSpec ips = new IvParameterSpec(iv.getBytes());  
         cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
         byte [] decryptData = cipher.doFinal(Base64.decode(encryptText));
         return new String(decryptData, encoding);  
     }  
} 