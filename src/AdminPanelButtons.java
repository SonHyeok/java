import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class AdminPanelButtons extends JFrame {
    private String loggedInUsername = null;
    private Connection connection = null;
    JButton[] buttons;

    AdminPanelButtons(String loggedInUsername, Connection connection){
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
    }

    // 어드민 메인 페이지 왼쪽 패널 버튼 추가 메소드
    public void addAdminLeftButtons(JPanel buttonPanel){
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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }

    // 어드민 페이지 리다이렉트 함수
    private void openAdminPage() {
        AdminPage adminPage = new AdminPage(loggedInUsername, connection);
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