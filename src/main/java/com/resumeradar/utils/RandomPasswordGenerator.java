package com.resumeradar.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomPasswordGenerator {

	private static final int length = 10;
	
    public static String generateStrongPassword() {
        
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_+=<>?";

        SecureRandom random = new SecureRandom();
        List<Character> passwordChars = new ArrayList<>();

        // Ensure at least one character from each category
        passwordChars.add(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        passwordChars.add(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        passwordChars.add(digits.charAt(random.nextInt(digits.length())));
        passwordChars.add(specialChars.charAt(random.nextInt(specialChars.length())));

        // Fill the remaining characters randomly
        String allChars = upperCaseLetters + lowerCaseLetters + digits + specialChars;
        for (int i = 4; i < length; i++) {
            passwordChars.add(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Shuffle to prevent predictable sequence
        Collections.shuffle(passwordChars);

        // Build the final password string
        StringBuilder password = new StringBuilder();
        for (char ch : passwordChars) {
            password.append(ch);
        }

        return password.toString();
    }

    
}
