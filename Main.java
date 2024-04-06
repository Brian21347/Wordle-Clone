
/**
 * Name: Brian Zhou
 * Project Title: Wordle
 * Date: 1/29/2024
 * Period: 5th
 * Please note that list of words were taken from https://word-lists.com/word-lists/list-of-common-5-letter-words/
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.HashMap;

public class Main {
    private static final String WORD_FILE = "sorted.txt";
    private static String[][] guesses = new String[12][5];
    private static String[][] keyBoard = {
            { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", },
            { "A", "S", "D", "F", "G", "H", "J", "K", "L", },
            { "Z", "X", "C", "V", "B", "N", "M", }
    };
    private static final String RESET = "\u001B[0m";
    private static final String CORRECT_COLOR = "\u001B[42m";
    private static final String CORRECT = "*";
    private static final String PARTIALLY_CORRECT_COLOR = "\u001B[43m";
    private static final String PARTIALLY_CORRECT = "@";
    private static final String WRONG_COLOR = "\u001B[41m";
    private static final String WRONG = "X";
    private static final int NUM_WORDS = 9977;
    private static String[] words;
    private static String target;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        words = new String[NUM_WORDS];
        getWords();
        target = getRandomWord().toUpperCase();
        int guessIndex = 0;
        int guessNumer = 0;
        while (guessIndex < 12) {
            draw();

            String tryWord = getInput(scanner).toUpperCase();

            guesses[guessIndex] = wordToArray(tryWord);
            guessIndex++;
            String[] hint = getHint(tryWord);
            guesses[guessIndex] = hint;
            guessIndex++;
            updateKeyBoardColor(hint, tryWord);
            guessNumer++;

            if (tryWord.equals(target)) {
                draw();
                System.out.println("You got it!");
                System.out.println("You used " + guessNumer + " guesse(s)");
                scanner.close();
                return;
            }
        }
        draw();
        System.out.println("Sorry, you ran out of guesses");
        System.out.println("The word was: " + target);
        scanner.close();
    }

    private static void draw() {
        clearScreen();
        printGuesses();
        printKeyBoard();
    }

    private static void updateKeyBoardColor(String[] hint, String word) {
        for (int i = 0; i < keyBoard.length; i++) {
            for (int j = 0; j < keyBoard[i].length; j++) {
                for (int ii = 0; ii < hint.length; ii++) {
                    if (!stripColor(keyBoard[i][j]).equals(String.valueOf(word.charAt(ii)))) {
                        continue;
                    }
                    keyBoard[i][j] = changeKeyColor(hint[ii], keyBoard[i][j]);
                }
            }
        }
    }

    private static String changeKeyColor(String character, String key) {
        if (key.contains(CORRECT_COLOR)) {
            return key;
        }

        String keyNoColor = stripColor(key);

        if (character.contains(CORRECT)) {
            return character.replace(CORRECT, keyNoColor);
        }
        if (character.contains(PARTIALLY_CORRECT)) {
            return character.replace(PARTIALLY_CORRECT, keyNoColor);
        }

        if (key.contains(PARTIALLY_CORRECT_COLOR)) {
            return key;
        }

        return character.replace(WRONG, keyNoColor);
    }

    private static String stripColor(String str) {
        if (str.contains(CORRECT_COLOR)) {
            str = str.replace(CORRECT_COLOR, "");
        }
        if (str.contains(PARTIALLY_CORRECT_COLOR)) {
            str = str.replace(PARTIALLY_CORRECT_COLOR, "");
        }
        if (str.contains(WRONG_COLOR)) {
            str = str.replace(WRONG_COLOR, "");
        }
        if (str.contains(RESET)) {
            str = str.replace(RESET, "");
        }
        return str;
    }

    private static void printKeyBoard() {
        /*
         * Q W E R T Y U I O P
         * _A S D F G H J K L_
         * ___Z X C V B N M___
         */
        for (int i = 0; i < keyBoard.length; i++) {
            if (i != 0) {
                System.out.print(i == 1 ? " " : "   ");
            }
            for (int j = 0; j < keyBoard[i].length; j++) {
                System.out.print(keyBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void printGuesses() {
        for (int i = 0; i < guesses.length; i++) {
            System.out.print("| ");
            for (int j = 0; j < guesses[i].length; j++) {
                System.out.print((guesses[i][j] == null) ? " " : guesses[i][j]);
            }
            System.out.println(" |");
        }
    }

    private static String[] wordToArray(String word) {
        String[] s = new String[word.length()];
        for (int i = 0; i < word.length(); i++) {
            s[i] = String.valueOf(word.charAt(i));
        }
        return s;
    }

    private static HashMap<Character, Integer> countChars(String word) {
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            if (map.containsKey(word.charAt(i))) {
                map.replace(word.charAt(i), map.get(word.charAt(i)) + 1);
            } else {
                map.put(word.charAt(i), 1);
            }
        }
        return map;
    }

    private static String[] getHint(String word) {
        String[] hint = new String[word.length()];
        HashMap<Character, Integer> tmap = countChars(target);
        HashMap<Character, Integer> gmap = countChars(word);
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == target.charAt(i)) {
                hint[i] = CORRECT_COLOR + CORRECT + RESET;
                tmap.replace(word.charAt(i), tmap.get(word.charAt(i)) - 1);
                gmap.replace(word.charAt(i), gmap.get(word.charAt(i)) - 1);
            }
        }
        for (int i = 0; i < word.length(); i++) {
            if (tmap.containsKey(word.charAt(i))) {
                if (hint[i] != null) {
                    continue;
                }
                if (tmap.get(word.charAt(i)) <= 0 || gmap.get(word.charAt(i)) <= 0) {
                    hint[i] = WRONG_COLOR + WRONG + RESET;
                    continue;
                }
                hint[i] = PARTIALLY_CORRECT_COLOR + PARTIALLY_CORRECT + RESET;
                tmap.replace(word.charAt(i), tmap.get(word.charAt(i)) - 1);
                gmap.replace(word.charAt(i), gmap.get(word.charAt(i)) - 1);
            } else {
                hint[i] = WRONG_COLOR + "X" + RESET;
            }
        }
        return hint;
    }

    private static String getInput(Scanner scanner) {
        String input = "";
        String prompt = "Please enter in your guess: \n>>>";
        while (!isWord(input)) {
            System.out.print(prompt);
            input = scanner.nextLine();
            prompt = "Sorry, your last input was not a five letter word, please try again.\n>>>";
        }
        return input;
    }

    private static boolean isWord(String word) {
        if (word.length() != 5)
            return false;
        return binarySearch(words, word) != -1;
    }

    private static int binarySearch(String[] arr, String str) {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (arr[m].equals(str))
                return m;
            if (arr[m].compareTo(str) < 0)
                l = m + 1;
            else
                r = m - 1;
        }
        return -1;
    }

    private static void getWords() throws FileNotFoundException {
        File file = new File(WORD_FILE);
        Scanner scanner = new Scanner(file);
        int i = 0;
        while (scanner.hasNextLine()) {
            words[i] = scanner.nextLine();
            i++;
        }
        scanner.close();
    }

    private static String getRandomWord() {
        return words[(int) (Math.random() * NUM_WORDS)];
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
