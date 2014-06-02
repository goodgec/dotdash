package com.groupa.dotdash.dotdash;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by himelica on 5/20/14.
 */
public class Translator {

    public static final long MAX_DOT_DURATION = 200;
    public static final long LETTER_BREAK_DURATION = 300;
    public static final long SPACE_DURATION = 1000;
    public static final long ELEMENT_DURATION = 1200; // Based on one word per minute read speed

    private static final ArrayList<String> morseAlphaCharacters = new ArrayList<String>(Arrays.asList(".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
            ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--",
            "-..-", "-.--", "--.."));
    private static final ArrayList<String> morseNumericCharacters = new ArrayList<String>(Arrays.asList("-----",".----","..---","...--","....-",".....","-....","--...",
            "---..","----."));

//    public Translator() {}

    public static long[] convertTextToMorse(String text, int wpm) {
        String morse = "";
        text = text.toUpperCase();

        for (char c : text.toCharArray()) {
            morse += convertCharToMorse(c) + "b";
        }

        ArrayList<Long> output = new ArrayList<Long>();
        output.add((long)0);
        for (char m : morse.toCharArray()){
            switch (m) {
                case '.':
                    output.add(ELEMENT_DURATION);
                    break;
                case '-':
                    output.add(ELEMENT_DURATION * 3);
                    break;
                case 'b':
                    output.add((long)0);
                    output.add(ELEMENT_DURATION);
                    output.add((long)0);
                    break;
                case '/':
                    output.add((long)0);
                    output.add(ELEMENT_DURATION * 5);
                    output.add((long)0);
                    break;
                default:
                    break;
            }
            output.add(ELEMENT_DURATION);
        }

        long[] timeOutput = new long[output.size()];
        for (int i = 0; i < output.size(); i++) {
            timeOutput[i] = output.get(i) / wpm;
        }

        return timeOutput;
    }

    public static String convertCharToMorse(char c) {
        if (c == '.') {
            return ".-.-.-";
        }
        else if (c == ',') {
            return "--..--";
        }
        else if (64 < c && c < 90) {
            return morseAlphaCharacters.get(c - 'A');
        }
        else if (47 < c && c <58) {
            return morseNumericCharacters.get(c - '0');
        }
        else if (c == ' '){
            return "/";
        }
        else {
            return "";
        }
    }

    public static char convertMorseToText(ArrayList<Long> durations) {
        String morseText = "";

        for (int i = 0; i < durations.size(); i ++) {
            if (durations.get(i) <= MAX_DOT_DURATION) {
                morseText += ".";
            }
            else {
                morseText += "-";
            }
        }

        if (morseAlphaCharacters.contains(morseText)) {
            return (char) (morseAlphaCharacters.indexOf(morseText) + 'A');
        }
        else if (morseNumericCharacters.contains(morseText)) {
            return (char) (morseNumericCharacters.indexOf(morseText) + '0');
        }
        else if (morseText.equals(".-.-.-")) {
            return '.';
        }
        else if (morseText.equals("--..--")) {
            return ',';
        }
        else {
            return '?';
        }
    }

    public static void outputMessage(Context context, long[] times) {
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
//        AudioManager beeper = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

//        ToneGenerator beeper = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, (int)(ToneGenerator.MAX_VOLUME * 0.85));

        if (DotDash.receiveAsVibrate){
            vibrator.vibrate(times, -1);
        }
//        if (DotDash.receiveAsBeep) {
//            for(int i = 1; i < times.length; i +=2) {
//                beeper.startTone(ToneGenerator.TONE_,(int)times[i]);
//                beeper.startTone(ToneGenerator.TONE_PROP_BEEP2, (int)times[i+1]);
//                //beeper.startTone(AudioManager.RINGER_MODE_SILENT, (int)times[i+1]);
//            }
//        }
//        beeper.release();
    }

    public static void sendMessage(Context context, Contact contact, String text) {
        SmsManager manager = SmsManager.getDefault();

        Message message = new Message(text, contact, true);
        if (message.getText().length() == 0) {
            Toast.makeText(context, "Invalid empty text", Toast.LENGTH_LONG).show();
            return;
        }
        contact.getConversation().addMessage(message);
        manager.sendTextMessage(message.getContact().getNumber(), null, message.getText(), null, null);
        DataManager.getInstance().addMessageToDb(message);
    }
}
