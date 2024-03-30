package com.example.loupgarou;

import java.util.Random;

public class RandomCodeGenerator {

    public static String generateCode(int length){
        boolean codeAlreadyInUse = true;
        int leftLetter = 97; // letter 'a'
        int rightLetter = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        String generatedCode = "";
        while(codeAlreadyInUse){
            for (int i = 0; i < length; i++) {
                int randomChoosedNumber = leftLetter + (int)
                        (random.nextFloat() * (rightLetter - leftLetter + 1));
                buffer.append((char) randomChoosedNumber);
            }
            generatedCode = buffer.toString();
            /*check if generatedCode is used in another room, break if not: clear the buffer and try
            again*/
            codeAlreadyInUse = false; //example
        }

        return generatedCode;

    }
}
