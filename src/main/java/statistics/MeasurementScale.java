package statistics;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class MeasurementScale extends JComponent {
    int width;
    int height;
    int numberOfDashes;
    Double maxValue;
    int actualZero;
    MeasurementListener measurementListener;
    MeasurementScale(int width, int height,int numberOfDashes, MeasurementListener measurementListener){
        this.height = height;
        this.width = width;
        this.numberOfDashes = numberOfDashes+2;
        this.measurementListener = measurementListener;
    }

    public void setScale(Double maxValue){
        this.maxValue = maxValue;
    }

    public void paint(Graphics g){
        width = measurementListener.getWidth();
        height = measurementListener.getHeight();
        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(4));
        g2.setPaint(Color.BLACK);
        g2.drawLine((int) (width*0.05), 0, (int) (width*0.05), height);

        int dashStep = height / numberOfDashes;

            g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 2.0f, new float[]{5.0f}, 0.0f));

            double numberStep = (maxValue / numberOfDashes);
            int temp = 0;
            DecimalFormat formattedDouble = new DecimalFormat("#0.00");

            for (int i = numberOfDashes; i > 0; i--) {
                g2.setPaint(Color.BLACK);
                g2.drawLine((int) (width * 0.04), dashStep * i, width, dashStep * i);
                g2.setColor(new Color(155, 115, 255));
                g2.drawString(formattedDouble.format(numberStep * temp++), (int) (width * 0.007), (dashStep * i) + 6);
            }
        actualZero = dashStep*numberOfDashes;
    }

    public int getActualZero(){
        return actualZero;
    }

}
