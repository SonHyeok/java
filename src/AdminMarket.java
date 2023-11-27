import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminMarket extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel;
    private JButton[] buttons;
    private Connection connection;
    private String loggedInUsername;
    private JEditorPane infoArea;

    AdminMarket(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        initComponents();
        DisplayMarketBoard();
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

        buttons = new JButton[5];

        buttons[0] = new JButton("메인페이지");
        buttons[0].setForeground(Color.WHITE);
        buttons[0].setBackground(Color.DARK_GRAY);
        buttons[0].setPreferredSize(new Dimension(150, 50));
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAdminPage();
            }
        });
        buttonPanel.add(buttons[0]);

        buttons[1] = new JButton("회원추가");
        buttons[1].setForeground(Color.WHITE);
        buttons[1].setBackground(Color.DARK_GRAY);
        buttons[1].setPreferredSize(new Dimension(150, 50));
        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAdminPageMemberAdd();
            }
        });
        buttonPanel.add(buttons[1]);

        buttons[2] = new JButton("포인트 적립");
        buttons[2].setForeground(Color.WHITE);
        buttons[2].setBackground(Color.DARK_GRAY);
        buttons[2].setPreferredSize(new Dimension(150, 50));
        buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAdminPoint();
            }
        });
        buttonPanel.add(buttons[2]);

        buttons[3] = new JButton("PT 예약 설정");
        buttons[3].setForeground(Color.WHITE);
        buttons[3].setBackground(Color.DARK_GRAY);
        buttons[3].setPreferredSize(new Dimension(150, 50));
        buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAdminPTSet();
            }
        });
        buttonPanel.add(buttons[3]);

        buttons[4] = new JButton("장터 관리");
        buttons[4].setForeground(Color.WHITE);
        buttons[4].setBackground(Color.DARK_GRAY);
        buttons[4].setPreferredSize(new Dimension(150, 50));
        buttons[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAdminMarketBoard();
            }
        });
        buttonPanel.add(buttons[4]);

        leftPanel.add(buttonPanel, BorderLayout.CENTER);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.BLACK);
        c.add(rightPanel, BorderLayout.CENTER);

        // JEditorPane으로 변경
        infoArea = new JEditorPane();
        infoArea.setContentType("text/html");
        infoArea.setEditable(false);
        infoArea.setBackground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(infoArea);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void DisplayMarketBoard() {
        try {
            String sql = "SELECT * FROM MarketBoard";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            String htmlText = "<html>";

            // "장터" 정보를 한 번만 추가
            htmlText += "<div style='text-align: center; color: white; font-size: 28px; font-family: \"맑은 고딕\", sans-serif;'>";
            htmlText += "<b>장터</b> " + "<br>";
            htmlText += "</div>";
            htmlText += "<hr style='border-top: 2px solid white;'>";  // 구분선

            while (resultSet.next()) {
                htmlText += "<div style='text-align: center; color: white; font-size: 28px; font-family: \"맑은 고딕\", sans-serif;'>";
                htmlText += "<b>게시글 ID :</b> " + resultSet.getInt("MarketTitleID") + "<br>";
                htmlText += "<b>제목 :</b> " + resultSet.getString("Title") + "<br>";
                htmlText += "<b>판매자:</b> " + resultSet.getString("SellerEmail") + "<br>";
                String buyerEmail = resultSet.getString("buyerEmail");
                if (buyerEmail != null) {
                    htmlText += "<b>구매자:</b> " + buyerEmail + "<br>";
                } else {
                    htmlText += "<b>구매자:</b> <br>";
                }
                htmlText += "</div>";
                htmlText += "<hr style='border-top: 2px solid white;'>";  // 구분선
            }

            htmlText += "</html>";
            infoArea.setText(htmlText);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openAdminPage() {
        AdminMarket adminPage = new AdminMarket(loggedInUsername, connection);
        adminPage.setVisible(true);
        dispose();
    }

    private void openAdminPageMemberAdd() {
        AdminMemberAdd adminmemberadd = new AdminMemberAdd(loggedInUsername, connection);
        adminmemberadd.setVisible(true);
        dispose();
    }

    private void openAdminPoint() {
        AdminMemberPoint adminmemberpoint = new AdminMemberPoint(loggedInUsername, connection);
        adminmemberpoint.setVisible(true);
        dispose();
    }

    private void openAdminPTSet() {
        AdminPTSet adminptset = new AdminPTSet(loggedInUsername, connection);
        adminptset.setVisible(true);
        dispose();
    }

    private void openAdminMarketBoard() {
        AdminMarket adminmarket = new AdminMarket(loggedInUsername, connection);
        adminmarket.setVisible(true);
        dispose();
    }
}