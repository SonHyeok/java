import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Calendar;

import static java.util.Calendar.DATE;

public class logCalender extends JFrame{
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel;
    private JButton[] buttons;
    private String loggedInUsername;

    String[] dayAr = {"일", "월", "화", "수", "목", "금", "토"};
    JButton[] dateBoxAr = new JButton[dayAr.length*5];
    JPanel p_center; //날짜 박스 처리할 영역
    Calendar cal; //날짜 객체

    int yy; //기준점이 되는 년도
    int mm; //기준점이 되는 월
    int startDay; //월의 시작 요일
    int lastDate; //월의 마지막 날



    Connection connection;
    PreparedStatement preparedStatement;
    Statement statement;

     logCalender(String loggedInUsername, Connection connection) throws SQLException {
         this.loggedInUsername = loggedInUsername;
         this.connection = connection; // connection 변수를 클래스 멤버 변수에 할당

        initComponents();

        getCurrentDate(); //현재 날짜 객체 생성
        getDateInfo(); //날짜 객체로 부터 정보들 구하기
        createDay(); //요일 박스 생성
        createDate(); //날짜 박스 생성
        printDate(); //상자에 날짜 그리기
        setSize(1000,800);
    }

    private void initComponents() {
        p_center = new JPanel();

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

        buttons = new JButton[11];

        buttons[0] = new JButton("메인페이지");
        buttons[0].setForeground(Color.WHITE);
        buttons[0].setBackground(Color.DARK_GRAY);
        buttons[0].setPreferredSize(new Dimension(150, 50));
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMainPage(loggedInUsername);
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
        rightPanel.add(p_center, BorderLayout.CENTER);
        c.add(rightPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }

    //현재날짜 객체 만들기
    public void getCurrentDate() {
        cal = Calendar.getInstance();
        System.out.println(cal.get(DATE));

    }

    //시작 요일, 끝 날 등 구하기
    public void getDateInfo() {
        yy = cal.get(Calendar.YEAR);
        mm = cal.get(Calendar.MONTH);
        startDay = getFirstDayOfMonth(yy, mm);
        lastDate = getLastDate(yy, mm);
    }

    //요일 생성
    public void createDay() {
        for(int i = 0; i < 7; i++){
            DateBox2 dayBox = new DateBox2(dayAr[i], Color.gray, 100, 50);
            p_center.add(dayBox);
        }
    }

    //날짜 생성
    public void createDate() {
        for(int i = 0; i < dayAr.length*5; i++) {
            JButton dateBox = new JButton("");

            dateBox.setPreferredSize(new Dimension(100,40));
            p_center.add(dateBox);
            dateBoxAr[i] = dateBox; // 버튼 배열에 버튼 추가
        }
        saveLog(); // 버튼마다 이벤트 생성
    }

    public int getFirstDayOfMonth(int yy, int mm) {
        Calendar cal = Calendar.getInstance(); //날짜 객체 생성
        cal.set(yy, mm, 1);
        return cal.get(Calendar.DAY_OF_WEEK)-1;//요일은 1부터 시작으로 배열과 쌍을 맞추기 위해 -1
    }

    //사용 방법 : 2021년 2월을 구할시 2021, 1을 넣으면 됨
    public int getLastDate(int yy, int mm) {
        Calendar cal = Calendar.getInstance();
        cal.set(yy, mm+1, 0);
        //마지막 날을 의미한다.
        return cal.get(DATE);
    }

    //날짜 박스에 날짜 출력하기
    public void printDate() throws SQLException {
        System.out.println("시작 요일"+startDay);
        System.out.println("마지막 일"+lastDate);

        try{
            int k = 1; // select 결과 인덱싱 위한 변수
            int j = 0; // select 결과 배열에 저장하기 위한 인덱싱 변수
            int[] visitDay = new int[30]; //select 결과(날짜 데이터) 저장하기 위한 배열

            String visitSql = "SELECT VisitDate from visit"; // 입장한 날짜 가져와서 저장
            statement = connection.createStatement();
            ResultSet days = statement.executeQuery(visitSql);

            while(days.next()){ // 배열에 입장한 날짜 저장하는 반복문
                visitDay[j] = (days.getDate(k)).getDate();
                j++;
            }

            int n = 1; // 날짜 setText 하기 위한 변수
            for(int i = 0; i < dateBoxAr.length; i++) {
                if(i >= startDay && n <= lastDate) {
                    dateBoxAr[i].setText(Integer.toString(n));
                    dateBoxAr[i].repaint();
                    if(cal.get(DATE) == n){ // 현재 날짜 배경색 바꾸기
                        dateBoxAr[i].setBackground(new Color(255,128,0)); // 주황색으로 변경
                    }
                    else{ // 입장한 날짜 색 바꾸기
                        for(int v = 0; v < visitDay.length; v++ ){
                            if(visitDay[v] == n){ // 방문했던 날짜일 경우
                                dateBoxAr[i].setBackground(new Color(102, 255,0)); // 초록색으로 변경
                            }
                        }
                    }
                    n++;
                }else { // 방문한 적 없고, 달력에 표시할 수 있는 날짜가 더이상 없는 경우 "" 설정
                    dateBoxAr[i].setText("");
                    dateBoxAr[i].repaint();
                }
            }
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    public void saveLog(){
        for(int i = 0; i < dayAr.length*5; i++){
            JButton curBtn = dateBoxAr[i]; // 현재 클릭된 버튼 텍스트 가져오려고 만든 변수

            curBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFrame dialogPanel = new JFrame();
                    JButton saveBtn = new JButton("저장");
                    JButton closeBtn = new JButton("닫기");
                    JTextArea logField = new JTextArea();
                    JDialog dialog = new JDialog(dialogPanel, "운동 일지", true);
                    JPanel buttonPanel = new JPanel();
                    String btnDate = curBtn.getText(); // 버튼에 써있는 날짜 가져오기
                    ResultSet logDatas; // 일지 데이터 저장하는 변수

                    dialog.setLayout(new BorderLayout());
                    buttonPanel.setLayout(new FlowLayout());

                    logField.setLineWrap(true); // 자동 줄바꿈 설정
                    logField.setPreferredSize(new Dimension(200, 200));
                    saveBtn.setPreferredSize(new Dimension(100, 40));
                    closeBtn.setPreferredSize(new Dimension(100, 40));

                    buttonPanel.add(saveBtn);
                    buttonPanel.add(closeBtn);
                    dialog.add(logField, BorderLayout.CENTER);
                    dialog.add(buttonPanel, BorderLayout.SOUTH);


                    //작성 되어 있는 로그 가져오는 코드 작성-------------------------------------
                    try {
                        String logInfoSql = "SELECT WorkoutDate, WorkoutDetails from workoutlog where MemberEmail = ? ";
                        PreparedStatement preparedStatement = connection.prepareStatement(logInfoSql);
                        preparedStatement.setString(1,loggedInUsername);
                        logDatas = preparedStatement.executeQuery();

                        while(logDatas.next()){
                            if(logDatas.getString("WorkoutDate").equals(btnDate)){
                                logField.setText(logDatas.getString("WorkoutDetails"));
                            }
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    //--------------------------------------------------

                    saveBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String logText = logField.getText();

                            // "저장" 버튼을 클릭하면 텍스트 영역의 내용을 변수에 저장
                            try {
                                String sql = "insert into workoutLog (MemberEmail,WorkoutDate,WorkoutDetails) values (?,?,?)";// db에 일지 작성하는 이벤트 만들어야함
                                preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setString(1, loggedInUsername);
                                preparedStatement.setString(2, btnDate);
                                preparedStatement.setString(3, logText);

                                preparedStatement.executeUpdate();

                                // 저장 버튼 클릭 후 팝업창 이벤트
                                JOptionPane.showMessageDialog(saveBtn,"저장되었습니다!");
                                dialog.dispose();

                            }catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });

                    closeBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // "닫기" 버튼을 클릭하면 다이얼로그를 닫음
                            dialog.dispose();
                        }
                    });

                    dialog.pack(); // gpt가 해줘서 뭔지 모르겠음
                    dialog.setSize(300,300);
                    dialog.setVisible(true);
                }
            });
        }
    }

    private void openMainPage(String loggedInUsername) {
        MainPage mainPage = new MainPage(loggedInUsername,connection);
        mainPage.setVisible(true);
        dispose();
    }
}

