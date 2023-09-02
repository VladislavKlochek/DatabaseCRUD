package databaseApp;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.StrokeBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame implements Runnable {
    private final JPanel informationPanel = new JPanel();
    private final JPanel actionPanel = new JPanel();
    private final JPanel additionalFunctionalityPanel = new JPanel();
    private final MainFrameListener mainFrameListener = new MainFrameListener();
    private final DatabaseConnection connection = new DatabaseConnection();
    private final List<QueryButton> queryButtons = new ArrayList<>(List.of(
            new QueryButton("Список лекарств", "SELECT * FROM listOfDrugs"),
            new QueryButton("Список заказов", "SELECT * FROM listOfOrders"),
            new QueryButton("Список рецепов", "SELECT * FROM recipe"),
            new QueryButton("Тип лекарства", "SELECT * FROM typeOfDrug"),
            new QueryButton("Список ингредиентов", "SELECT * FROM ingredient"),
            new QueryButton("Список лекарств на складе", "SELECT * FROM listOfDrugsInStorage")));

    private void initActionPanel() {
        JButton showButton = new JButton("Просмотреть таблицы");
        JButton addButton = new JButton("Добавить запись");
        JButton updateButton = new JButton("Обновить запись");
        JButton deleteButton = new JButton("Удалить запись");

        List<JButton> buttons = new ArrayList<>();
        buttons.add(showButton);
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);

        buttons.forEach(b->setButtonProp(b,0));

        int colorChange = queryButtons.size()-1;
        for (QueryButton queryButton : queryButtons) {
            setButtonProp(queryButton, colorChange);
            colorChange--;
        }

        showButton.addMouseListener(new ButtonDropUpListener(ChoiceWindowAction.READ, informationPanel, connection.getConnection(), mainFrameListener, this, queryButtons));
        addButton.addMouseListener(new ButtonDropUpListener(ChoiceWindowAction.CREATE, informationPanel, connection.getConnection(), mainFrameListener, this, queryButtons));
        updateButton.addMouseListener(new ButtonDropUpListener(ChoiceWindowAction.UPDATE, informationPanel, connection.getConnection(), mainFrameListener, this, queryButtons));
        deleteButton.addMouseListener(new ButtonDropUpListener(ChoiceWindowAction.DELETE, informationPanel, connection.getConnection(), mainFrameListener, this, queryButtons));

        actionPanel.setBackground(new Color(52, 38, 84, 255));
        actionPanel.setLayout(new GridLayout(1, 5));
        actionPanel.add(showButton);
        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

    }

    private void initAdditionalFunctionalityPanel() {
        int preferredWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.02) + 2;
        int preferredHeight = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.02)
                * (Toolkit.getDefaultToolkit().getScreenSize().getWidth() /
                Toolkit.getDefaultToolkit().getScreenSize().getHeight())) + 2;

        additionalFunctionalityPanel.setPreferredSize(new Dimension(preferredWidth, 0));
        additionalFunctionalityPanel.setBackground(new Color(71, 71, 73));
        ImageIcon barCharImage = new ImageIcon("D:\\ForPrograms\\JavaProject\\Statistics\\src\\main\\resources\\BarChartIcon.png");
        additionalFunctionalityPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JButton barChartButton = new JButton("", barCharImage);
        barChartButton.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        barChartButton.setBackground(new Color(117, 87, 194));
        barChartButton.setBorder(new StrokeBorder(new BasicStroke(2), new Color(50, 50, 50)));
        barChartButton.setFocusPainted(false);
        barChartButton.setToolTipText("Создать график");

        barChartButton.addActionListener(new SetupBarChart(this, connection.getConnection(), queryButtons));
        additionalFunctionalityPanel.add(barChartButton);
    }

    private void initInformationPanel() {
        informationPanel.setBorder(new LineBorder(Color.black, 0));
        informationPanel.setBackground(new Color(52, 47, 77, 236));
    }

    @Override
    public void run() {
        setSize(700, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);
        setTitle("Wow, this thing is certainly not worthless");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("src/main/resources/DatabaseIcon.png");
        setIconImage(icon.getImage());
        informationPanel.setSize((int) (this.getSize().width-additionalFunctionalityPanel.getPreferredSize().getWidth()),
                (int) (this.getSize().getHeight() - actionPanel.getPreferredSize().getHeight()));

        JScrollPane scrollPane = new JScrollPane(informationPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAlignmentX(Component.RIGHT_ALIGNMENT);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setAutoscrolls(true);
        scrollPane.setBackground(Color.LIGHT_GRAY);

        initActionPanel();
        initAdditionalFunctionalityPanel();
        initInformationPanel();

        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        add(additionalFunctionalityPanel, BorderLayout.EAST);

        addComponentListener(mainFrameListener);
        addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
                        // Получить новую ширину панели
            int newWidth = informationPanel.getWidth();

            // Пройтись по всем JLabel и обновить их размеры
            for (Component component : informationPanel.getComponents()) {
                if (component instanceof JPanel) {
                    JPanel innerPanel = (JPanel) component;
                    for (Component label : innerPanel.getComponents()) {
                        if (label instanceof JLabel) {
                            JLabel jLabel = (JLabel) label;

                            // Установить новую ширину для JLabel
                            int newLabelWidth = newWidth / innerPanel.getComponentCount();
                            jLabel.setSize(new Dimension(newLabelWidth, jLabel.getPreferredSize().height));
                        }
                    }
                }
            }

            // Обновить панель
            informationPanel.revalidate();
            informationPanel.repaint();

            repaint();
        }
    });
    }
    private void setButtonProp(JButton button, int i){
        button.setBackground(new Color(87 + i*10,  54+i*10, 107));
        button.setForeground(Color.WHITE);
        button.setUI(new CustomButtonUI());
        button.setFocusable(false);
    }
    
    static class CustomButtonUI extends BasicButtonUI {
        @Override
        protected void paintButtonPressed(Graphics g, AbstractButton b) {
            // Здесь вы можете рисовать собственный фон для нажатой кнопки
            g.setColor(new Color(b.getBackground().getRed() - 10, b.getBackground().getGreen() - 10, b.getBackground().getBlue())); // Например, устанавливаем красный цвет
            g.fillRect(0, 0, b.getWidth(), b.getHeight());
        }
    }
}

