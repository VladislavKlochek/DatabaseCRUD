package statistics;

import javax.swing.*;
import java.awt.*;

public class ColumnAllMouseListener {
    private final ColumnMouseListener columnMouseListener;
    private final ColumnMouseMotionListener columnMouseMotionListener;

    public ColumnAllMouseListener() {
        columnMouseListener = new ColumnMouseListener(this);
        columnMouseMotionListener = new ColumnMouseMotionListener(this);
    }

    public ColumnMouseListener getColumnMouseListener() {
        return columnMouseListener;
    }

    public ColumnMouseMotionListener getColumnMouseMotionListener() {
        return columnMouseMotionListener;
    }

    public void setInfoWindow(JWindow infoWindow) {
        columnMouseMotionListener.setInfoWindow(infoWindow);
    }

    public void setInfoWindowCoordinates(JWindow infoWindow){

        int startX = MouseInfo.getPointerInfo().getLocation().getX() >
                Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.88 ?
                (int) (MouseInfo.getPointerInfo().getLocation().getX() - infoWindow.getWidth() - 10) :
                (int) (MouseInfo.getPointerInfo().getLocation().getX() + 15);

        int startY = MouseInfo.getPointerInfo().getLocation().getY() >
                Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.88 ?
                (int) (MouseInfo.getPointerInfo().getLocation().getY() - infoWindow.getHeight() - 5) :
                (int) (MouseInfo.getPointerInfo().getLocation().getY() + 15);

        infoWindow.setLocation(startX, startY);
    }
}
