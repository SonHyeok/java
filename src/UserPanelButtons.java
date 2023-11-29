import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPanelButtons extends JFrame {

    private String loggedInUsername = null;
    private Connection connection = null;
    JButton[] buttons;

    public UserPanelButtons(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
    }


    // 메인 페이지 왼쪽 버튼 추가 메소드
    public void addLeftButtons(JPanel buttonPanel) {
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
    }
    //----------------------------------------------------------------------------------------------

    // 메인 페이지 오른쪽 패널 버튼 추가 함수-------------------------------------------------------------
    public void addRightButtons(JPanel newButtonPanel){
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
    }
    //----------------------------------------------------------------------------------------------

    //MemberShip 회원권 구매 함수--------------------------------------------------------------------------------
    private void performMembershipPurchase(int months, int pointsRequired, JEditorPane infoArea) {// 오른쪽 페이지 갱신하려고 파라미터로 infoArea 받아옴
        try {
            String checkPoints = "SELECT MemberPoint FROM members WHERE MemberEmail=?";
            PreparedStatement checkPointsStatement = connection.prepareStatement(checkPoints);
            checkPointsStatement.setString(1, loggedInUsername);
            ResultSet checkPointsResult = checkPointsStatement.executeQuery();

            if (checkPointsResult.next()) {
                int memberPoints = checkPointsResult.getInt("MemberPoint");

                if (memberPoints >= pointsRequired) {
                    int updatedPoints = memberPoints - pointsRequired;
                    String updatePointsSql = "UPDATE Members SET MemberPoint=? WHERE MemberEmail=?";
                    PreparedStatement updatePointsStatement = connection.prepareStatement(updatePointsSql);
                    updatePointsStatement.setInt(1, updatedPoints);
                    updatePointsStatement.setString(2, loggedInUsername);
                    updatePointsStatement.executeUpdate();

                    String updateMembershipSql = "UPDATE Members SET MemberDate=MemberDate+? WHERE MemberEmail=?";
                    PreparedStatement updateMembershipStatement = connection.prepareStatement(updateMembershipSql);
                    updateMembershipStatement.setInt(1, months * 30);
                    updateMembershipStatement.setString(2, loggedInUsername);
                    updateMembershipStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "회원권 구매가 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                    displayMemberInfo(infoArea);
                } else {
                    JOptionPane.showMessageDialog(this, "포인트가 부족합니다.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //----------------------------------------------------------------------------------------------

    // MemberShip.java 버튼 추가 메소드----------------------------------------------------------------
    public void addMemberShipButton(JPanel newButtonPanel, JEditorPane infoArea){// 오른쪽 페이지 갱신하려고 파라미터로 infoArea 받아옴
        newButtonPanel.setBackground(Color.BLACK);

        buttons[8] = new JButton("3개월 : 180,000 P");
        buttons[8].setForeground(Color.WHITE);
        buttons[8].setBackground(Color.DARK_GRAY);
        buttons[8].setPreferredSize(new Dimension(150, 50));
        buttons[8].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performMembershipPurchase(3, 180000,infoArea);
            }
        });
        newButtonPanel.add(buttons[8]);

        buttons[9] = new JButton("6개월 : 300,000 P");
        buttons[9].setForeground(Color.WHITE);
        buttons[9].setBackground(Color.DARK_GRAY);
        buttons[9].setPreferredSize(new Dimension(150, 50));
        buttons[9].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performMembershipPurchase(6, 300000,infoArea);
            }
        });
        newButtonPanel.add(buttons[9]);

        buttons[10] = new JButton("9개월 : 400,000 P");
        buttons[10].setForeground(Color.WHITE);
        buttons[10].setBackground(Color.DARK_GRAY);
        buttons[10].setPreferredSize(new Dimension(150, 50));
        buttons[10].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performMembershipPurchase(9, 400000,infoArea);
            }
        });
        newButtonPanel.add(buttons[10]);
    }
    //----------------------------------------------------------------------------------------------


    //MemberShipPT 버튼 추가 메소드--------------------------------------------------------------------
    public void addMemberShipPTButtons(JPanel newButtonPanel, JEditorPane infoArea){ // 오른쪽 페이지 갱신하려고 파라미터로 infoArea 받아옴
        buttons[8] = new JButton("10회 : 500,000 P");
        buttons[8].setForeground(Color.WHITE);
        buttons[8].setBackground(Color.DARK_GRAY);
        buttons[8].setPreferredSize(new Dimension(150, 50));
        buttons[8].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performPTPurchase(10, 500000,infoArea);
            }
        });
        newButtonPanel.add(buttons[8]);

        buttons[9] = new JButton("15회 : 700,000 P");
        buttons[9].setForeground(Color.WHITE);
        buttons[9].setBackground(Color.DARK_GRAY);
        buttons[9].setPreferredSize(new Dimension(150, 50));
        buttons[9].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performPTPurchase(15, 700000,infoArea);
            }
        });
        newButtonPanel.add(buttons[9]);

        buttons[10] = new JButton("20회 : 900,000 P");
        buttons[10].setForeground(Color.WHITE);
        buttons[10].setBackground(Color.DARK_GRAY);
        buttons[10].setPreferredSize(new Dimension(150, 50));
        buttons[10].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performPTPurchase(20, 900000,infoArea);
            }
        });
        newButtonPanel.add(buttons[10]);
    }
    //----------------------------------------------------------------------------------------------

    // MemberShip, MemberShipPT 페이지 갱신 메소드-----------------------------------------------------
    private void displayMemberInfo(JEditorPane infoArea) {// 오른쪽 페이지 갱신하려고 파라미터로 infoArea 받아옴
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
    //----------------------------------------------------------------------------------------------

    // MemberShipPT PT 구매 함수--------------------------------------------------------------------
    private void performPTPurchase(int sessions, int pointsRequired, JEditorPane infoArea) {// 오른쪽 페이지 갱신하려고 파라미터로 infoArea 받아옴
        try {
            String checkPoints = "SELECT MemberPoint FROM members WHERE MemberEmail=?";
            PreparedStatement checkPointsStatement = connection.prepareStatement(checkPoints);
            checkPointsStatement.setString(1, loggedInUsername);
            ResultSet checkPointsResult = checkPointsStatement.executeQuery();

            if (checkPointsResult.next()) {
                int memberPoints = checkPointsResult.getInt("MemberPoint");

                if (memberPoints >= pointsRequired) {
                    int updatedPoints = memberPoints - pointsRequired;
                    String updatePointsSql = "UPDATE members SET MemberPoint=? WHERE MemberEmail=?";
                    PreparedStatement updatePointsStatement = connection.prepareStatement(updatePointsSql);
                    updatePointsStatement.setInt(1, updatedPoints);
                    updatePointsStatement.setString(2, loggedInUsername);
                    updatePointsStatement.executeUpdate();

                    String updatePTSql = "UPDATE members SET MemberPT=MemberPT+? WHERE MemberEmail=?";
                    PreparedStatement updatePTStatement = connection.prepareStatement(updatePTSql);
                    updatePTStatement.setInt(1, sessions);
                    updatePTStatement.setString(2, loggedInUsername);
                    updatePTStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "PT 구매가 완료되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                    displayMemberInfo(infoArea);
                } else {
                    JOptionPane.showMessageDialog(this, "포인트가 부족합니다.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //----------------------------------------------------------------------------------


    private void PersonalInfo() throws SQLException {
        PersonalInfo personalInfo= new PersonalInfo(loggedInUsername, connection);
        personalInfo.setVisible(true);
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

    private void openMainPage() {
        MainPage mainPage = new MainPage(loggedInUsername, connection);
        mainPage.setVisible(true);
        dispose();
    }
    //-------------------------------------------------------------------------------

}
