package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static final String END_OF_SENTENCE_REGEX = "[A-Za-z0-9]*[.!?]";
    private static final char[] VOWELS = {'a', 'e', 'i', 'o', 'u', 'y'};

    public static void main(String[] args) {
        String fileName = args[0];
        try (Scanner scanner = new Scanner(new File(fileName))) {
            TextAnalysis textAnalysis = analyseText(scanner);

            printAnalysis(textAnalysis);

            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            Scanner userInput = new Scanner(System.in);
            String choice = userInput.nextLine();

            printIndex(choice, textAnalysis);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File not found!");
        }
    }

    private static void printIndex(String choice, TextAnalysis textAnalysis) {
        IndexEnum index = IndexEnum.valueOf(choice.toUpperCase());
        switch (index){
            case ARI:
                calculateARI(textAnalysis);
                break;
            case FK:
                calculateFK(textAnalysis);
                break;
            case SMOG:
                calculateSMOG(textAnalysis);
                break;
            case CL:
                calculateCL(textAnalysis);
                break;
            case ALL:
                calculateARI(textAnalysis);
                calculateFK(textAnalysis);
                calculateSMOG(textAnalysis);
                calculateCL(textAnalysis);
                break;
        }

        System.out.println("This text should be understood in average by 14.25-year-olds.");
    }

    private static TextAnalysis analyseText(Scanner scanner) {
        TextAnalysis textAnalysis = new TextAnalysis();

        System.out.println("The text is:");
        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            System.out.println(input);

            List<String> words = List.of(input.split("\\s"));
            textAnalysis.incrementWordsBy(words.size());

            // In case the last word does not contain a symbol at the end
            if (!words.get(words.size() - 1).matches(END_OF_SENTENCE_REGEX)) {
                textAnalysis.incrementSentences();
            }

            for (String word : words) {
                textAnalysis.incrementCharactersBy(word.length());

                int syllables = countSyllables(word.toLowerCase());

                textAnalysis.incrementSyllablesBy(syllables);
                textAnalysis.incrementPolysyllables(syllables);

                // I've found the ending word
                if (word.matches(END_OF_SENTENCE_REGEX)) {
                    textAnalysis.incrementSentences();
                }
            }
        }

        return textAnalysis;
    }

    private static void printAnalysis(TextAnalysis textAnalysis) {
        System.out.println("Words: " + textAnalysis.getWords());
        System.out.println("Sentences: " + textAnalysis.getSentences());
        System.out.println("Characters: " + textAnalysis.getCharacters());
        System.out.println("Syllables: " + textAnalysis.getSyllables());
        System.out.println("Polysyllables: " + textAnalysis.getPolysyllables());
    }

    private static int countSyllables(String word) {
        int vowels = 0;
        int upperIdx = word.length() - 1;

        char theLast = word.charAt(upperIdx);

        if (String.valueOf(theLast).matches("[,.?!]")){
            upperIdx--;
        }

        for (int c = 0; c <= upperIdx; c++) {
            if (isVowel(word.charAt(c))) {
                vowels++;
            }

            if (c < upperIdx) {
                if (consecutiveVowels(word.charAt(c), word.charAt(c + 1))) {
                    vowels--;
                }
            } else if (word.charAt(c) == 'e') {
                // Last letter is 'e'
                vowels--;
            }
        }

        return vowels <= 0 ? 1 : vowels;
    }

    private static boolean consecutiveVowels(char c1, char c2) {
        return isVowel(c1) && isVowel(c2);
    }

    private static boolean isVowel(char letter) {
        for (char vowel : VOWELS) {
            if (letter == vowel) {
                return true;
            }
        }

        return false;
    }

    private static void calculateARI(TextAnalysis textAnalysis) {
        float charsOverWords = (float) textAnalysis.getCharacters() / textAnalysis.getWords();
        float wordsOverSentences = (float) textAnalysis.getWords() / textAnalysis.getSentences();

        float ari = (float) (4.71 * charsOverWords + (0.5 * wordsOverSentences) - 21.43);

        String age = getAgeByIndex(ari);

        System.out.println("Automated Readability Index: " + ari + " (about " + age + "-year-olds).");
    }

    private static void calculateFK(TextAnalysis textAnalysis) {
        float wordsOverSentences = (float) textAnalysis.getWords() / textAnalysis.getSentences();
        float syllablesOVerWords = (float) textAnalysis.getSyllables() / textAnalysis.getWords();

        float fk = (float) (0.39 * wordsOverSentences + 11.8 * syllablesOVerWords - 15.59);
        String age = getAgeByIndex(fk);

        System.out.println("Flesch–Kincaid readability tests: " + fk + " (about " + age + "-year-olds).");
    }

    private static void calculateSMOG(TextAnalysis textAnalysis) {
        float polysyllablesOverSentences = textAnalysis.getPolysyllables() * 30 / textAnalysis.getSentences();

        float smog = (float) (1.043 * Math.sqrt(polysyllablesOverSentences) + 3.1291);
        String age = getAgeByIndex(smog);

        System.out.println("Simple Measure of Gobbledygook: " + smog + " (about " + age + "-year-olds).");
    }

    private static void calculateCL(TextAnalysis textAnalysis) {
        float averageChars = (float) textAnalysis.getCharacters() / textAnalysis.getWords() * 100;
        float averageSentences = (float) textAnalysis.getSentences() / textAnalysis.getWords() * 100;

        float cl = (float) (0.0588 * averageChars - 0.296 * averageSentences - 15.8);
        String age = getAgeByIndex(cl);

        System.out.println("Coleman–Liau index: " + cl + " (about " + age + "-year-olds).");
    }

    private static String getAgeByIndex(float index) {
        return Objects.requireNonNull(
                ScoreEnum.getByScore((int) (Math.ceil(index)))
        ).age.split("-")[1];
    }
}
