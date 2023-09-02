package databaseApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;

public class ButtonDropUpListener extends MouseAdapter {
    private JWindow window;
    private final ChoiceWindowAction action;
    private final JPanel panel;
    private final Connection connection;
    private final MainFrameListener mainFrameListener;
    private final JFrame mainFrame;
    public static DeleteJWindow deleteWindow;
    private final List<QueryButton> queryButtons;

    public ButtonDropUpListener(ChoiceWindowAction action, JPanel panel, Connection connection, MainFrameListener mainFrameListener, JFrame mainFrame, List<QueryButton> queryButtons) {
        this.action = action;
        this.panel = panel;
        this.connection = connection;
        this.mainFrameListener = mainFrameListener;
        this.mainFrame = mainFrame;
        this.queryButtons = queryButtons;
        initDeleteButton();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        window = new ChoiceWindow(e.getComponent(), action, panel, connection, mainFrame, queryButtons);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        new Thread(() -> {
            boolean run = true;
            while (run) {
                Point mouseLocation = MouseInfo.getPointerInfo().getLocation();

                int mouseX = (int) mouseLocation.getX();
                int mouseY = (int) mouseLocation.getY();

                if (!(window.getX() + window.getWidth() > mouseX &&
                        window.getX() <= mouseX &&
                        window.getY() + window.getHeight() > mouseY &&
                        window.getY() <= mouseY)) {
                    window.dispose();
                    run = false;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }

    private void initDeleteButton(){
        deleteWindow = new DeleteJWindow(connection, panel);
        deleteWindow.setSize(new Dimension(50, 80));
        deleteWindow.setLayout(new GridLayout(1, 0));
        DeleteButton button = new DeleteButton();
        button.setBackground(new Color(162, 57, 26));
        button.setIcon(new ImageIcon("src/main/resources/TrashCanIconWithoutWhiteBackground.png"));
        deleteWindow.setDeleteButton(button);
        deleteWindow.add(button);
        deleteWindow.setLocation(mainFrameListener.getAbsoluteX() + mainFrameListener.getWidth() - 88,
                mainFrameListener.getAbsoluteY() + mainFrameListener.getHeight() - 115);
        mainFrameListener.setDeleteWindow(ButtonDropUpListener.deleteWindow);
        deleteWindow.setVisible(false);
    }
}
