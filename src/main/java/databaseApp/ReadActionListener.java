package databaseApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.List;

class ReadActionListener implements ActionListener {
    private final JPanel panel;
    private final Connection connection;
    private final List<JLabel> labelsToAdd;
    private final List<List<JLabel>> allLabels;
    private final List<JPanel> allPanels;
    private final List<String> typeOfData = new ArrayList<>();
    private final Map<String, String> map = new LinkedHashMap<>();
    private String tableName;
    public ReadActionListener(JPanel panel, Connection connection, List<JLabel> labelsToAdd,
                              List<List<JLabel>> allLabels, List<JPanel> allPanels) {
        this.panel = panel;
        this.connection = connection;
        this.labelsToAdd = labelsToAdd;
        this.allLabels = allLabels;
        this.allPanels = allPanels;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            allPanels.clear();
            panel.removeAll();
            if(ButtonDropUpListener.deleteWindow.isShowing()){
                ButtonDropUpListener.deleteWindow.check();
            }

            ResultSet resultSet;
            //Кол-во строк в таблице
            Statement statement = connection.createStatement();
            resultSet =
                    statement.executeQuery("SELECT COUNT(*) AS row_count FROM "
                            + ((QueryButton) e.getSource()).getSQLQuery().split(" ")
                            [((QueryButton) e.getSource()).getSQLQuery().split(" ").length - 1]);
            resultSet.next();
            int rowCount = resultSet.getInt("row_count");

            //Для отдельной кнопки
            statement = connection.createStatement();
            resultSet = statement.executeQuery(((QueryButton) e.getSource()).getSQLQuery());

            //Определяем тип данных
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                map.put( resultSet.getMetaData().getColumnName(i) , resultSet.getMetaData().getColumnClassName(i));
                typeOfData.add(String.valueOf(resultSet.getMetaData().getColumnClassName(i)));
            }

            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            //Для возможности удаления;
            int columnOfID = 0;

            //Заполнение названий колонок
            for (int i = 1; i < resultSet.getMetaData().getColumnCount() + 1; i++) {
                labelsToAdd.add(adjustInfoView(resultSet.getMetaData().getColumnName(i)));
                Font font = new Font("Bahnschrift SemiLight Condensed", Font.BOLD, 20);

                labelsToAdd.forEach(jLabel -> {jLabel.setForeground(Color.WHITE);
                jLabel.setFont(font);});

                if(resultSet.getMetaData().getColumnName(i).toLowerCase(Locale.ROOT).equals("id")){
                    columnOfID = i;
                }
            }
            labelsToAdd.forEach(e1 -> e1.setHorizontalAlignment(JLabel.CENTER));
            labelsToAdd.forEach(e2 -> e2.setHorizontalTextPosition(JLabel.CENTER));
            var o = new JPanel(new GridLayout(1, labelsToAdd.size()));
            o.setBackground(new Color(71, 71, 73));
            o.setForeground(Color.WHITE);
            labelsToAdd.forEach(o::add);
            panel.add(o);
            labelsToAdd.clear();

            tableName = resultSet.getMetaData().getTableName(1);


            //Заполнение данными таблиц
            resultSet.next();
            for (int j = 0; j < rowCount; j++) {
                int currentId = 0;
                for (int i = 1; i < resultSet.getMetaData().getColumnCount()+1; i++) {
                    String typeToOutput;
                    switch (typeOfData.get(i-1)) {
                        case "java.lang.Integer":
                            typeToOutput = String.valueOf(resultSet.getInt(i));
                            if(columnOfID == i){
                                currentId = resultSet.getInt(i);
                            }
                            break;
                        case "java.lang.String":
                            typeToOutput = String.valueOf(resultSet.getString(i));
                            break;
                        case "org.postgresql.util.PGmoney":
                            typeToOutput = String.valueOf(resultSet.getDouble(i));
                            break;
                        case "java.lang.Boolean":
                            typeToOutput = String.valueOf(resultSet.getBoolean(i));
                            break;
                        case "java.sql.Timestamp":
                            typeToOutput = String.valueOf(resultSet.getTimestamp(i)).split("00:00:00\\.0")[0];
                            /*typeToOutput = String.valueOf(resultSet.getTimestamp(i + 1));*/
                            break;
                        default:
                            typeToOutput = "N/A";
                    }
                    var gh = adjustInfoView(typeToOutput);
                    labelsToAdd.add(gh);
                }

                Color currColor = ((JButton)e.getSource()).getBackground();
                var r = new Random().nextInt(20);
                Color color = new Color(currColor.getRed(), currColor.getGreen(), currColor.getBlue() + r);

                var h = new DeletableJPanel(currentId, new GridLayout(1, labelsToAdd.size()), color, tableName);
                labelsToAdd.forEach(e1 -> e1.setHorizontalAlignment(JLabel.CENTER));
                labelsToAdd.forEach(e2 -> e2.setHorizontalTextPosition(JLabel.CENTER));
                labelsToAdd.forEach(h::add);
                panel.add(h);

                //Добавление всех панелй для возможности удаления
                allPanels.add(h);

                allLabels.add(new ArrayList<>(labelsToAdd));


                labelsToAdd.clear();
                resultSet.next();
            }
            allLabels.clear();
            panel.revalidate();
            panel.repaint();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public String getTableName(){return tableName;}
    private JLabel adjustInfoView(String info){
        JLabel label = new JLabel("<html>" + info.split(" ")[0]);
        for (int i = 1; i < info.split(" ").length; i++) {
            label.setText(label.getText() + "<br>" + info.split(" ")[i]);
        }
        label.setText(label.getText() + "</html>");
        label.setFont(new Font("Comic Sans", Font.BOLD,20));
        label.setForeground(Color.black);
        label.setPreferredSize(new Dimension(label.getFontMetrics(label.getFont()).stringWidth(info),
                label.getFontMetrics(label.getFont()).getHeight() * info.split(" ").length));
        label.setMaximumSize(new Dimension(new Dimension(label.getFontMetrics(label.getFont()).stringWidth(info),
                label.getFontMetrics(label.getFont()).getHeight())));
        return label;
    }

    public Map<String, String> getDataTypes(){
        return map;
    }
    public List<JPanel> getAllPanels(){
        return allPanels;
    }
}