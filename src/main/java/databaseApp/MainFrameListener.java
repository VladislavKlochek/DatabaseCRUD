package databaseApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class MainFrameListener implements ComponentListener {

    private int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private int absoluteX = 0;
    private int absoluteY = 0;
    private JWindow deleteWindow;

    @Override
    public void componentResized(ComponentEvent e) {
        width = e.getComponent().getWidth();
        height = e.getComponent().getHeight();
        absoluteX = (int) e.getComponent().getLocationOnScreen().getX();
        absoluteY = (int) e.getComponent().getLocationOnScreen().getY();

        deleteWindow.setLocation(absoluteX + width - 88, absoluteY + height - 115);
        if(ButtonDropUpListener.deleteWindow.getDeleteButton().atLeastOneRecordIsChosen){
            deleteWindow.setVisible(true);
        }

        if (((((MainFrame) e.getComponent()).getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) &&
                ButtonDropUpListener.deleteWindow.getDeleteButton().atLeastOneRecordIsChosen) {
            deleteWindow.setVisible(true);
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        absoluteX = (int) e.getComponent().getLocationOnScreen().getX();
        absoluteY = (int) e.getComponent().getLocationOnScreen().getY();
        deleteWindow.setLocation(absoluteX + e.getComponent().getWidth() - 88,
                absoluteY + e.getComponent().getHeight() - 115);
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

    public int getAbsoluteX() {
        return absoluteX;
    }

    public int getAbsoluteY() {
        return absoluteY;
    }

    public void setDeleteWindow(JWindow window){
        deleteWindow = window;
    }
}
