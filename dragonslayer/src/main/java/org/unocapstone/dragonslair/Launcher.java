package org.unocapstone.dragonslair;

/**
 * Helper class to launch the application. Java has a hard time launching classes of class Application, so
 * this is a neat little hack to get us up and running.
 */
public class Launcher {
    public static void main(String[] args) {
        System.out.println("Hello World");
        Log.LogMessage("Program Opened");
        Main.main(args);
    }
}
