import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MemberShipPT extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel;
    private JButton[] buttons;
    private JEditorPane infoArea;
    private String loggedInUsername;

    String url = "jdbc:mysql://localhost:3306/test";
    String userName = "root";
    String password = "0000";
    Connection connection;

    MemberShipPT(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        initComponents();
        displayMemberInfo();
    }

    private void initComponents() {
        UserPanelButtons userPanelButtons = new UserPanelButtons(loggedInUsername,connection);
        c = this.getContentPane();
        c.setLayout(new BorderLayout());
        c.setBackground(Color.BLACK);

        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.BLACK);
        c.add(leftPanel, BorderLayout.WEST);

        titleLabel = new JLabel("OO헬스장에 오신걸 환영합니다.", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.DARK_GRAY);
        leftPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 0, 10));
        buttonPanel.setBackground(Color.BLACK);

        userPanelButtons.addLeftButtons(buttonPanel); // 패널에 버튼 추가
        leftPanel.add(buttonPanel, BorderLayout.CENTER); // 버튼 추가된 왼쪽 패널 add


        leftPanel.add(buttonPanel, BorderLayout.CENTER);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.BLACK);
        c.add(rightPanel, BorderLayout.CENTER);

        infoArea = new JEditorPane();
        infoArea.setContentType("text/html");
        infoArea.setEditable(false);
        infoArea.setBackground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(infoArea);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel newButtonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        newButtonPanel.setBackground(Color.BLACK);
        userPanelButtons.addMemberShipPTButtons(newButtonPanel,infoArea);

        rightPanel.add(newButtonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void displayMemberInfo() {
        try {
            String sql = "SELECT * FROM Members WHERE MemberEmail=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, loggedInUsername);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // HTML 형식으로 문자열 구성
                String htmlText = "<html>";
                htmlText += "<div style='text-align: center; color: white; font-size: 28px; font-family: \"맑은 고딕\", sans-serif;'>";
                htmlText += "<b>PT 이용권 구매</b> " + "<br>" + "<br>";
                htmlText += "<b>회원님의 남은 포인트:</b> " + resultSet.getInt("MemberPoint") + "<br>";
                htmlText += "<b>회원님의 남은 기간:</b> " + resultSet.getInt("MemberDate") + "일" + "<br>";
                htmlText += "<b>PT횟수:</b> " + resultSet.getInt("MemberPT") + "<br>";
                htmlText += "</div>";
                htmlText += "</html>";

                infoArea.setText(htmlText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}