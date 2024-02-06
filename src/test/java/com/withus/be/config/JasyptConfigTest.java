package com.withus.be.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {

    @Test
    @DisplayName("암호화 테스트")
    void stringEncryptor() {
        String url = "url";
        String username = "username";
        String password = "password";

        System.out.println("encrypted url : " + jasyptEncoding(url));
        System.out.println("encrypted username : " + jasyptEncoding(username));
        System.out.println("encrypted password : " + jasyptEncoding(password));
    }

    public String jasyptEncoding(String value) {
        StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
        stringEncryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        stringEncryptor.setPassword("testKey");
        stringEncryptor.setIvGenerator(new RandomIvGenerator());
        return stringEncryptor.encrypt(value);
    }
}
