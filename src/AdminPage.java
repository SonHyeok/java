import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminPage extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel;
    private JButton[] buttons;
    private String loggedInUsername;
    private Connection connection;


    AdminPage(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        initComponents();
    }

    private void initComponents() {
        c = this.getContentPane();
        c.setLayout(new BorderLayout());
        c.setBackground(Color.BLACK);

        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.BLACK);
        c.add(leftPanel, BorderLayout.WEST);

        titleLabel = new JLabel("OO헬스장 관리자 페이지.", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.DARK_GRAY);
        leftPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 10));
        buttonPanel.setBackground(Color.BLACK);

        AdminPanelButtons ap = new AdminPanelButtons(loggedInUsername, connection);
        ap.addAdminLeftButtons(buttonPanel);
        leftPanel.add(buttonPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }


}