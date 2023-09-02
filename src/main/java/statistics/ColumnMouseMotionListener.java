package statistics;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class ColumnMouseMotionListener implements MouseMotionListener {
    private JWindow infoWindow;
    private final ColumnAllMouseListener columnAllMouseListener;
    public ColumnMouseMotionListener(ColumnAllMouseListener columnAllMouseListener) {
        this.columnAllMouseListener = columnAllMouseListener;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (infoWindow != null) {
            columnAllMouseListener.setInfoWindowCoordinates(infoWindow);
            infoWindow.repaint();
        }
    }

    public void setInfoWindow(JWindow infoWindow) {
        this.infoWindow = infoWindow;
    }
}
