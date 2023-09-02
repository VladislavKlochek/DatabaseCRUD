package statistics;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
public class MeasurementListener implements ComponentListener {

    int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()* 0.7);
    int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()* 0.7);

    @Override
    public void componentResized(ComponentEvent e) {
        height = e.getComponent().getHeight();
        width = e.getComponent().getWidth();
        e.getComponent().repaint();

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
