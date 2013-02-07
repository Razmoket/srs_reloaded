package fr.afnic.commons.beans.customer;

import java.util.Random;

public class EppPasswordGenerator {
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final int LENGTH = 8;

    private static final EppPasswordGenerator INSTANCE = new EppPasswordGenerator();

    public static String generatePassword() {
        return INSTANCE.generatePasswordImpl();
    }

    protected String generatePasswordImpl() {
        final String[] voyelles = { "A", "E", "I", "O", "U", "Y" };
        final String[] consonnes = { "Z", "R", "T", "P", "S", "D", "F", "G", "J", "K", "L", "M", "W", "X", "C", "V", "B", "N" };

        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < LENGTH; i++) {
            if (i % 2 == 0) {
                // consonne
                final int rand = EppPasswordGenerator.RANDOM.nextInt(consonnes.length);
                buffer.append(consonnes[rand]);
            } else {
                // voyelle
                final int rand = EppPasswordGenerator.RANDOM.nextInt(voyelles.length);
                buffer.append(voyelles[rand]);
            }
        }

        this.addNum(buffer);
        this.addNum(buffer);

        return buffer.toString();

    }

    private void addNum(StringBuffer buffer) {
        int num = EppPasswordGenerator.RANDOM.nextInt(10);
        int posNum = EppPasswordGenerator.RANDOM.nextInt(buffer.toString().length() + 1);
        buffer.insert(posNum, Integer.toString(num));
    }

}
