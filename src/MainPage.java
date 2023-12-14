
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainPage extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel;
    private JEditorPane infoArea;
    private String loggedInUsername;
    JButton[] buttons =  new JButton[12];
    Connection connection;

    MainPage(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        initComponents();
        displayMemberInfo();
    }

    private void initComponents() {
        UserPanelButtons userPanelButtons = new UserPanelButtons(loggedInUsername,connection);
        c = this.getContentPane();
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(c);

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

        JPanel newButtonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        newButtonPanel.setBackground(Color.BLACK);

        userPanelButtons.addLeftButtons(buttonPanel, buttons,topFrame); // 패널에 버튼 추가
        userPanelButtons.addRightButtons(newButtonPanel, buttons,topFrame);

        leftPanel.add(buttonPanel, BorderLayout.CENTER); // 버튼 추가된 왼쪽 패널 add
        rightPanel.add(newButtonPanel, BorderLayout.SOUTH); // 버튼 추가된 오른쪽 패널 add

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
                String htmlText = "<html>";
                htmlText += "<div style='text-align: center; color: white; font-size: 28px; font-family: \"맑은 고딕\", sans-serif;'>";
                htmlText += "<b>회원님의 정보</b> " + "<br>" + "<br>";
                htmlText +=  resultSet.getString("MemberEmail") + "<b> 회원님 반갑습니다</b> " + "<br>" + "<br>";
                htmlText += "<b>회원님의 남은 포인트:</b> " + resultSet.getInt("MemberPoint") + "<br>";
                htmlText += "<b>회원님의 남은 기간:</b> " + resultSet.getInt("MemberDate") + "일" + "<br>";
                htmlText += "<b>PT횟수:</b> " + resultSet.getInt("MemberPT") + "<br>";
                htmlText += "</div>";
                htmlText += "</html>";

                // JEditorPane에 HTML 문자열 추가
                infoArea.setText(htmlText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}