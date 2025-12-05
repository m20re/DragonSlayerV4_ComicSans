package org.unocapstone.dragonslair;

/**
 * Contains Data-relevant helper functions to generate dummy data
 */
public class Helpers {

    public static String generateRandomName() { return "user-" + Long.toHexString(System.nanoTime()); }

    public static String generateRandomPhoneNumber() {
        // Headless method of generating a number
        java.util.concurrent.ThreadLocalRandom rnd = java.util.concurrent.ThreadLocalRandom.current();
        int part1 = rnd.nextInt(200, 1000);
        int part2 = rnd.nextInt(0, 1000);
        int part3 = rnd.nextInt(0, 10000);
        return String.format("%03d-%03d-%04d", part1, part2, part3);
    }
}
