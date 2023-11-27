import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class AdminCalender extends JFrame{
    String[] dayAr = {"일", "월", "화", "수", "목", "금", "토"};
    JButton[] dateBoxAr = new JButton[dayAr.length*5];
    JPanel p_center; //날짜 박스 처리할 영역
    Calendar cal; //날짜 객체
    JTextField logField = new JTextField();
    private String loggedInUsername;

    Connection connection;
    int yy; //기준점이 되는 년도
    int mm; //기준점이 되는 월
    int startDay; //월의 시작 요일
    int lastDate; //월의 마지막 날

    AdminCalender(String loggedInUsername, Connection connection){
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;

        p_center = new JPanel();
        add(p_center, BorderLayout.CENTER);

        getCurrentDate(); //현재 날짜 객체 생성
        getDateInfo(); //날짜 객체로 부터 정보들 구하기
        createDay(); //요일 박스 생성
        createDate(); //날짜 박스 생성
        printDate(); //상자에 날짜 그리기

    }

    //현재날짜 객체 만들기
    public void getCurrentDate() {
        cal = Calendar.getInstance();
        System.out.println(cal.get(Calendar.DATE));

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
            DateBox dayBox = new DateBox(dayAr[i], Color.gray, 100, 50);
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
        eventButton(); // 버튼마다 이벤트 생성
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

    //날짜 박스에 날짜 출력하기
    public void printDate() {
        System.out.println("시작 요일"+startDay);
        System.out.println("마지막 일"+lastDate);

        int n = 1;
        for(int i = 0; i < dateBoxAr.length; i++) {
            if(i >= startDay && n <= lastDate) {
                dateBoxAr[i].setText(Integer.toString(n));
                dateBoxAr[i].repaint();
                if(cal.get(Calendar.DATE) == n){ // 현재 날짜 배경색 바꾸기
                    dateBoxAr[i].setBackground(new Color(255,128,0));
                }
                n++;
            }else {
                dateBoxAr[i].setText("");
                dateBoxAr[i].repaint();
            }
        }
    }

    // 버튼 이벤트 메소드
    public void eventButton() {
        for (int i = 0; i < dayAr.length * 5; i++) {
            startDay = getFirstDayOfMonth(yy, mm);
            int selectedDay = i + 1 - startDay; // 선택된 날짜 (1부터 시작)

            dateBoxAr[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String trainerName = JOptionPane.showInputDialog(null, "트레이너 이름을 입력하세요:", "트레이너 이름 입력", JOptionPane.QUESTION_MESSAGE);

                    if (trainerName != null && !trainerName.isEmpty()) {
                        if (isValidTrainer(trainerName)) {
                            showTimeSlotDialog(trainerName, selectedDay);
                        } else {
                            JOptionPane.showMessageDialog(null, "잘못된 트레이너 이름입니다. 다시 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
        }
    }

    private boolean isValidTrainer(String trainerName) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String query = "SELECT TrainerName FROM Trainer WHERE TrainerName = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, trainerName);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void showTimeSlotDialog(String trainerName, int selectedDay) {
        JFrame dialogPanel = new JFrame();
        JButton saveBtn = new JButton("저장");
        JButton closeBtn = new JButton("닫기");
        JDialog dialog = new JDialog(dialogPanel, "PT 시간대 설정", true);
        JPanel buttonPanel = new JPanel();

        // 시간대 선택을 위한 UI 추가
        JPanel timeSelectionPanel = new JPanel();
        timeSelectionPanel.setLayout(new GridLayout(7, 2));
        JCheckBox[] timeCheckBoxes = new JCheckBox[14];

        for (int i = 0; i < 13; i++) {
            int hour = i + 9; // 9부터 시작
            String timeLabel = String.format("%02d:00", hour);
            timeCheckBoxes[i] = new JCheckBox(timeLabel);
            timeSelectionPanel.add(timeCheckBoxes[i]);
        }

        dialog.setLayout(new BorderLayout());
        buttonPanel.setLayout(new FlowLayout());

        buttonPanel.add(saveBtn);
        buttonPanel.add(closeBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(timeSelectionPanel, BorderLayout.CENTER);

        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveSelectedTime(trainerName, selectedDay, timeCheckBoxes);
                dialog.dispose();
            }
        });

        closeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.pack();
        dialog.setSize(400, 300);
        dialog.setVisible(true);
    }

    private void saveSelectedTime(String trainerName, int selectedDay, JCheckBox[] timeCheckBoxes) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            for (int i = 0; i < timeCheckBoxes.length; i++) {
                if (timeCheckBoxes[i] != null && timeCheckBoxes[i].isSelected()) {
                    int hour = i + 9;
                    int minute = (i % 2) * 0;

                    String startTime = String.format("%02d:%02d:00", hour, minute);
                    String endTime = String.format("%02d:%02d:59", hour + 1, minute);

                    if (!isTimeSlotAlreadyExists(trainerName, selectedDay, startTime, endTime, connection)) {
                        String insertQuery = "INSERT INTO TrainerSchedule (TrainerName, SelectDay, Available_Start_Time, Available_End_Time) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                            preparedStatement.setString(1, trainerName);
                            preparedStatement.setInt(2, selectedDay);
                            preparedStatement.setTime(3, java.sql.Time.valueOf(startTime));
                            preparedStatement.setTime(4, java.sql.Time.valueOf(endTime));
                            preparedStatement.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean isTimeSlotAlreadyExists(String trainerName, int selectedDay, String startTime, String endTime, Connection connection) {
        try {
            String query = "SELECT * FROM TrainerSchedule WHERE TrainerName = ? AND SelectDay = ? AND Available_Start_Time = ? AND Available_End_Time = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, trainerName);
                preparedStatement.setInt(2, selectedDay);
                preparedStatement.setTime(3, java.sql.Time.valueOf(startTime));
                preparedStatement.setTime(4, java.sql.Time.valueOf(endTime));
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}