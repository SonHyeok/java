
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
        initComponents(); // UI 초기화 메서드 호출
        MemberAdd(); // 회원 추가 메서드 호출
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

        AdminPanelButtons ap = new AdminPanelButtons(loggedInUsername, connection);
        leftPanel.add(ap);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }

    //회원 추가 메소드
    private void MemberAdd() {
        String email = JOptionPane.showInputDialog("회원 이메일을 입력하세요:");
        if (checkEmail(email)) { // 이미 존재하는 이메일인지 확인
            JOptionPane.showMessageDialog(null, "이미 존재하는 회원입니다.");
            return;
        } else {
            if (email == null || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "잘못된 값 혹은 취소로 회원추가를 종료합니다.");
                return;
            }
        }

        // 비밀번호 입력 받기
        String password = JOptionPane.showInputDialog("비밀번호를 입력하세요:");
        if (password == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "잘못된 값 혹은 취소로 회원추가를 종료합니다.");
            return;
        }

        // 이름 입력 받기
        String name = JOptionPane.showInputDialog("이름을 입력하세요:");
        if (name == null || name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "잘못된 값 혹은 취소로 회원추가를 종료합니다.");
            return;
        }

        // 나이 입력 받기
        String ageStr = JOptionPane.showInputDialog("나이를 입력하세요:");
        if (ageStr == null || ageStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "잘못된 값 혹은 취소로 회원추가를 종료합니다.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "잘못된 값 혹은 취소로 회원추가를 종료합니다.");
            return;
        }

        // 성별 입력 받기
        String sex = JOptionPane.showInputDialog("성별을 입력하세요 (남 or 여):");
        if (sex == null || sex.isEmpty()) {
            JOptionPane.showMessageDialog(null, "잘못된 값 혹은 취소로 회원추가를 종료합니다.");
            return;
        }

        // 전화번호 입력 받기
        String phone = JOptionPane.showInputDialog("전화번호를 입력하세요 (010-0000-0000):");
        if (phone == null || phone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "잘못된 값 혹은 취소로 회원추가를 종료합니다.");
            return;
        }

        // 생년월일 입력 받기
        String birthStr = JOptionPane.showInputDialog("생년월일을 입력하세요 (yyyy-mm-dd):");
        if (birthStr == null || birthStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "잘못된 값 혹은 취소로 회원추가를 종료합니다.");
            return;
        }

        Date birth;
        try {
            birth = Date.valueOf(birthStr);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "올바른 생년월일 형식으로 입력하세요.");
            return;
        }

        try {
            // SQL 쿼리를 사용하여 회원 추가
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

    // 이메일 중복 확인 메서드
    private boolean checkEmail(String email) {
        try {
            String sql = "SELECT * FROM members WHERE MemberEmail=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // 결과가 존재하면 true, 그렇지 않으면 false 반환
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}