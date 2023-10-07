package org.example;

public class Main {
    public static void main(String[] args) {
        String encrypt = S_DES.encrypt("10001000", "0001011111", false);
        System.out.println(encrypt);
    }

}