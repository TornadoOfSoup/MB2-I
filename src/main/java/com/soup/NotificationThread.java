package com.soup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NotificationThread extends JFrame implements Runnable {

    private Thread thread;

    private String message, header;
    private long initTime;
    long timeLength = 100;
    JLabel timerLabel = new JLabel("time");
    JProgressBar progressBar;
    static int aliveCount = 0;

    public NotificationThread (String header, String message, long timeLength) {
        aliveCount++;
        setSize(300, 125);
        setLayout(new GridBagLayout());
        setUndecorated(true);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle screenSize = defaultScreen.getDefaultConfiguration().getBounds();
        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        int taskBarHeight = screenSize.height - winSize.height;
        int x = (int) screenSize.getMaxX() - getWidth();
        int y = (int) screenSize.getMaxY() - getHeight() - taskBarHeight;

        setLocation(x, y - ((getHeight() + 5) * (aliveCount - 1)));

        this.message = message;
        this.header = header;
        this.timeLength = timeLength;

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;

        JLabel headingLabel = new JLabel(header);
        headingLabel.setOpaque(false);
        add(headingLabel, constraints);

        constraints.gridx++;
        constraints.weightx = 0f;
        constraints.weighty = 0f;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.FIRST_LINE_END;

        JButton closeButton = new JButton(new AbstractAction("x") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });

        closeButton.setMargin(new Insets(1, 4, 1, 4));
        closeButton.setFocusable(false);
        add(closeButton, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;

        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        add(messageLabel, constraints);

        constraints.gridy++;

        //timerLabel.setForeground(new Color(120, 120, 120));
        //add(timerLabel, constraints);

        progressBar = new JProgressBar(0, (int)timeLength);
        add(progressBar, constraints);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }


    @Override
    public void run() {
        setVisible(true);
        setAlwaysOnTop(true);

        initTime = System.currentTimeMillis();
        progressBar.setStringPainted(true);
        while (initTime + timeLength - System.currentTimeMillis() > 0) {
            progressBar.setValue((int)(initTime + timeLength - System.currentTimeMillis()));

            progressBar.setString((double)(initTime + timeLength - System.currentTimeMillis()) / 1000 + "s");
        }

        long animInitTime = System.currentTimeMillis();
        long animLength = 300;

        /*
        while (animInitTime + animLength - System.currentTimeMillis() > 0) {

            float alpha = (float)(animInitTime + animLength - System.currentTimeMillis()) / animLength;
            setBackground(new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue(), (int) (alpha * 255)));
        }
        */

        int initX = getLocation().x;
        int initY = getLocation().y;

        while (animInitTime + animLength - System.currentTimeMillis() > 0) {
            int offset = (int) (((float)(animInitTime + animLength - System.currentTimeMillis()) / animLength) * getWidth());
            offset = getWidth() - offset;
            setLocation(initX + offset, initY);
        }

        dispose();
        aliveCount--;
    }

    public void start() {
        System.out.println("Starting " + this.getClass().getName() + "!");
        if (thread == null) {
            thread = new Thread(this, "NotificationThread");
            thread.start();
        }
    }
}
