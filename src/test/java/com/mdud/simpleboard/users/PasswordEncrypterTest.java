package com.mdud.simpleboard.users;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordEncrypterTest {

    @Test
    public void encrypt() {
        String testPass = "test123";
        String encryptTest = PasswordEncrypter.encrypt(testPass);
        assertNotEquals(testPass, encryptTest);
        System.out.println(encryptTest);
    }
}