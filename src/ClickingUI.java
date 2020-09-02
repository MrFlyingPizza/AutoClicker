import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;

import javax.swing.*;
import java.awt.*;

public class ClickingUI extends JFrame {

    private JPanel mainPanel;
    private JSpinner delaySpinner;
    private JButton controlButton;
    private JLabel delayLabel;

    private ClickingRunnable clickingInstance;

    private void startClickingInstance() {
        System.out.println("Starting clicking instance");
        clickingInstance.start((int) delaySpinner.getValue());
        controlButton.setText("Stop");
    }

    private void stopClickingInstance() {
        clickingInstance.stop();
        controlButton.setText("Start");
    }

    private void changeClickingState() {

        if (clickingInstance.getRunning()) {
            System.out.println("Attempting to end clicking.");
            stopClickingInstance();
        } else {
            System.out.println("Attempting to start clicking.");
            startClickingInstance();
        }
    }

    public ClickingUI() {
        // create instance of clicking runnable, exit on fail
        try {
            clickingInstance = new ClickingRunnable();
            System.out.println("instance created");
        } catch (AWTException e) {
            System.err.println("Failed to create clicking runnable!");
            e.printStackTrace();
            System.exit(1);
        }

        // global key listener implementation
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new NativeKeyAdapter() {
                @Override
                public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
                    super.nativeKeyPressed(nativeKeyEvent);
                    if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_ESCAPE) changeClickingState();
                }
            });
        } catch (NativeHookException e) {
            System.err.println("Failed to register native hook.");
            e.printStackTrace();
        }

        // set new number model to limit user input to acceptable numbers
        SpinnerModel numberModel = new SpinnerNumberModel(1000, 0, 10000, 1);
        delaySpinner.setModel(numberModel);

        // link the control button with thread runnable start/stop procedures
        controlButton.addActionListener(e -> changeClickingState());

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ClickingUI");
        frame.setMinimumSize(new Dimension(200, 50));
        frame.setResizable(false);
        frame.setContentPane(new ClickingUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}