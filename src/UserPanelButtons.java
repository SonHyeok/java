import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UserPanelButtons extends JPanel {

    private String loggedInUsername = null;
    private Connection connection = null;
    private PreparedStatement stmt;

    public UserPanelButtons(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
    }

    // 메인 페이지 왼쪽 버튼 추가 메소드
    public void addLeftButtons(JPanel buttonPanel, JButton[] buttons,JFrame c) {
        // 파라미터로 버튼 넣을 패널, 페이지 이동 메소드 추가할 버튼, 사용중이던 프레임 받아옴
        buttonPanel.setBackground(Color.BLACK);

        // 메인페이지 버튼 생성 및 이벤트 설정
        buttons[0] = new JButton("메인페이지");
        buttons[0].setForeground(Color.WHITE);
        buttons[0].setBackground(Color.DARK_GRAY);
        buttons[0].setPreferredSize(new Dimension(150, 50));
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c.dispose(); // 이전에 사용중이던 프레임 닫기
                openMainPage();
            }
        });
        buttonPanel.add(buttons[0]);

        // 입장 버튼 등록 및 이벤트 생성
        buttons[1] = new JButton("입장");
        buttons[1].setForeground(Color.WHITE);
        buttons[1].setBackground(Color.DARK_GRAY);
        buttons[1].setPreferredSize(new Dimension(150, 50));
        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "어서오세요!");
                //현재 날짜
                LocalDate now = LocalDate.now();
                // 현재 시간
                 LocalTime nowtime = LocalTime.now();
                // 현재시간 출력
                 System.out.println(nowtime);  // 06:20:57.008731300
                // 포맷 정의하기
                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                // 포맷 적용하기
                 String formatedNow = nowtime.format(formatter);
                // 포맷 적용된 현재 시간 출력
                 System.out.println(formatedNow);

                 // 입장 날짜 및 시간 db에 입력
                 String InsertVisitDataSQL = "INSERT INTO visit(memberemail, visitdate, visittime)values(?,?,?);";
                try {
                    stmt = connection.prepareStatement(InsertVisitDataSQL);
                    stmt.setString(1,loggedInUsername);
                    stmt.setString(2, String.valueOf(now));
                    stmt.setString(3,formatedNow);

                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });
        buttonPanel.add(buttons[1]);

        // 퇴장 버튼 등록 및 이벤트 설정
        buttons[2] = new JButton("퇴장");
        buttons[2].setForeground(Color.WHITE);
        buttons[2].setBackground(Color.DARK_GRAY);
        buttons[2].setPreferredSize(new Dimension(150, 50));
        buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //현재 날짜
                LocalDate now = LocalDate.now();

                // 현재 시간
                LocalTime nowtime = LocalTime.now();
                // 현재시간 출력
                System.out.println(nowtime);  // 06:20:57.008731300
                // 포맷 정의하기
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                // 포맷 적용하기
                String formatedNow = nowtime.format(formatter);
                // 포맷 적용된 현재 시간 출력
                System.out.println(formatedNow);

                // 퇴장시간 업데이트
                String InsertEndTimeDataSQL = "UPDATE visit SET endtime = ? WHERE memberemail = ? and visitdate = ?";
                try {
                    stmt = connection.prepareStatement(InsertEndTimeDataSQL);
                    stmt.setString(1,formatedNow);
                    stmt.setString(2,loggedInUsername);
                    stmt.setString(3, String.valueOf(now));
                    int num = stmt.executeUpdate();

                    if(num > 0){ // 입장 기록 없이 퇴장먼저하면 경고 메세지
                        c.dispose();
                        JOptionPane.showMessageDialog(null, "안녕히가세요!");
                    }else{
                        JOptionPane.showMessageDialog(null, "입장 먼저 해주세요!");

                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(buttons[2]);

        //개인정보 수정 버튼 생성 및 이벤트 설정
        buttons[3] = new JButton("개인정보 수정");
        buttons[3].setForeground(Color.WHITE);
        buttons[3].setBackground(Color.DARK_GRAY);
        buttons[3].setPreferredSize(new Dimension(150, 50));
        buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    c.dispose();
                    PersonalInfo();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(buttons[3]);

        //트레이너 평가 버튼 생성 및 이벤트 설정
        buttons[4] = new JButton("트레이너 평가");
        buttons[4].setForeground(Color.WHITE);
        buttons[4].setBackground(Color.DARK_GRAY);
        buttons[4].setPreferredSize(new Dimension(150, 50));
        buttons[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    c.dispose();
                    TrainerRate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonPanel.add(buttons[4]);

        //게시판 버튼 생성 및 이벤트 설정
        buttons[5] = new JButton("게시판");
        buttons[5].setForeground(Color.WHITE);
        buttons[5].setBackground(Color.DARK_GRAY);
        buttons[5].setPreferredSize(new Dimension(150, 50));
        buttons[5].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c.dispose();
                openMarketAndBoardPage();
            }
        });
        buttonPanel.add(buttons[5]);

        // 운동메이트 버튼 생성 및 이벤트 설정
        buttons[6] = new JButton("운동메이트");
        buttons[6].setForeground(Color.WHITE);
        buttons[6].setBackground(Color.DARK_GRAY);
        buttons[6].setPreferredSize(new Dimension(150, 50));
        buttons[6].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try { // 운동메이트 구하는 글을 게시할 것인지, 게시된 글에 참여할 것인지 구분하여 페이지 연결
                    String[] selectPage = {"게시","참가"};
                    int result = JOptionPane.showOptionDialog(null, "게시 or 참가", "Option"
                    ,JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, selectPage, null );

                    if(result == 0){ // 게시인 경우
                        c.dispose();
                        TrainingMatePost();

                    }
                    else if(result == 1){ // 참가인 경우
                        c.dispose();
                        TrainingMateBoard();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        buttonPanel.add(buttons[6]);

        // 일정확인 버튼 생성 및 이벤트 설정
        buttons[7] = new JButton("일정 확인");
        buttons[7].setForeground(Color.WHITE);
        buttons[7].setBackground(Color.DARK_GRAY);
        buttons[7].setPreferredSize(new Dimension(150, 50));
        buttons[7].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    c.dispose();
                    UserSchedule();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        buttonPanel.add(buttons[7]);
    }
    //----------------------------------------------------------------------------------------------

    // 메인 페이지 오른쪽 패널 버튼 추가 함수-------------------------------------------------------------
    public void addRightButtons(JPanel newButtonPanel, JButton[] buttons,JFrame c){
        // 파라미터로 버튼 넣을 패널, 페이지 이동 메소드 추가할 버튼, 사용중이던 프레임 받아옴
        buttons[8] = new JButton("PT구매");
        buttons[8].setForeground(Color.WHITE);
        buttons[8].setBackground(Color.DARK_GRAY);
        buttons[8].setPreferredSize(new Dimension(150, 50));
        buttons[8].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c.dispose();
                OpenMemberShipPT();
            }
        });
        newButtonPanel.add(buttons[8]);

        // 회원권 구매 버튼 생성 및 이벤트 설정
        buttons[9] = new JButton("회원권 구매");
        buttons[9].setForeground(Color.WHITE);
        buttons[9].setBackground(Color.DARK_GRAY);
        buttons[9].setPreferredSize(new Dimension(150, 50));
        buttons[9].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c.dispose();
                OpenMemberShip();
            }
        });
        newButtonPanel.add(buttons[9]);

        //PT예약 버튼 생성 및 이벤트 설정
        buttons[10] = new JButton("PT 예약하기");
        buttons[10].setForeground(Color.WHITE);
        buttons[10].setBackground(Color.DARK_GRAY);
        buttons[10].setPreferredSize(new Dimension(150, 50));
        buttons[10].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    c.dispose();
                    TrainerRate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        newButtonPanel.add(buttons[10]);

        // 운동일지 버튼 생성 및 이벤트 설정
        buttons[11] = new JButton("운동일지 작성하기");
        buttons[11].setForeground(Color.WHITE);
        buttons[11].setBackground(Color.DARK_GRAY);
        buttons[11].setPreferredSize(new Dimension(150, 50));
        buttons[11].addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  try {
                      c.dispose();
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
    // 회원권 구매 및 화면 갱신 메소드
    // 오른쪽 페이지 갱신하려고 파라미터로 infoArea 받아옴
    private void performMembershipPurchase(int months, int pointsRequired, JEditorPane infoArea) {
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
                    displayMemberInfoMember(infoArea);
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
    // 회원권 구매하는 화면 오른쪽 패널 버튼 생성 및 이벤트 설정 메소드
    public void addMemberShipButton(JPanel newButtonPanel, JEditorPane infoArea, JButton[] buttons){// 오른쪽 페이지 갱신하려고 파라미터로 infoArea 받아옴
        newButtonPanel.setBackground(Color.BLACK);
        // 3개월권 구매하는 버튼 및 이벤트 생성
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

        // 6개월권 구매하는 버튼 및 이벤트 생성
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

        // 9개월권 구매하는 버튼 및 이벤트 생성
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
    // PT 구매하는 버튼 생성, 화면 갱신하는 메소드
    // 오른쪽 페이지 갱신하려고 파라미터로 infoArea 받아옴
    public void addMemberShipPTButtons(JPanel newButtonPanel, JEditorPane infoArea, JButton[] buttons){
        // 10회 구매 버튼 및 이벤트 설정
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

        // 15회 구매 버튼 및 이벤트 설정
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

        // 20회 구매 버튼 및 이벤트 설정
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
    private void displayMemberInfoMember(JEditorPane infoArea) {// 오른쪽 페이지 갱신하려고 파라미터로 infoArea 받아옴
        try {
            String sql = "SELECT * FROM Members WHERE MemberEmail=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, loggedInUsername);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // HTML 형식으로 문자열 구성
                String htmlText = "<html>";
                htmlText += "<div style='text-align: center; color: white; font-size: 28px; font-family: \"맑은 고딕\", sans-serif;'>";
                htmlText += "<b>회원 이용권 구매</b> " + "<br>" + "<br>";
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

    private void displayMemberInfoPT(JEditorPane infoArea) {// 오른쪽 페이지 갱신하려고 파라미터로 infoArea 받아옴
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
                    displayMemberInfoPT(infoArea);
                } else {
                    JOptionPane.showMessageDialog(this, "포인트가 부족합니다.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //----------------------------------------------------------------------------------


    // 개인정보 변경 페이지 이동 메소드
    private void PersonalInfo() throws SQLException {
        PersonalInfo personalInfo= new PersonalInfo(loggedInUsername, connection);
        personalInfo.setVisible(true);
    }

    // 회원권 구매 페이지 이동 메소드
    private void OpenMemberShip() {
        MemberShip membership = new MemberShip(loggedInUsername, connection);
        membership.setVisible(true);
    }

    // PT 구매 페이지 이동 메소드
    private void OpenMemberShipPT() {
        MemberShipPT membershippt = new MemberShipPT(loggedInUsername, connection);
        membershippt.setVisible(true);
    }

    // 운동일지 페이지 이동 메소드
    private void OpenLogCalender() throws SQLException {
        logCalender logCalender = new logCalender(loggedInUsername, connection);
        logCalender.setVisible(true);
    }

    // 트레이너 평가 열람 페이지 이동 메소드
    private void TrainerRate() throws SQLException {
        TrainerRate trainerRate= new TrainerRate(loggedInUsername, connection);
        trainerRate.setVisible(true);
    }

    // 운동 메이트 게시 페이지 이동 메소드
    private void TrainingMatePost() throws SQLException {
        TrainingMatePost trainingMatePost = new TrainingMatePost(loggedInUsername, connection);
        trainingMatePost.setVisible(true);
    }

    // 운동 메이트 참가 페이지 이동 메소드
    private void TrainingMateBoard() throws SQLException {
        TrainingMateBoard trainingMateBoard = new TrainingMateBoard(loggedInUsername, connection);
        trainingMateBoard.setVisible(true);
    }

    // 운동 스케줄 페이지 이동 메소드
    private void UserSchedule() throws SQLException{
        userSchedule userSchedule = new userSchedule(loggedInUsername,connection);
        userSchedule.setVisible(true);
    }
    
    //메인 페이지 이동 메소드
    private void openMainPage() {
        MainPage mainPage = new MainPage(loggedInUsername, connection);
        mainPage.setVisible(true);
    }

    //장터 및 게시판 페이지 이동 메소드
    private void openMarketAndBoardPage() {
        MarketAndBoard marketandboard = new MarketAndBoard(loggedInUsername, connection);
        marketandboard.setVisible(true);
    }
    //-------------------------------------------------------------------------------

}
