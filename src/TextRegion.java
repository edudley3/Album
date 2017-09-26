import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by elizabethdudley on 10/8/16.
 */

public class TextRegion extends JComponent implements KeyListener {
    private PhotoComponent pc;
    //private Image img;
    private Graphics g2;
    private String chars;
    private int x;
    private int a,b;
    private int y;
    private int cursor;
    private int width;
    private int height;
    private boolean selected =false;
    private String font;

    public TextRegion(int x, int y, int width, int height, PhotoComponent p) {
        setPreferredSize(new Dimension(width, height));
        this.width = width;
        this.height = height;
        setSize(new Dimension(width, height));
        setDoubleBuffered(true);
        //System.out.println(getWidth());
        pc = p;
        addKeyListener(this);
        setFocusable(true);
        //requestFocusInWindow();
        setName("TextRegion");
        this.x = x;
        a = x+10;
        this.y = y;
        b = y+20;
        font = "Apple Gothic";
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//        System.out.println("TextBox Paint Component");
        g.setColor(Color.pink);
        if (selected) {
            g.setColor(new Color(244,241, 66));
//            g.drawRect(this.x - 5, this.y -5, width +10, height +10);
        }
        g.fillRect(x, y, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        Font font1 = new Font(font, Font.PLAIN, 10);
        g.setFont(font1);
        if (chars != null) {
            FontMetrics fontInfo = g.getFontMetrics();
            a = x + 5;
            b = y+ fontInfo.getHeight();
            String[] words = chars.split(" ");
            for (String s : words) {
                    if ((a + fontInfo.stringWidth(s+" ")) > (x + width)) {
                        a = x + 5;
                        b += fontInfo.getHeight();
                    }

                    if (b > y + height) {
                        setSize(new Dimension(width, height + fontInfo.getHeight() + 2));
                        height = getHeight();
                    }
                    g.drawString(s, a, b);
                    a += fontInfo.stringWidth(s + " ");
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (chars == null) {
            chars = new String();
            cursor = -1;
        }
        if (e.getKeyChar() != KeyEvent.VK_BACK_SPACE) {
            chars = chars + e.getKeyChar();
            cursor++;
        }
        //System.out.println("I'm being called!");
        pc.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && (cursor > 0)) {
            chars = chars.substring(0, cursor -1);
            cursor--;
            System.out.println("I'm being Called!");
        }
        pc.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void setSelected(boolean b) {
        selected = b;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setFont(String f) {
        font =f;
        repaint();
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }
}
