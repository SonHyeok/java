
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminMemberAdd extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel;
    private JButton[] buttons;
    private String loggedInUsername;

    Connection connection;

    AdminMemberAdd(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        initComponents();
        MemberAdd();
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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void MemberAdd() {
        String email = JOptionPane.showInputDialog("회원 이메일을 입력하세요:");
        if (email != null && !email.isEmpty()) {
            if (checkEmail(email)) {
                JOptionPane.showMessageDialog(null, "이미 존재하는 회원입니다.");
                return;
            }

            String password = JOptionPane.showInputDialog("비밀번호를 입력하세요:");
            String name = JOptionPane.showInputDialog("이름을 입력하세요:");
            int age = Integer.parseInt(JOptionPane.showInputDialog("나이를 입력하세요:"));
            String sex = JOptionPane.showInputDialog("성별을 입력하세요:");
            String phone = JOptionPane.showInputDialog("전화번호를 입력하세요:");
            Date birth = Date.valueOf(JOptionPane.showInputDialog("생년월일을 입력하세요 (yyyy-mm-dd):"));

            try {
                String sql = "INSERT INTO members (MemberEmail, MemberPassword, MemberName, MemberAge, MemberSex, MemberPN, MemberBirth, IsAdmin) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, false)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, email);
                statement.setString(2, password);
                statement.setString(3, name);
                statement.setInt(4, age);
                statement.setString(5, sex);
                statement.setString(6, phone);
                statement.setDate(7, birth);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(null, "회원 추가가 완료되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(null, "회원 추가에 실패했습니다.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean checkEmail(String email) {
        try {
            String sql = "SELECT * FROM members WHERE MemberEmail=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}