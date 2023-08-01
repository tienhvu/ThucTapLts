package com.example.demo.Entity;

import java.util.HashMap;
import java.util.Map;

public class VerificationCodeStorage {
    private static Map<String, String> verificationCodes = new HashMap<>();

    public static void saveVerificationCode(String email, String verificationCode) {
        verificationCodes.put(email, verificationCode);
    }

    public static String getVerificationCode(String email) {
        return verificationCodes.get(email);
    }

    public static void deleteVerificationCode(String email) {
        verificationCodes.remove(email);
    }
}
