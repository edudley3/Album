import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.File;

/**
 * Created by elizabethdudley on 10/27/16.
 */
public class LightTable extends JComponent implements MouseMotionListener, MouseListener{
    private ViewMode mode;
    private boolean magnet;
    private String magnetLabel = "";
    private PhotoComponent currPhoto;
    private ArrayList<PhotoComponent> pcs = new ArrayList<>();
    private int prevx = 0;
    private int prevy = 0;
    private ArrayList<JLabel> magnets = new ArrayList<>();
//    private JPanel photoPanel = new JPanel();

    public LightTable() {
        mode = ViewMode.PHOTO;
        this.setLayout(new BorderLayout());
        magnet = false;
        addMouseMotionListener(this);
        setBackground(new Color(200, 200, 200));
        addMouseListener(this);
//        photoPanel.setLayout(new BorderLayout());
//        photoPanel.setPreferredSize(new Dimension(800, 800));
//        photoPanel.setOpaque(true);
//
//        photoPanel.repaint();
//        this.add(photoPanel, BorderLayout.CENTER);
//        photoPanel.repaint();
//        revalidate();
//        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        if (magnet & !magnetLabel.equals("")) {
            g.setColor(Color.BLACK);
            g.fillRect(prevx - 5, prevy - 5, 10, 10);
            g.setColor(getBackground());
        }
    }

    public void addPhoto(File fn) {
        PhotoComponent temp = new PhotoComponent(fn);
        temp.setLightTable(this);
        pcs.add(temp);
        setCurrPhoto(temp);

    }

    public PhotoComponent getCurrPhoto() {
        return currPhoto;
    }

    public void setCurrPhoto(PhotoComponent photoComponent) {
        if (currPhoto != null) {
            currPhoto.getThumbnail().setSelected(false);
        }
        currPhoto = photoComponent;
        currPhoto.getThumbnail().setSelected(true);
        revalidate();
        repaint();
    }

    public ArrayList<PhotoComponent> getPcs() {return pcs;}

    public ViewMode getMode() {
        return mode;
    }

    public void setMode(ViewMode vm) {
        mode = vm;
    }

    public void setMagnet(boolean t) {
        magnet = t;
    }

    public boolean isMagnet() {
        return magnet;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (magnet) {
            prevx = e.getX();
            prevy = e.getY();
        }
        repaint();
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (magnet) {
            prevx = e.getX();
            prevy = e.getY();
        }
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public String getMagnetLabel() {
        return magnetLabel;
    }

    public void setMagnetLabel(String label) {
        magnetLabel = label;
    }

    public void addMagnet(JLabel label) {
        magnets.add(label);
        label.addMouseListener(new MouseAdapter() {
            private boolean selected = false;
            private Point prev;
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                selected = true;
                System.out.println("pressed " + selected);
                prev = e.getPoint();
                label.getParent().revalidate();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                selected = false;
                label.setLocation(label.getX() + (e.getX() - prev.x), label.getY() + (e.getY() - prev.y));
                ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), Album.timer.getActionCommand());
                Album.timer.getActionListeners()[0].actionPerformed(ae);
                label.getParent().revalidate();

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                label.setLocation(label.getX() + (e.getX() - prev.x), label.getY() + (e.getY() - prev.y));
                ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), Album.timer.getActionCommand());
                Album.timer.getActionListeners()[0].actionPerformed(ae);
                label.getParent().revalidate();
            }
        });
    }

    public void removeAllMagnets() {
        magnets.clear();
    }

    public ArrayList<ThumbnailComponent> taggedThumbnails(Tags tag) {
        ArrayList<ThumbnailComponent> tagged = new ArrayList<>();
        for (PhotoComponent pc : pcs) {
            if (pc.getTags().contains(tag)) {
                tagged.add(pc.getThumbnail());
            }
        }
        return tagged;
    }

    public ArrayList<JLabel> getMagnets() {
        return magnets;
    }

    public JLabel getMagnet(Tags tag) {
        for (JLabel magnet: magnets) {
            if (tag.equals(Tags.FAMILY)) {
                if (magnet.getText().equals("Family")) {
                    return magnet;
                }
            } else if (tag.equals(Tags.WORK)) {
                if (magnet.getText().equals("Work")) {
                    return magnet;
                }
            } else if (tag.equals(Tags.VACATION)) {
                if (magnet.getText().equals("Vacation")) {
                    return magnet;
                }
            } else if (tag.equals(Tags.SCHOOL)) {
                if (magnet.getText().equals("School")) {
                    return magnet;
                }
            }
        }
        return null;
    }
}
