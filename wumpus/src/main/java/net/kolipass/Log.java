package main.java.net.kolipass;

/**
 * Created by kolipass on 09.12.13.
 */
public class Log {
    private static boolean DEFAULT_ON = true;

    private boolean on = true;

    public Log() {
        on = DEFAULT_ON;
    }

    public Log(boolean on) {
        this.on = on;
    }

    public void d(String s) {
        if (on) {
            System.out.println(s);
        }
    }

    public void printStackTrace(Exception ex) {
        if (on) {
            ex.printStackTrace();
        }
    }

}
