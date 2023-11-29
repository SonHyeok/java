import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

public class AdminPTSet extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel;
    private JButton[] buttons;
    private Connection connection;
    private String loggedInUsername;

    AdminPTSet(String loggedInUsername, Connection connection) {
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
        leftPanel.add(buttonPanel, BorderLayout.CENTER);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.BLACK); // 패널 배경색을 검은색으로 설정

        AdminCalender calenderPanel = new AdminCalender(loggedInUsername,connection);
        c.add(rightPanel, BorderLayout.CENTER); // 패널을 중앙에 배치
        rightPanel.add(calenderPanel.p_center,BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800); // 크기를 1000x800으로 키움
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }


}