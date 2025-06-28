import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
    private static final String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String digits = "0123456789";
    private static final String symbols = "!\"#$%&\'()*+,-./:;<=>?@[\\]^_`";
    private static final String[] alphabet = { lowerCase, upperCase, digits, symbols };

    public static void main(String[] args) throws Exception {
        System.out.println(".Healthy Password.");
        System.out.println("1) Evaluate password.");
        System.out.println("2) Generate a healthy password.");
        int option = scanner.nextInt();
        scanner.nextLine();
        if (option != 1 && option != 2) {
            throw new Exception("Please pick 1 or 2.");
        }
        switch (option) {
            case 1:
                System.out.println("Enter password:");
                String password = scanner.nextLine();
                // [0] = length score, [1] = complexity score
                double[] password_score = score(password);
                System.out.printf("Length score: %.2f%%\n", password_score[0] * 100);
                System.out.printf("Complexity score: %.2f%%\n", password_score[1] * 100);

                break;
            case 2:
                String passwordGenerated = generate();
                System.out.println("Here's a healthy password for you:");
                System.out.println(passwordGenerated);
                break;
        }
    }

    private static String generate() {
        Random random = new Random();
        int length = random.nextInt(14, 22);
        StringBuilder password = new StringBuilder();
        boolean[] presence = { false, false, false, false };
        for (int i = 0; i < length; i++) {
            int characterType = random.nextInt(0, 4);
            int index;
            switch (characterType) {
                case 0:
                    // lowerCase
                    index = random.nextInt(26);
                    password.append(lowerCase.charAt(index));
                    presence[0] = true;
                    break;
                case 1:
                    // upperCase
                    index = random.nextInt(26);
                    password.append(upperCase.charAt(index));
                    presence[1] = true;
                    break;
                case 2:
                    // digit
                    index = random.nextInt(10);
                    password.append(digits.charAt(index));
                    presence[2] = true;
                    break;
                case 3:
                    // symbol
                    index = random.nextInt(symbols.length());
                    password.append(symbols.charAt(index));
                    presence[3] = true;
                    break;
            }
        }
        for (int i = 0; i < presence.length; i++) {
            if (presence[i] == false) {
                int count = random.nextInt(5);
                for (int j = 0; j < count; i++) {
                    int index = random.nextInt(alphabet[i].length());
                    password.append(alphabet[i].charAt(index));
                }
            }
        }
        return shuffle(password.toString());
    }

    private static String shuffle(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        Collections.shuffle(characters);
        StringBuilder result = new StringBuilder(input.length());
        for (char c : characters) {
            result.append(c);
        }
        return result.toString();
    }

    private static double[] score(String password) {
        /*
         * 1. Length:
         * Longer passwords are significantly harder to crack. Aim for at least 12-16
         * characters, and even longer is better.
         * 2. Complexity:
         * Include a mix of uppercase and lowercase letters, numbers, and symbols.
         * 3. Uniqueness:
         * Do not reuse the same password across multiple accounts. This prevents a
         * single compromised password from exposing all your accounts.
         * 4. Randomness:
         * Avoid using dictionary words, names, dates, or any easily guessable
         * information. Instead, create a random string of characters or use a
         * passphrase of random words.
         */
        // extract caracteristics of password
        int p_length = password.length();
        double upperCasePercentage = (double) upperCaseCount(password) / p_length;
        double lowerCasePercentage = (double) lowerCaseCount(password) / p_length;
        double digitsPercentage = (double) digitsCount(password) / p_length;
        double symbolsPercentage = (double) symbolsCount(password) / p_length;

        int length_score = 0;
        if (p_length < 12)
            length_score = 0;
        else if (p_length >= 12 && p_length < 16)
            length_score = 1;
        else
            length_score = 2;

        int complexity_score = 0;
        if (lowerCasePercentage > 0)
            complexity_score++;
        if (upperCasePercentage > 0)
            complexity_score++;
        if (digitsPercentage > 0)
            complexity_score++;
        if (symbolsPercentage > 0)
            complexity_score++;

        return new double[] { (double) length_score / 3, (double) complexity_score / 4 };
    }

    private static int upperCaseCount(String password) {
        int count = 0;
        for (int i = 0; i < password.length(); i++)
            if (Character.isUpperCase(password.charAt(i)))
                count++;
        return count;
    }

    private static int lowerCaseCount(String password) {
        int count = 0;
        for (int i = 0; i < password.length(); i++)
            if (Character.isLowerCase(password.charAt(i)))
                count++;
        return count;
    }

    private static int digitsCount(String password) {
        int count = 0;
        for (int i = 0; i < password.length(); i++)
            if (Character.isDigit(password.charAt(i)))
                count++;
        return count;
    }

    private static int symbolsCount(String password) {
        int count = 0;
        for (int i = 0; i < password.length(); i++)
            if (!Character.isDigit(password.charAt(i)) && !Character.isLowerCase(password.charAt(i))
                    && !!Character.isUpperCase(password.charAt(i)))
                count++;
        return count;
    }
}
