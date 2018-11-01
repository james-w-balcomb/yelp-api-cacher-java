package com.boringbalcomb.AtsUtilities;

public class AtsMiscellaneous {

    public static void takeItSlow() {
        try {
            System.out.println("Pausing for 100 milliseconds...");
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            // Unhandled exception: java.lang.InterruptedException
            System.out.println("java.lang.InterruptedException");
            ex.printStackTrace();
        }
    }

    public static void doRateLimitNaive() {
        try {
            System.out.println("Pausing for 100 milliseconds...");
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            // Unhandled exception: java.lang.InterruptedException
            System.out.println("java.lang.InterruptedException");
            ex.printStackTrace();
        }
    }

    public static void pause() {
        try {
            System.out.println("Pausing for 100 milliseconds...");
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            // Unhandled exception: java.lang.InterruptedException
            System.out.println("java.lang.InterruptedException");
            ex.printStackTrace();
        }
    }

}
