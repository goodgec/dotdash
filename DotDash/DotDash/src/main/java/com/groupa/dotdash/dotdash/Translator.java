package com.groupa.dotdash.dotdash;

import java.util.ArrayList;

/**
 * Created by himelica on 5/20/14.
 */
public class Translator {

    public String convertTextToMorse(String text) {
        String output = "";
        text = text.toUpperCase();
        for (char c : text.toCharArray()) {
            output += convertCharToMorse(c);
        }

        return output;
    }

    public String convertCharToMorse(char c) {
        switch(c) {
            case 'A':
                return ".-";
            case 'B':
                return "-...";
            case 'C':
                return "-.-.";
            case 'D':
                return "-..";
            case 'E':
                return ".";
            case 'F':
                return "..-.";
            case 'G':
                return "--.";
            case 'H':
                return "....";
            case 'I':
                return "..";
            case 'J':
                return ".---";
            case 'K':
                return "-.-";
            case 'L':
                return ".-..";
            case 'M':
                return "--";
            case 'N':
                return "-.";
            case 'O':
                return "---";
            case 'P':
                return ".--.";
            case 'Q':
                return "--.-";
            case 'R':
                return ".-.";
            case 'S':
                return "...";
            case 'T':
                return "-";
            case 'U':
                return "..-";
            case 'V':
                return "...-";
            case 'W':
                return ".--";
            case 'X':
                return "-..-";
            case 'Y':
                return "-.--";
            case 'Z':
                return "--..";
            case '0':
                return "-----";
            case '1':
                return ".----";
            case '2':
                return "..---";
            case '3':
                return "...--";
            case '4':
                return "....-";
            case '5':
                return ".....";
            case '6':
                return "-....";
            case '7':
                return "--...";
            case '8':
                return "---..";
            case '9':
                return "----.";
            case '.':
                return ".-.-.-";
            case ',':
                return "--..--";
            case ' ':
                return "/";
            default:
                return "...-..-";
        }
    }

    public String convertMorseToText(ArrayList<Double[]> pressTimes) {
        String output = "";
        // press times come from Rn minus Pn
        // press times 0 to x is .   x to y is -
        // wait times come from Pn minus Rn-1
        // if long wait times then its a space

//        for i in input:
//            while Pi - Ri-1 is not space:
//                //creating a word
//                while Pi - Ri-1 is not letter break:
//                    // creating a letter
//                    translate letter
//                    append letter
//            append a space



        return output;
    }

}
