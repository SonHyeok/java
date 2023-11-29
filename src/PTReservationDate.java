import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Calendar;

public class PTReservationDate extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel, topPanel, middlePanel, bottomPanel;
    private JLabel titleLabel, titleText, subText;
    private JButton[] buttons;
    private JButton selectTimeButton;
    private JTextArea infoArea;
    private Connection connection;
    private String loggedInUsername;  // 사용자 이메일을 저장하는 변수
    private String selectedTrainer;


    String[] dayAr = {"일", "월", "화", "수", "목", "금", "토"};
    JButton[] dateBoxAr = new JButton[dayAr.length*5];
    JPanel p_center; //날짜 박스 처리할 영역
    Calendar cal; //날짜 객체
    JTextField logField = new JTextField();

    int yy; //기준점이 되는 년도
    int mm; //기준점이 되는 월
    int startDay; //월의 시작 요일
    int lastDate; //월의 마지막 날



    PTReservationDate(String loggedInUsername, Connection connection, String selectedTrainer) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        this.selectedTrainer = selectedTrainer;
        initComponents();

    }



    public void initComponents() {
        UserPanelButtons userPanelButtons = new UserPanelButtons(loggedInUsername,connection);
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

        userPanelButtons.addLeftButtons(buttonPanel); // 패널에 버튼 추가
        leftPanel.add(buttonPanel, BorderLayout.CENTER); // 버튼 추가된 왼쪽 패널 add

        // 우측 패널 생성
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.white); // 패널 배경색을 검은색으로 설정
        c.add(rightPanel, BorderLayout.CENTER); // 패널을 중앙에 배치

        // 우측 패널의 상단, 중간, 하단 패널 생성
        topPanel = new JPanel(new BorderLayout());  // 상단 패널
        topPanel.setBackground(Color.WHITE);
        rightPanel.add(topPanel, BorderLayout.NORTH);
        middlePanel = new JPanel(new BorderLayout());  // 중간 패널
        middlePanel.setBackground(Color.WHITE);
        rightPanel.add(middlePanel, BorderLayout.CENTER);
        bottomPanel = new JPanel(new BorderLayout());  // 하단 패널
        bottomPanel.setBackground(Color.WHITE);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);


        // 제목(상단 패널 CENTER)
        titleText = new JLabel("PT 예약하기");
        titleText.setForeground(Color.BLACK);
        titleText.setBackground(Color.WHITE);
        titleText.setFont(new Font("맑은 고딕", Font.BOLD, 50));  // 폰트
        titleText.setHorizontalAlignment(JLabel.CENTER);  // 글자 가운데 배치
        topPanel.add(titleText, BorderLayout.CENTER);


        // ----------------------------------------------
        // 이번 달 PT 현황(중간 패널 NORTH)
        subText = new JLabel("이번달 PT 현황");
        subText.setForeground(Color.BLACK);
        subText.setFont(new Font("맑은 고딕", Font.PLAIN, 20));  // 폰트
        subText.setHorizontalAlignment(JLabel.CENTER);
        middlePanel.add(subText, BorderLayout.NORTH);


        // ----------------------------------------------
        // 캘린더(중간 패널 CENTER)

        createCalendar();



        // ----------------------------------------------
        // 회원님의 남은 PT 횟수(하단 패널 NORTH)

        // 회원의 남은 PT 횟수를 가져오는 메소드 호출
        String remainingPT = getRemainingPT(loggedInUsername);

        // 횟수를 보여줄 라벨 생성 및 설정
        JLabel ptStatusLabel = new JLabel("회원님의 남은 PT 횟수       " + remainingPT);
        ptStatusLabel.setForeground(Color.black);
        ptStatusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        ptStatusLabel.setHorizontalAlignment(JLabel.CENTER);
        bottomPanel.add(ptStatusLabel, BorderLayout.NORTH);


        // ----------------------------------------------
        // 시간 정하기 버튼(하단 패널 SOUTH)
        selectTimeButton = new JButton("시간 정하기");
        selectTimeButton.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        selectTimeButton.setBackground(Color.BLACK);
        selectTimeButton.setForeground(Color.WHITE);
        selectTimeButton.setPreferredSize(new Dimension(100, 100));
        // UIManager를 사용하여 JButton의 텍스트 색상 설정
        UIManager.put("Button.foreground", Color.black);
        selectTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 버튼 클릭 시 동작할 내용 구현
                // 캘린더에서 선택한 날짜 정보 가져오기
                // 여기에서 선택한 날짜 정보를 사용하여 PtSessions 테이블에 저장하는 코드 추가
                int selectedDay = getSelectedDay();
                //String selectedTrainer = getSelectedTrainer(); // 이전 페이지에서 선택한 트레이너(PtSessions의 가장 마지막에 생성된 튜플의 TrainerName)

                if (selectedDay != -1) {
                    // PtSessions 테이블에 예약 정보 저장
                    try {

                        // 가장 최근에 추가된 튜플의 ID를 가져오는 쿼리
                        String recentTupleIdQuery = "SELECT MAX(SessionID) AS recentId FROM PtSessions";
                        PreparedStatement recentTupleIdStatement = connection.prepareStatement(recentTupleIdQuery);
                        ResultSet resultSet = recentTupleIdStatement.executeQuery();

                        int recentTupleId = -1;

                        // 결과가 있을 경우 최근 ID를 가져옴
                        if (resultSet.next()) {
                            recentTupleId = resultSet.getInt("recentId");
                        }

                        // 결과가 있을 경우 최근 ID를 가져옴
                        if (resultSet.next()) {
                            recentTupleId = resultSet.getInt("recentId");
                        }

                        if (recentTupleId != -1) {
                            // 최근에 추가된 튜플에 값을 추가하는 쿼리
                            String addValueQuery = "UPDATE PtSessions SET PT_Date = ? WHERE SessionID = ?";  // 가장 최근에 추가된 튜플에 PT 시작 시간을 추가
                            PreparedStatement addValueStatement = connection.prepareStatement(addValueQuery);
                            addValueStatement.setInt(1, selectedDay);
                            addValueStatement.setInt(2, recentTupleId);
                            addValueStatement.executeUpdate();
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }



                // 다음 화면으로 이동
                PTReservationTime ptReservationTime = new PTReservationTime(loggedInUsername, connection, selectedTrainer, selectedDay);
                ptReservationTime.setVisible(true);
                setVisible(false);
            }
        });
        bottomPanel.add(selectTimeButton, BorderLayout.SOUTH);


        // ----------------------------------------------

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800); // 크기를 1000x800으로 키움
        setTitle("헬스장 출입 관리 시스템 - PT 예약하기");
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치
    }

    // 회원의 남은 PT 횟수를 가져오는 메소드
    private String getRemainingPT(String userEmail) {
        String remainingPT = "N/A";  // 초기값으로 "N/A" 설정 (데이터를 찾을 수 없을 때 표시할 값)

        try {
            // 데이터베이스에서 회원의 남은 PT 횟수를 가져오는 쿼리 작성 및 실행
            String query = "SELECT MemberPT FROM Members WHERE MemberEmail = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userEmail);
            ResultSet resultSet = preparedStatement.executeQuery();

            // 결과가 있을 경우 횟수를 가져옴
            if (resultSet.next()) {
                int ptCount = resultSet.getInt("MemberPT");
                remainingPT = String.valueOf(ptCount);
            }

            // 자원 해제
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return remainingPT;
    }

    // ---------------------------------------------------------------
    // 캘린더 생성 메서드
    private void createCalendar() {
        JPanel calendarPanel = new JPanel(new GridLayout(6, 7)); // 캘린더를 담을 패널 생성

        // 캘린더 코드 추가
        String[] dayAr = {"일", "월", "화", "수", "목", "금", "토"};
        JButton[] dateBoxAr = new JButton[dayAr.length * 5];

        Calendar cal = Calendar.getInstance();
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH);
        int startDay = getFirstDayOfMonth(yy, mm);
        int lastDate = getLastDate(yy, mm);

        // 요일 생성
        for (int i = 0; i < 7; i++) {
            DateBox2 dayBox = new DateBox2(dayAr[i], Color.gray, 110, 70);
            calendarPanel.add(dayBox);
        }

        // 날짜 생성
        for (int i = 0; i < dayAr.length * 5; i++) {
            JButton dateBox = new JButton("");
            dateBox.setPreferredSize(new Dimension(100, 40));
            calendarPanel.add(dateBox);
            dateBoxAr[i] = dateBox; // 버튼 배열에 버튼 추가
        }

        // 날짜 박스에 날짜 출력하기
        int n = 1;
        for (int i = 0; i < dateBoxAr.length; i++) {
            if (i >= startDay && n <= lastDate) {
                dateBoxAr[i].setText(Integer.toString(n));
                dateBoxAr[i].repaint();
                if (cal.get(Calendar.DATE) == n) { // 현재 날짜 배경색 바꾸기
                    dateBoxAr[i].setBackground(new Color(255, 128, 0));
                }

                // 코드 추가 부분
                final int selectedDay = n;
                dateBoxAr[i].addActionListener(e -> {
                    // 클릭한 날짜에 대한 동작 수행
                    System.out.println("Clicked on day " + selectedDay);
                    selectedDate = selectedDay;
                });

                n++;
            } else {
                dateBoxAr[i].setText("");
                dateBoxAr[i].repaint();
            }
        }

        // 캘린더 패널을 중간 패널에 추가
        middlePanel.add(calendarPanel, BorderLayout.CENTER);

        // 이벤트 처리를 위한 메서드 호출
        eventButton(dateBoxAr);
    }

    // 날짜에 대한 이벤트 처리 메서드
    private void eventButton(JButton[] dateBoxAr) {
        for (int i = 0; i < dateBoxAr.length; i++) {
            final int day = i; // 클로저 내에서 사용하기 위해 final 변수로 설정
            dateBoxAr[i].addActionListener(e -> {
                // 클릭한 날짜에 대한 동작 수행
                //System.out.println("Clicked on day " + (day + 1));
            });
        }
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
        return cal.get(Calendar.DATE);
    }

    // 날짜에 대한 이벤트 처리 메서드에서 사용할 변수 추가
    private int selectedDate = -1;

    // 날짜에 대한 이벤트 처리 메서드에서 선택한 날짜 정보 가져오기
    private int getSelectedDay() {
        // 여기에서 선택한 날짜 정보를 반환하는 코드 추가
        // 예시로 현재 날짜를 반환하도록 설정
        return selectedDate;
    }

    // PtSessions 테이블에 날짜 저장하는 메서드 추가
    private void saveSelectedDate(String trainerName, int selectedDay) {
        try {
            // PtSessions 테이블에 날짜 저장하는 쿼리 작성 및 실행
            String query = "INSERT INTO PtSessions (TrainerName, PT_Date) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, trainerName);
            preparedStatement.setInt(2, selectedDay);
            preparedStatement.executeUpdate();

            // 자원 해제
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // getSelectedTrainer() 메서드 추가
    private String getSelectedTrainer() {
        return selectedTrainer;
    }

}
