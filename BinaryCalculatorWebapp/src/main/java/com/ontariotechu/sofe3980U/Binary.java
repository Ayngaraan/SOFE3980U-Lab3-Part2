package com.ontariotechu.sofe3980U;

public class Binary {
    private String number = "0";

    public Binary(String number) {
        if (number == null || number.isEmpty()) {
            this.number = "0";
            return;
        }
        // Validate binary input (only 0s and 1s)
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch != '0' && ch != '1') {
                this.number = "0";
                return;
            }
        }
        // Remove leading zeros
        int beg;
        for (beg = 0; beg < number.length(); beg++) {
            if (number.charAt(beg) != '0') {
                break;
            }
        }
        this.number = (beg == number.length()) ? "0" : number.substring(beg);
    }

    public String getValue() {
        return this.number;
    }

    public static Binary add(Binary num1, Binary num2) {
        // ... (Keep your existing add method from Lab 1 or the provided code) ...
        // Placeholder for brevity:
        int ind1 = num1.number.length() - 1;
        int ind2 = num2.number.length() - 1;
        int carry = 0;
        String num3 = "";
        while (ind1 >= 0 || ind2 >= 0 || carry != 0) {
            int sum = carry;
            if (ind1 >= 0) sum += (num1.number.charAt(ind1--) - '0');
            if (ind2 >= 0) sum += (num2.number.charAt(ind2--) - '0');
            carry = sum / 2;
            sum = sum % 2;
            num3 = ((sum == 0) ? "0" : "1") + num3;
        }
        return new Binary(num3);
    }

    // --- REQUIRED IMPLEMENTATIONS FOR SUBMISSION ---

    public static Binary or(Binary num1, Binary num2) {
        int len1 = num1.number.length();
        int len2 = num2.number.length();
        int maxLen = Math.max(len1, len2);
        
        // Pad with leading zeros
        String n1 = String.format("%" + maxLen + "s", num1.number).replace(' ', '0');
        String n2 = String.format("%" + maxLen + "s", num2.number).replace(' ', '0');
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maxLen; i++) {
            char c1 = n1.charAt(i);
            char c2 = n2.charAt(i);
            // If either bit is 1, result is 1
            if (c1 == '1' || c2 == '1') {
                result.append("1");
            } else {
                result.append("0");
            }
        }
        return new Binary(result.toString());
    }

    public static Binary and(Binary num1, Binary num2) {
        int len1 = num1.number.length();
        int len2 = num2.number.length();
        int maxLen = Math.max(len1, len2);
        
        String n1 = String.format("%" + maxLen + "s", num1.number).replace(' ', '0');
        String n2 = String.format("%" + maxLen + "s", num2.number).replace(' ', '0');
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maxLen; i++) {
            char c1 = n1.charAt(i);
            char c2 = n2.charAt(i);
            // Both bits must be 1 for result to be 1
            if (c1 == '1' && c2 == '1') {
                result.append("1");
            } else {
                result.append("0");
            }
        }
        return new Binary(result.toString());
    }

    public static Binary multiply(Binary num1, Binary num2) {
        Binary result = new Binary("0");
        String n2 = num2.number;
        
        // Standard multiplication: shift and add
        for (int i = n2.length() - 1; i >= 0; i--) {
            if (n2.charAt(i) == '1') {
                String shift = num1.number + "0".repeat(n2.length() - 1 - i);
                result = Binary.add(result, new Binary(shift));
            }
        }
        return result;
    }
}