package statistics;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class StatPanel extends JPanel {
    private Map<Object, Double> data;
    private final Set<Object> checked = new HashSet<>();

    private final double maxScale;
    private final double xGap;
    private final MeasurementScale measurementScale;
    private final MeasurementListener measurementListener;
    private final ColumnMouseMotionListener columnMouseMotionListener;
    private final ColumnMouseListener columnMouseListener;
    public StatPanel(Double maxScale, double xGap, MeasurementListener measurementListener,
                     MeasurementScale measurementScale, ColumnMouseMotionListener columnMouseMotionListener,
                     ColumnMouseListener columnMouseListener) {
        this.maxScale = maxScale;
        this.xGap = xGap;
        this.measurementScale = measurementScale;
        this.measurementListener = measurementListener;
        this.columnMouseListener = columnMouseListener;
        this.columnMouseMotionListener = columnMouseMotionListener;
        /* add(measurementScale);*/
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(measurementScale != null) {
            measurementScale.paint(g);
        }
        statPaint();
    }

    public void statPaint(){
        removeAll();
        validate();
        int startPoint = (int) (measurementListener.getWidth()*0.05 + 2);
        int actualWidth = measurementListener.getWidth() - startPoint - 30;
        int widthStep = (int) ((actualWidth / data.size())*1.1);

        int endHeightPoint = measurementScale.getActualZero();
        Column standard = new Column(0,0,0, measurementScale.getActualZero(), maxScale,
                "", null, null);

        var f = maxScale;
        int cq = data.size()-1;
        int counter = 0;
        while(counter != data.size()) {
            Map.Entry<Object, Double> t = findNext(f, cq);
            f = t.getValue();
            Column c = new Column((widthStep*cq+startPoint),
                    endHeightPoint-(t.getValue()*standard.getHeight())/standard.getValue(), (int) (widthStep-widthStep*xGap),
                    (t.getValue()*standard.getHeight())/standard.getValue(), t.getValue(), t.getKey(),
                    columnMouseListener, columnMouseMotionListener);
            var c1 = new Color(100, (int) (60*(c.getValue() /standard.getValue())),200);//255,115,0
            c.setBackground(c1);
            c.setLocation(c.getX(), endHeightPoint - c.getHeight());
            c.setSize(c.getPreferredSize());
            cq--;
            add(c);
            counter++;
        }
        checked.clear();
    }

    public void setData(Map<Object, Double> data){
        this.data = data;
    }

    private Map.Entry<Object, Double> findNext(double i, int cq){
        if(cq == 0){
            return new AbstractMap.SimpleEntry<>(new Object(), 0.0);
        }
        var outString = data.keySet().stream().filter(s -> !checked.contains(s))
                .filter(s -> data.get(s) <= i).max((o1, o2) -> data.get(o1).compareTo(data.get(o2))).get();
        var outInt = data.get(data.keySet().stream().filter(s -> !checked.contains(s))
                .filter(s -> data.get(s) <= i).max((o1, o2) -> data.get(o1).compareTo(data.get(o2))).get());
        checked.add(outString);
        return new AbstractMap.SimpleEntry<>(outString, outInt);
    }
}