package statistics;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BarChart extends JFrame {
    private final Map<Object, Double> dataMap;
    private StatPanel statPanel;
    private final MeasurementScale measurementScale;
    private final MeasurementListener measurementListener = new MeasurementListener();
    private final ColumnAllMouseListener columnAllMouseListener = new ColumnAllMouseListener();
    private final int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private final int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();

    public BarChart(Map<Object, Double> data){
        dataMap = data;
        measurementScale = new MeasurementScale((int) (width * 0.75), (int) (height * 0.75), 18, measurementListener);
        measurementScale.setScale(dataMap.values().stream().max(Double::compare).get());
        init();
        statPanel.setData(dataMap);
    }

    public void init(){
        setSize((int) (width * 0.75), (int) (height * 0.75));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Bar chart");
        ImageIcon icon = new ImageIcon("src/main/resources/BarChartIcon.png");
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);

        //Инициализация всех панелей
        statPanel = new StatPanel(dataMap.values().stream().max(Double::compare).get() ,0.01,
                measurementListener, measurementScale, getColumnAllMouseListener().getColumnMouseMotionListener(),
                getColumnAllMouseListener().getColumnMouseListener());
        statPanel.addComponentListener(measurementListener);
        statPanel.setPreferredSize(new Dimension((int) (width * 0.75), (int) (height * 0.7)));
        statPanel.setSize(new Dimension((int) (width * 0.75), (int) (height * 0.7)));
        statPanel.setBackground(new Color(50,50,50));
        statPanel.setVisible(true);

        add(statPanel);
        setVisible(true);
    }

    protected ColumnAllMouseListener getColumnAllMouseListener(){
        return columnAllMouseListener;
    }
}


