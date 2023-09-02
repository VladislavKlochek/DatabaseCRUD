package statistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ColumnMouseListener extends MouseAdapter implements ActionListener{
    private MouseEvent mouseEvent;
    private boolean isIncreasing = true;
    private Color columnColor;
    private int currentStep = 0;
    private final ColumnAllMouseListener columnAllMouseListener;
    private final Timer timerWait = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(mouseInside){
                gradientTimer.start();
                infoWindow = new InfoWindow(mouseEvent.getComponent());
                columnAllMouseListener.setInfoWindowCoordinates(infoWindow);
                columnAllMouseListener.setInfoWindow(infoWindow);
            }
        }
    });
    {
        timerWait.setRepeats(false);
    }
    private final Timer gradientTimer = new Timer(0, this);
    private boolean mouseInside;
    private JWindow infoWindow;
    public ColumnMouseListener(ColumnAllMouseListener columnAllMouseListener) {
        this.columnAllMouseListener = columnAllMouseListener;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(!mouseInside) {
            mouseInside = true;
            mouseEvent = e;
            columnColor = mouseEvent.getComponent().getBackground();
            timerWait.start();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseInside = false;
        mouseEvent.getComponent().setBackground(columnColor);
        timerWait.stop();
        gradientTimer.stop();
        currentStep = 0;
        isIncreasing = true;
        if(infoWindow != null) {
            infoWindow.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        float ratio;
        Color newColor = null;
        int STEPS = 100;
        if(isIncreasing && currentStep < STEPS){
            currentStep++;
            ratio = (float) currentStep / STEPS;
            newColor = interpolationColor(columnColor, Color.WHITE, ratio);
        } else if (currentStep <= STEPS) {
            isIncreasing = false;
            currentStep--;
            ratio = (float) (100-currentStep) / STEPS;
            newColor = interpolationColor(Color.WHITE, columnColor, ratio);
            if(currentStep == 0){isIncreasing = true;}
        }
        mouseEvent.getComponent().setBackground(newColor);
    }
    private Color interpolationColor(Color color1, Color color2, float ratio) {
        int red = (int) (color1.getRed() + ratio * (color2.getRed() - color1.getRed()));
        int green = (int) (color1.getGreen() + ratio * (color2.getGreen() - color1.getGreen()));
        int blue = (int) (color1.getBlue() + ratio * (color2.getBlue() - color1.getBlue()));
        return new Color(red, green, blue);
    }
}
