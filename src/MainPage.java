
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainPage extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel;
    private JButton[] buttons;
    private JEditorPane infoArea;
    private String loggedInUsername;

    Connection connection;

    MainPage(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        initComponents();
        displayMemberInfo();
    }

    private void initComponents() {
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

        buttons = new JButton[12];

        buttons[0] = new JButton("메인페이지");
        buttons[0].setForeground(Color.WHITE);
        buttons[0].setBackground(Color.DARK_GRAY);
        buttons[0].setPreferredSize(new Dimension(150, 50));
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMainPage();
            }
        });
        buttonPanel.add(buttons[0]);

        buttons[1] = new JButton("입장");
        buttons[1].setForeground(Color.WHITE);
        buttons[1].setBackground(Color.DARK_GRAY);
        buttons[1].setPreferredSize(new Dimension(150, 50));
        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });
        buttonPanel.add(buttons[1]);

        buttons[2] = new JButton("퇴장");
        buttons[2].setForeground(Color.WHITE);
        buttons[2].setBackground(Color.DARK_GRAY);
        buttons[2].setPreferredSize(new Dimension(150, 50));
        buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });
        buttonPanel.add(buttons[2]);

        buttons[3] = new JButton("개인정보 수정");
        buttons[3].setForeground(Color.WHITE);
        buttons[3].setBackground(Color.DARK_GRAY);
        buttons[3].setPreferredSize(new Dimension(150, 50));
        buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PersonalInfo();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(buttons[3]);

        buttons[4] = new JButton("트레이너 평가");
        buttons[4].setForeground(Color.WHITE);
        buttons[4].setBackground(Color.DARK_GRAY);
        buttons[4].setPreferredSize(new Dimension(150, 50));
        buttons[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });
        buttonPanel.add(buttons[4]);

        buttons[5] = new JButton("게시판");
        buttons[5].setForeground(Color.WHITE);
        buttons[5].setBackground(Color.DARK_GRAY);
        buttons[5].setPreferredSize(new Dimension(150, 50));
        buttons[5].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });
        buttonPanel.add(buttons[5]);

        buttons[6] = new JButton("운동메이트");
        buttons[6].setForeground(Color.WHITE);
        buttons[6].setBackground(Color.DARK_GRAY);
        buttons[6].setPreferredSize(new Dimension(150, 50));
        buttons[6].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });
        buttonPanel.add(buttons[6]);

        buttons[7] = new JButton("장터");
        buttons[7].setForeground(Color.WHITE);
        buttons[7].setBackground(Color.DARK_GRAY);
        buttons[7].setPreferredSize(new Dimension(150, 50));
        buttons[7].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });
        buttonPanel.add(buttons[7]);

        leftPanel.add(buttonPanel, BorderLayout.CENTER);

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

        buttons[8] = new JButton("PT구매");
        buttons[8].setForeground(Color.WHITE);
        buttons[8].setBackground(Color.DARK_GRAY);
        buttons[8].setPreferredSize(new Dimension(150, 50));
        buttons[8].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenMemberShipPT();
            }
        });
        newButtonPanel.add(buttons[8]);

        buttons[9] = new JButton("회원권 구매");
        buttons[9].setForeground(Color.WHITE);
        buttons[9].setBackground(Color.DARK_GRAY);
        buttons[9].setPreferredSize(new Dimension(150, 50));
        buttons[9].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenMemberShip();
            }
        });
        newButtonPanel.add(buttons[9]);

        buttons[10] = new JButton("PT 예약하기");
        buttons[10].setForeground(Color.WHITE);
        buttons[10].setBackground(Color.DARK_GRAY);
        buttons[10].setPreferredSize(new Dimension(150, 50));
        buttons[10].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TrainerRate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        newButtonPanel.add(buttons[10]);

        buttons[11] = new JButton("운동일지 작성하기");
        buttons[11].setForeground(Color.WHITE);
        buttons[11].setBackground(Color.DARK_GRAY);
        buttons[11].setPreferredSize(new Dimension(150, 50));
        buttons[11].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    OpenLogCalender();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
        );
        newButtonPanel.add(buttons[11]);

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

    private void openMainPage() {
        MainPage mainPage = new MainPage(loggedInUsername, connection);
        mainPage.setVisible(true);
        dispose();
    }

    private void OpenMemberShip() {
        MemberShip membership = new MemberShip(loggedInUsername, connection);
        membership.setVisible(true);
        dispose();
    }

    private void OpenMemberShipPT() {
        MemberShipPT membershippt = new MemberShipPT(loggedInUsername, connection);
        membershippt.setVisible(true);
        dispose();
    }
    private void OpenLogCalender() throws SQLException {
        logCalender logCalender = new logCalender(loggedInUsername, connection);
        logCalender.setVisible(true);
        dispose();
    }

    private void TrainerRate() throws SQLException {
        TrainerRate trainerRate= new TrainerRate(loggedInUsername, connection);
        trainerRate.setVisible(true);
        dispose();
    }

    private void PersonalInfo() throws SQLException {
        PersonalInfo personalInfo= new PersonalInfo(loggedInUsername, connection);
        personalInfo.setVisible(true);
        dispose();
    }
}