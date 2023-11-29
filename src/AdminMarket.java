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

        AdminPanelButtons ap = new AdminPanelButtons(loggedInUsername, connection);
        ap.addAdminLeftButtons(buttonPanel);
        leftPanel.add(buttonPanel, BorderLayout.CENTER);

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
}