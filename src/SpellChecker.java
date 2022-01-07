/*
 * The words.txt file is from the standard unix dictionary, found in
 * /usr/share/dict/words on mac os systems
 */
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Scanner;

public class SpellChecker {
    static SeparateChainingHashST<String, String> dictionary;
    static ArrayList<String> possibleWords = new ArrayList<String>();

    private static void initDictionary(SeparateChainingHashST<String, String> dictionary, String fileName) {
        In input = new In(fileName);

        while (!input.isEmpty()) {
            String s = input.readLine();
            dictionary.put(s, "");
        }
    }

    public SpellChecker(String fileName) {
        dictionary = new SeparateChainingHashST<String, String>();
        initDictionary(dictionary, fileName);
    }

    private static void adjCheck(String word) {
        char[] chars = word.toCharArray();
        char temp;
        for(int i = 0; i < chars.length - 1; i++) {
            temp = chars[i];
            chars[i] = chars[i+1];
            chars[i+1] = temp;
            possibleWords.add(new String(chars));
            chars = word.toCharArray();
        }
    }

    private static void addCheck(String word) {
        //add front
        String temp = word;
        for(char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            temp = alphabet + word;
            possibleWords.add(temp);
            temp = word;
        }
        //add back
        for(char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            temp = word + alphabet;
            possibleWords.add(temp);
            temp = word;
        }
    }

    //helper method to do all 3 transformations
    public static void transformations(String word) {
        addCheck(word);
        removeCheck(word);
        adjCheck(word);
    }

    private static void removeCheck(String word) {
        char[] chars = word.toCharArray();
        char temp;

        //remove from back
        StringBuilder back = new StringBuilder(word);
        back.deleteCharAt(chars.length - 1);
        possibleWords.add(back.toString());

        //remove from front
        StringBuilder front = new StringBuilder(word);
        front.deleteCharAt(0);
        possibleWords.add(front.toString());
    }

    public static void main(String[] args) {
        SpellChecker spellChecker = new SpellChecker("words.txt");

        System.out.println("Welcome to spell checker. Press '0'' to exit...");
        Scanner scan = new Scanner(System.in);
        String input;
        boolean mistake = false;
        do {
            System.out.println("Please input a word below:");
            input = scan.next();
            if(input.equals("0")) continue;

            for (String word : dictionary.keys()) {
                if (input.equals(word)) {
                    System.out.println("No mistakes found");
                    mistake = false;
                    System.out.println();
                    break;
                } else {
                    mistake = true;
                }
            }
            if (mistake) {
                transformations(input);
                ArrayList<String> fixes = new ArrayList<String>();

                //Explanation: I thought the second option was better because it felt like the simpler approach and in my eyes
                //simpler is better if it isn't more costly. It sounds like you need to do more operations for the other option.
                for (String check : possibleWords) {
                    for (String word : dictionary.keys()) {
                        if (check.equals(word)) {
                            fixes.add(word);
                        }
                    }
                }
                if (fixes.isEmpty()) {
                    System.out.println("What in the world are you trying to spell??");
                } else {
                    System.out.println("Did you mean to spell: " + fixes);
                    fixes.clear();
                }
                possibleWords.clear();
                System.out.println();
            }
            // Note: When running the code, hold down the Control key while pressing D to end the program.
        } while (!input.equals("0"));
    }
}
