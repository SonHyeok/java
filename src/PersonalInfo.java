import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonalInfo extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel;
    private JButton[] buttons;
    private JEditorPane infoArea;
    private String loggedInUsername;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    Connection connection;

    String MemberID;
    String MemberPW;

    PersonalInfo(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        initComponents();
        getPersonalInfo();
    }
    private void getPersonalInfo() {
        try {
            preparedStatement = connection.prepareStatement("select MemberEmail,MemberPassword from members where MemberEmail = ?");
            preparedStatement.setString(1,loggedInUsername);
            rs = preparedStatement.executeQuery();
            rs.next();
            MemberID = rs.getString("MemberEmail");
            MemberPW = rs.getString("MemberPassword");

            System.out.println("MemberID = " + MemberID);
            System.out.println("MemberPW= " + MemberPW);
        } catch (SQLException e) {

        }
    }

    private void setPersonalInfo(String MemberID, String MemberPW){
        try{
            preparedStatement = connection.prepareStatement("UPDATE members SET MemberEmail = ?, MemberPassword = ? where MemberEmail = ?");
            preparedStatement.setString(1,MemberID);
            preparedStatement.setString(2,MemberPW);
            preparedStatement.setString(3,loggedInUsername);

            preparedStatement.executeUpdate();

        }catch (SQLException e){

        }

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

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.BLACK);
        c.add(rightPanel, BorderLayout.CENTER);

        // JEditorPane으로 변경
        infoArea = new JEditorPane();
        infoArea.setVisible(false);
        infoArea.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(infoArea);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel newButtonPanel = new JPanel(new GridLayout(4, 1, 0, 10)); // 오른쪽 페이지 코드
        newButtonPanel.setBackground(Color.BLACK);

        c.add(rightPanel, BorderLayout.CENTER);
        rightPanel.add(newButtonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }
}

