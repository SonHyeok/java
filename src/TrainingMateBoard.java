import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TrainingMateBoard extends JFrame {
    private Container c;
    JTable table;
    JFrame topFrame;
    private String DeleteMateInfoSQL = "DELETE FROM workoutmate WHERE matetitleid = ?";
    private String getMateInfoSQL = "SELECT matetitleid,memberemail,workoutdate,workouttime,workoutpart,meetingplace FROM workoutmate WHERE memberemail != ?;";
    private String setMateSQL = "INSERT INTO matchmate (publisherEmail,mateEmail,WorkoutDate,WorkoutTime,WorkoutPart,MeetingPlace) VALUES(?,?,?,?,?,?);";
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel, titleText;
    private String loggedInUsername;
    private ResultSet rs;
    private PreparedStatement stmt;
    private Connection connection;
    JButton[] buttons = new JButton[12];
    private JButton partInBtn; // 참가 버튼
    private String publisherEmail,mateDate, mateTime, workoutPart,meetPlace, matchtitleId;

    TrainingMateBoard(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        initComponents();
    }


    private void initComponents() {
        UserPanelButtons userPanelButtons = new UserPanelButtons(loggedInUsername,connection); // 버튼 생성해주는 클래스 객체 생성
        c = this.getContentPane();

        // 최상위 프레임 정보 변수에 저장
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(c);

        c.setLayout(new BorderLayout());
        c.setBackground(Color.BLACK);

        leftPanel = new JPanel(new BorderLayout()); // 왼쪽 메뉴 패널 생성 및 설정
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

        rightPanel = new JPanel(new BorderLayout()); // 오른쪽 패널 생성 및 설정
        rightPanel.setBackground(Color.BLACK);

        titleText = new JLabel("운동메이트 참가하기"); // 오른쪽 패널 타이틀 설정
        titleText.setForeground(Color.WHITE);
        titleText.setBackground(Color.BLACK);
        titleText.setFont(new Font("맑은 고딕", Font.BOLD, 50));  // 폰트
        titleText.setHorizontalAlignment(JLabel.CENTER);  // 글자 가운데 배치

        rightPanel.add(titleText, BorderLayout.NORTH); // 타이틀 패널 추가

        c.add(rightPanel, BorderLayout.CENTER);
        showBoard(rightPanel); // 메이트 게시판 오른쪽 패널에 추가

        JPanel newButtonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        newButtonPanel.setBackground(Color.BLACK);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void showBoard(JPanel rightPanel)  { // 운동 메이트 게시글 추가하는 함수
        String mateInfo;

        DefaultTableModel model = new DefaultTableModel(); // 게시글 등록할 테이블 생성
        model.addColumn("번호");
        model.addColumn("목록");

        table = new JTable(model);
        partInBtn = new JButton("참가");

        try { // db에 등록된 데이터 가져와서 게시글 생성
            stmt = connection.prepareStatement(getMateInfoSQL);
            stmt.setString(1,loggedInUsername); // 본인 아이디로 등록한 게시글이 아닌 것만 가져오기 위해 로그인한 유저의 아이디 sql 조건으로 사용
            rs = stmt.executeQuery();

            while (rs.next()) { // 가져온 모든 게시글 생성
                String arr[] = new String[2];
                matchtitleId = rs.getString(1);
                publisherEmail = rs.getString(2);
                mateDate = rs.getString(3);
                mateTime = rs.getString(4);
                workoutPart = rs.getString(5);
                meetPlace = rs.getString(6);

                mateInfo = mateDate + "일 " + " 시간 : " + mateTime + " " + workoutPart + "운동 " + meetPlace + "에서 만나요.";
                arr[0] = matchtitleId;
                arr[1] = mateInfo;
                model.addRow(arr); // 게시글 타이틀 번호와 정보 테이블에 추가 => 게시글 추가
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        rightPanel.add(new JScrollPane(table), BorderLayout.CENTER); // 게시판 오른쪽 패널에 추가
        rightPanel.add(partInBtn,BorderLayout.SOUTH); // 참가 버튼 오른쪽 패널에 추가

        partInBtn.addActionListener(new ActionListener() { // 참가 버튼 이벤트
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow(); // 선택된 행의 번호
                if (row == -1) { // 아무것도 선택되지 않은 경우
                    JOptionPane.showMessageDialog(null, "참여할 운동을 선택해주세요.");
                }
                else {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    try { // 매치된 정보를 db에 저장하기 위해 쿼리에 정보 저장
                        stmt = connection.prepareStatement(setMateSQL);
                        stmt.setString(1,publisherEmail );
                        stmt.setString(2,loggedInUsername);
                        stmt.setString(3,mateDate);
                        stmt.setString(4,mateTime);
                        stmt.setString(5,workoutPart );
                        stmt.setString(6,meetPlace );

                        int num = stmt.executeUpdate(); // 쿼리가 제대로 실행되었는지 변수에 저장하여 검사

                        if (num > 0) { // 쿼리가 제대로 수행된 경우 게시글에 대한 정보를 db에서 삭제
                            stmt = connection.prepareStatement(DeleteMateInfoSQL);
                            stmt.setInt(1, Integer.parseInt(String.valueOf(model.getValueAt(row,0)))); // 선택된 게시글의 번호 삭제
                            stmt.executeUpdate();

                            model.removeRow(row); // 프로그램 내에서의 게시글 삭제

                            JOptionPane.showMessageDialog(null,"참가 완료되었습니다!");
                            topFrame.dispose();

                            // 다음 페이지 이동 메서드 추가
                            MainPage mainPage = new MainPage(loggedInUsername, connection);
                            mainPage.setVisible(true);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }


                }
            }
        });
    }
}

