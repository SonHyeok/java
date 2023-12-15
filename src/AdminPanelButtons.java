import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class AdminPanelButtons extends JPanel {
    private String loggedInUsername = null;
    private Connection connection = null;
    JButton[] buttons;

    public AdminPanelButtons(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        setLayout(new GridLayout(5, 1)); // 그리드 레이아웃 설정
        addAdminLeftButton(); // AdminPanelButtons의 왼쪽에 버튼 추가하는 메소드 호출
    }

    // AdminPanelButtons에 버튼을 추가하는 메소드
    public void addAdminLeftButton() {
        // 5개의 버튼을 담을 배열 생성
        buttons = new JButton[5];

        // 첫 번째 버튼: 메인페이지
        buttons[0] = new JButton("메인페이지");
        buttons[0].setForeground(Color.WHITE);
        buttons[0].setBackground(Color.DARK_GRAY);
        buttons[0].setPreferredSize(new Dimension(150, 50));
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 메인페이지 버튼 클릭 시 호출되어 메인페이지로 이동
                openAdminPage();
            }
        });
        add(buttons[0]);

        // 첫 번째 버튼: 화원추가
        buttons[1] = new JButton("회원추가");
        buttons[1].setForeground(Color.WHITE);
        buttons[1].setBackground(Color.DARK_GRAY);
        buttons[1].setPreferredSize(new Dimension(150, 50));
        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 회원추가 버튼 클릭 시 호출되어 회원추가 페이지로 이동
                openAdminPageMemberAdd();
            }
        });
        add(buttons[1]);

        // 위 버튼들 내용과 중복되어 생략
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
        add(buttons[2]);

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
        add(buttons[3]);

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
        add(buttons[4]);
    }

    // 메인페이지로 이동하는 메소드
    private void openAdminPage() {
        //SwingUtilities.invokeLater(() -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            AdminPage adminPage = new AdminPage(loggedInUsername, connection);
            adminPage.setVisible(true);
        //};
        //)
    }

    // 회원추가 페이지로 이동하는 메소드
    private void openAdminPageMemberAdd() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.dispose();
        AdminMemberAdd adminmemberadd = new AdminMemberAdd(loggedInUsername, connection);
        adminmemberadd.setVisible(true);
    }

    // 포인트 적립 페이지로 이동하는 메소드
    private void openAdminPoint() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.dispose();
        AdminMemberPoint adminmemberpoint = new AdminMemberPoint(loggedInUsername, connection);
        adminmemberpoint.setVisible(true);
    }

    // PT 예약 설정 페이지로 이동하는 메소드
    private void openAdminPTSet() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.dispose();
        AdminPTSet adminptset = new AdminPTSet(loggedInUsername, connection);
        adminptset.setVisible(true);
    }

    // 장터 관리 페이지로 이동하는 메소드
    private void openAdminMarketBoard() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.dispose();
        AdminMarket adminmarket = new AdminMarket(loggedInUsername, connection);
        adminmarket.setVisible(true);
    }
}