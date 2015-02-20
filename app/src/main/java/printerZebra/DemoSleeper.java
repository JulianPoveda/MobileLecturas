package printerZebra;

/**
 * Created by JULIANEDUARDO on 13/02/2015.
 */
public class DemoSleeper {

    private DemoSleeper() {

    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}