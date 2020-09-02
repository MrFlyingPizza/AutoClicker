import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

class ClickingRunnable implements Runnable {

    // concurrency control
    private final AtomicBoolean running = new AtomicBoolean();

    // delay between clicking operation (Default 1000 ms)
    private int delay = 1000;

    Robot bot;

    // constructor
    public ClickingRunnable() throws AWTException {
        bot = new Robot();
    }

    // start runnable and initialize thread
    public void start(int delay) {
        this.delay = delay;
        if (bot == null) {
            System.err.println("Failed to initialize simulation instance (Robot)!");
            return;
        };
        Thread clickingThread = new Thread(this);
        clickingThread.start();
    }

    // stop runnable
    public void stop() {
        running.set(false);
    }

    public boolean getRunning() {
        return running.get();
    }

    @Override
    public void run() {

        running.set(true);
        System.out.println("Delay: " + delay);
        System.out.println("Running clicking loop");
        while (running.get()) {

            bot.mousePress(MouseEvent.BUTTON1_MASK);
            bot.mouseRelease(MouseEvent.BUTTON1_MASK);
            bot.delay(delay);
        }
    }
}