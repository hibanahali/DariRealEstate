package tn.esprit.dari.utils;

public class Passwordgenerator {

    protected static final int MAX_LENGTH = 30;
    
    private static java.util.Random r = new java.util.Random();
    private static final String DIGITS = "23456789";
    private static final String LOCASE_CHARACTERS = "abcdefghjkmnpqrstuvwxyz";
    private static final String UPCASE_CHARACTERS = "ABCDEFGHJKMNPQRSTUVWXYZ";
    private static final String SYMBOLS = "@#$%!";
    private static final String ALL = DIGITS + LOCASE_CHARACTERS + UPCASE_CHARACTERS + SYMBOLS;
    private static final char[] upcaseArray = UPCASE_CHARACTERS.toCharArray();
    private static final char[] locaseArray = LOCASE_CHARACTERS.toCharArray();
    private static final char[] digitsArray = DIGITS.toCharArray();
    private static final char[] symbolsArray = SYMBOLS.toCharArray();
    private static final char[] allArray = ALL.toCharArray();
    
    public static String generatePassword() {
        StringBuilder sb = new StringBuilder();
 
        // get at least one lowercase letter
        sb.append(locaseArray[r.nextInt(locaseArray.length)]);
 
        // get at least one uppercase letter
        sb.append(upcaseArray[r.nextInt(upcaseArray.length)]);
 
        // get at least one digit
        sb.append(digitsArray[r.nextInt(digitsArray.length)]);
 
        // get at least one symbol
        sb.append(symbolsArray[r.nextInt(symbolsArray.length)]);
 
        // fill in remaining with random letters
        for (int i = 0; i < MAX_LENGTH - 4; i++) {
            sb.append(allArray[r.nextInt(allArray.length)]);
        }
 
        return sb.toString();
    }

}
