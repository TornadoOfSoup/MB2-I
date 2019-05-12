package com.soup;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.MemoryImageSource;

import javax.accessibility.Accessible;
import javax.swing.JFrame;
import javax.swing.Timer;

/*
 * 8/2/2012
 * Written by: Boris.  (Modified by TornadoOfSoup)
 *
 * This code causes a fake Blue Screen of Death to appear.  It will
 stay up for [x] seconds.  During that time, the mouse is rendered
 useless.
 * It can be ended by pressing CRTL and ATL and the same time.  (ALT* and no it can't)

 */

public class BSOD extends JFrame implements ActionListener, Accessible {

    /**
     *
     */
    private static final long serialVersionUID = -2013075867052538317L;

    private Timer t;
    private String message;
    private int notifTime;
    private boolean done = false;

    public BSOD() {
        this(3000);
    }

    public BSOD(int time) {
        this(time, "", 0);
    }

    public BSOD(int time, String message, int notifTime) {
        this.setUndecorated(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setAlwaysOnTop(true);

        this.message = message;
        this.notifTime = notifTime;

        Image img = createImage(new MemoryImageSource(1, 1, new int[2], 0, 0));
        Cursor cursor = this.getToolkit().createCustomCursor(img, new java.awt.Point(0,0),
                "Transparent");
        setCursor(cursor);

        this.setVisible(true);
        t = new Timer(time, this);
        t.start();
    }


    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (!done) {
            if (arg0.getSource() == t) {
                if (!message.equals("")) {
                    new NotificationThread("Sorry", message, notifTime).start();
                }
                this.dispose();
                done = true;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g);
        // Blue Back
        g.setColor(new Color(0, 0, 130));
        g.fillRect(0, 0, this.getToolkit().getScreenSize().width, this.getToolkit().getScreenSize().height);
        g.setColor(Color.WHITE);

        Font af = new Font("Courier New", Font.TRUETYPE_FONT, 32);

        int fontSize = af.getSize();

        g.setFont(af);
        g.drawString("A problem has been detected and Windows has been shut down to prevent damage",
                10, 20 + fontSize);

        g.drawString("to you computer.", 10, 2 * fontSize + 20);
        g.drawString("The problem seems to be caused by the following file:  SPCMDCON.SYS",
                10, 4 * fontSize + 20);
        g.drawString("PAGE_FAULT_IN_NONPAGED_AREA", 10, 6 * fontSize + 20);

        g.drawString("If this is the first time you've seen this Stop error screen,",
                10, 8 * fontSize + 20);
        g.drawString("press CTRL-ATL-DEL.  If this screen appears again, follow",
                10, 9 * fontSize + 20);

        g.drawString("these steps:", 10, 10 * fontSize + 20);

        g.drawString("Check to make sure any new hardware or software is properly installed.",
                10, 12 * fontSize + 20);
        g.drawString("If this is a new installation, ask your hardware or software manufacturer",
                10, 13 * fontSize + 20);
        g.drawString("for any windows updates you might need.", 10,
                14 * fontSize + 20);
        g.drawString("If problems continue, disable or remove any newly installed hardware",
                10, 16 * fontSize + 20);
        g.drawString("or software.  Disable BIOS memory options such as caching or shadowing.",
                10, 17 * fontSize + 20);
        g.drawString("If you need to use Safe Mode to remove or disable components, restart",
                10, 18 * fontSize + 20);
        g.drawString("your computer, press F8 to select Advanced Startup Options, and then ",
                10, 19 * fontSize + 20);
        g.drawString("select Safe Mode.", 10, 20 * fontSize + 20);
        g.drawString("Technical information:", 10, 22 * fontSize + 20);

        g.drawString("***  STOP: 0x00000050 (0xFD3094c2, 0x00000001, 0xFBE7617, 0x00000000)",
                10, 24 * fontSize + 20);
        g.drawString("***  SPCMDCON.SYS - Address FBFE7617 base at FBFE5000, DateStamp 3d6dd67c",
                10, 26 * fontSize + 20);
    }

}