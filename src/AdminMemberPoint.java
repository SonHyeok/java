
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminMemberPoint extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel;
    private JButton[] buttons;
    private String loggedInUsername;

    Connection connection;

    AdminMemberPoint(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        initComponents(); // GUI 컴포넌트 초기화 메소드 호출
        AddPoint(); // 포인트 추가 메소드 호출
    }

    // GUI 컴포넌트를 초기화하는 메소드 => AdminPanelButtons를 사용
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

    // 회원에게 포인트를 추가하는 메소드
    private void AddPoint() {
        String memberId = JOptionPane.showInputDialog("회원 아이디를 입력하세요:");

        if (memberId != null && !memberId.isEmpty()) {
            try {
                // 회원 조회
                String sql = "SELECT * FROM members WHERE MemberEmail=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, memberId);
                ResultSet resultSet = statement.executeQuery();
                int addPoint = 0;
                if (resultSet.next()) {
                    addPoint = Integer.parseInt(JOptionPane.showInputDialog("포인트를 입력하세요:"));
                }
                if (addPoint > 0) { // 포인트가 정상적으로 입력되면 회원이메일을 통해 유저에게 포인트 지급
                    int currentPoint = resultSet.getInt("MemberPoint");
                    int newPoint = currentPoint + addPoint;
                    String updateSql = "UPDATE members SET MemberPoint=? WHERE MemberEmail=?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                    updateStatement.setInt(1, newPoint);
                    updateStatement.setString(2, memberId);
                    updateStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "포인트가 성공적으로 적립되었습니다.");
                } else { // 회원이 존재하지 않거나 포인트 정상 입력 안할시 사용
                    JOptionPane.showMessageDialog(null, "입력한 아이디의 회원이 존재하지 않거나, 유효한 포인트를 입력하세요.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}