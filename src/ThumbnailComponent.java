import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by elizabethdudley on 10/27/16.
 */
public class ThumbnailComponent extends JComponent {
    private PhotoComponent pc;
    //static Dimension preferred;
    static Dimension max = new Dimension(300,300);
    //private Dimension min;
    private boolean selected;
    private int x;
    private int y;
    private Point destination;



    public ThumbnailComponent(PhotoComponent pc) {
        this.pc = pc;
        x = 0;
        y = 0;
        this.setMinimumSize(new Dimension((pc.getImage().getWidth(null)/10)*4, (pc.getImage().getHeight(null)/10)*4));
        this.setPreferredSize(new Dimension((pc.getImage().getWidth(null)/10)*4, (pc.getImage().getHeight(null)/10)*4));
//        this.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                setSelected(true);
//                pc.getLightTable().setCurrPhoto(pc);
//                repaint();
//            }
//        });
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(0.4, 0.4);
        g2d.drawImage(pc.getImage(), 0, 0, null);
        if (selected) {
            g2d.setColor(Color.YELLOW);
            float thickness = 10;
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawRect(5, 5, (pc.getImage().getWidth(null)) - 5, pc.getImage().getHeight(null)- 5);
            g2d.setStroke(oldStroke);
        } else {
            g2d.setColor(Color.WHITE);
            float thickness = 10;
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawRect(5, 5, (pc.getImage().getWidth(null)) - 5, pc.getImage().getHeight(null) - 5);
            g2d.setStroke(oldStroke);
//            System.out.println("x: " + x + ", y: " + y);
        }
    }

    public void setX(int n) {
        x=n;
    }

    public void setY(int n) {
        y = n;
    }

    public void setSelected(boolean n) {
        selected = n;
        repaint();
    }

    public boolean isSelected() {return selected;}

    public void setDestination(Point p) {
        destination = p;
    }

    public Point getDestination() {
        return destination;
    }

    public PhotoComponent getPc() {
        return pc;
    }

}
