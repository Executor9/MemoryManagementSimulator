package utils;

public class Logger {
    private static boolean enabled = true;

    public static void enable() {
        enabled = true;
    }

    public static void disable() {
        enabled = false;
    }

    public static void log(String message) {
        if(enabled) {
            System.out.println("[LOG]" + message);
        }
    }

    public static void warn(String message) {
        if(enabled) {
            System.out.println("[WAR]" + message);
        }
    }

    public static void err(String message) {
        if(enabled) {
            System.out.println("[ERR]" + message);
        }
    }
}
