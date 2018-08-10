package com.mdud.simpleboard.users;

public class PasswordEncrypter {
    public static String encrypt(String password) {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = password.getBytes();
        for(byte b : bytes)
            stringBuilder.append(b * 13);
        return stringBuilder.toString();
    }
}
