
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginDemo extends JFrame {
    private Container c;
    private JLabel userLabel, passLabel;
    private JTextField userTF;
    private JPasswordField passPF;
    private JButton loginButton, clearButton;
    private String loggedInUsername;
    private boolean isAdmin;
    private DBConnManager dbConnManager;
    Connection connection;

    LoginDemo() {
        dbConnManager = new DBConnManager();
        dbConnManager.createDbConnection(); // 연결 생성 추가
        connection = dbConnManager.getConnection(); // 연결 얻어오기 추가
        initComponents();
    }

    public void initComponents() {
        c = this.getContentPane();
        c.setLayout(null);

        userLabel = new JLabel("이메일 : ");
        userLabel.setBounds(50, 50, 100, 50);
        c.add(userLabel);

        userTF = new JTextField();
        userTF.setBounds(150, 50, 200, 50);
        c.add(userTF);

        passLabel = new JLabel("비밀번호 : ");
        passLabel.setBounds(50, 130, 100, 50);
        c.add(passLabel);

        passPF = new JPasswordField();
        passPF.setBounds(150, 130, 200, 50);
        c.add(passPF);

        loginButton = new JButton("로그인");
        loginButton.setBounds(150, 200, 100, 50);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userText;
                String pwdText;
                userText = userTF.getText();
                pwdText = passPF.getText();

                if (validateLogin(userText, pwdText)) {
                    JOptionPane.showMessageDialog(null, "로그인 성공");

                    if (isAdmin) {
                        openAdminPage();
                    } else {
                        openMainPage();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "회원번호 또는 비밀번호가 잘못되었습니다.");
                }
            }
        });
        c.add(loginButton);

        clearButton = new JButton("초기화");
        clearButton.setBounds(260, 200, 100, 50);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userTF.setText("");
                passPF.setText("");
            }
        });
        c.add(clearButton);
    }

    private boolean validateLogin(String userName, String password) {
        try {
            String sql = "SELECT * FROM Members WHERE MemberEmail=? AND MemberPassword=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userName);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                loggedInUsername = resultSet.getString("MemberEmail");
                isAdmin = resultSet.getBoolean("IsAdmin");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openMainPage() {
        MainPage mainPage = new MainPage(loggedInUsername, connection);
        mainPage.setVisible(true);
        dispose();
    }

    private void openAdminPage() {
        AdminPage adminPage = new AdminPage(loggedInUsername, connection);
        adminPage.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        LoginDemo frame = new LoginDemo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 420, 320);
        frame.setTitle("헬스장 출입 관리 시스템");
        frame.setResizable(false);
        frame.setVisible(true);
    }
}