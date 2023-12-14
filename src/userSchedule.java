import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userSchedule extends JFrame {
    private String getPublishSchedule = "SELECT mateemail, workoutdate, workouttime, workoutpart, meetingplace FROM matchmate where publisheremail = ?";
    private String getParticipateSchedule = "SELECT publisheremail, workoutdate, workouttime, workoutpart, meetingplace FROM matchmate where mateemail = ?";

    private String email,date, time, part, place, workoutInfo, meetTime;
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel, titleText;
    private String loggedInUsername;
    private ResultSet rs;
    private PreparedStatement stmt;
    JButton[] buttons = new JButton[12];
    Connection connection;
    JTable table;
    userSchedule(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection; // connection 변수를 클래스 멤버 변수에 할당
        initComponents();
    }

    private void initComponents() {
        UserPanelButtons userPanelButtons = new UserPanelButtons(loggedInUsername,connection);
        c = this.getContentPane();

        //최상위 프레임 정보 변수에 저장
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(c);

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

        userPanelButtons.addLeftButtons(buttonPanel,buttons,topFrame); // 패널에 버튼 추가
        leftPanel.add(buttonPanel, BorderLayout.CENTER); // 버튼 추가된 왼쪽 패널 add

        rightPanel = new JPanel(new BorderLayout());

        titleText = new JLabel("운동 스케줄");
        titleText.setForeground(Color.BLACK);
        titleText.setBackground(Color.WHITE);
        titleText.setFont(new Font("맑은 고딕", Font.BOLD, 50));  // 폰트
        titleText.setHorizontalAlignment(JLabel.CENTER);  // 글자 가운데 배치

        rightPanel.add(titleText, BorderLayout.NORTH);

        c.add(rightPanel, BorderLayout.CENTER);

        JPanel newButtonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        newButtonPanel.setBackground(Color.BLACK);
        showSchedule(rightPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void showSchedule(JPanel rightPanel){
        DefaultTableModel model = new DefaultTableModel(); // 스케줄 등록할 테이블 생성
        model.addColumn("일정");
        model.addColumn("내용");
        model.addColumn("메이트 아이디");
        table = new JTable(model);

        try{
            // 자신이 공고후 인원이 모집된 운동 정보 생성
            stmt = connection.prepareStatement(getPublishSchedule);
            stmt.setString(1,loggedInUsername);
            rs = stmt.executeQuery();

            while(rs.next()){  // 쿼리 실행 결과 모두 저장
                String arr[] = new String[3];
                email = rs.getString(1);
                date = rs.getString(2);
                time = rs.getString(3);
                part = rs.getString(4);
                place = rs.getString(5);

                // 모집했던 운동 인원 정보와 운동 정보를 스케줄 표에 표시
                workoutInfo = "운동 파트 : " + part +" 만남 장소 : " + place;
                meetTime = date + "일, " + "시간 : " + time;
                arr[0] = meetTime;
                arr[1] = workoutInfo;
                arr[2] = email + "(참여)";
                model.addRow(arr); // 스케줄 표에 추가
            }

            // 자신이 참여하기로 했던 운동 정보 생성
            stmt = connection.prepareStatement(getParticipateSchedule);
            stmt.setString(1,loggedInUsername);
            rs = stmt.executeQuery();

            while(rs.next()){   // 쿼리 실행 결과 모두 테이블에 저장
                String arr[] = new String[3];
                email = rs.getString(1);
                date = rs.getString(2);
                time = rs.getString(3);
                part = rs.getString(4);
                place = rs.getString(5);

                // 참여하기로 했던 운동의 모든 정보를 스케줄 표에 게시
                workoutInfo = "운동 파트 : " + part +", 만남 장소 : " + place;
                meetTime = date + "일, " + "시간 : " + time;
                arr[0] = meetTime;
                arr[1] = workoutInfo;
                arr[2] = email;
                model.addRow(arr); // 스케줄 표에 추가
            }

            rightPanel.add(new JScrollPane(table), BorderLayout.CENTER); // 게시판 오른쪽 패널에 추가

        }catch (SQLException e){

        }

    }
}
