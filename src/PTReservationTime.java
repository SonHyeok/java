import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.List;

public class PTReservationTime extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel, titleText;
    private JButton[] buttons;
    private JButton reserveButton;
    private JTable timeTable;
    private JScrollPane scrollPane;
    private JTextArea infoArea;
    private Connection connection;
    // 로그인한 회원에 대한 사용자 정보를 처리하는 필드
    private String loggedInUsername;
    private String selectedTrainer;
    private int selectedDay;

    PTReservationTime(String loggedInUsername, Connection connection, String selectedTrainer, int selectedDay) {
        this.loggedInUsername = loggedInUsername;  // 사용자 정보를 저장할 필드
        this.connection = connection;
        this.selectedTrainer = selectedTrainer;
        this.selectedDay = selectedDay;
        initComponents();
    }

    public void initComponents() {
        c = this.getContentPane();
        c.setLayout(new BorderLayout()); // 레이아웃을 BorderLayout으로 변경
        c.setBackground(Color.white); // 검은색 배경 설정

        // 좌측 패널 생성
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.BLACK); // 패널 배경색을 검은색으로 설정
        c.add(leftPanel, BorderLayout.WEST); // 패널을 서쪽에 배치

        // 좌측 패널의 텍스트 부분
        titleLabel = new JLabel("OO헬스장에 오신걸 환영합니다.", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.DARK_GRAY);
        leftPanel.add(titleLabel, BorderLayout.NORTH);

        // 좌측 패널의 버튼 부분
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 0, 10));
        buttonPanel.setBackground(Color.BLACK); // 버튼 패널 배경색을 검은색으로 설정

        // 8개의 버튼 생성
        buttons = new JButton[8];

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

        // 우측 패널 생성
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.white); // 패널 배경색을 검은색으로 설정
        c.add(rightPanel, BorderLayout.CENTER); // 패널을 중앙에 배치

        // 제목
        titleText = new JLabel("PT 예약하기");
        titleText.setForeground(Color.BLACK);
        titleText.setBackground(Color.WHITE);
        titleText.setFont(new Font("맑은 고딕", Font.BOLD, 50));  // 폰트
        titleText.setHorizontalAlignment(JLabel.CENTER);  // 글자 가운데 배치
        rightPanel.add(titleText, BorderLayout.NORTH);

        // ----------------------------------------------

        // 예약 테이블
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("시간");
        model.addColumn("가능 여부");

        String [] startTime = {"09:00:00", "10:00:00", "11:00:00", "12:00:00"
                , "13:00:00", "14:00:00", "15:00:00", "16:00:00"
                , "17:00:00", "18:00:00", "19:00:00", "20:00:00", "21:00:00"};
        // 시간 데이터 고정 입력
        Vector<Object> rowData1 = new Vector<>();
        rowData1.add("09:00~10:00");
        model.addRow(rowData1);

        Vector<Object> rowData2 = new Vector<>();
        rowData2.add("10:00~11:00");
        model.addRow(rowData2);

        Vector<Object> rowData3 = new Vector<>();
        rowData3.add("11:00~12:00");
        model.addRow(rowData3);

        Vector<Object> rowData4 = new Vector<>();
        rowData4.add("12:00~13:00");
        model.addRow(rowData4);

        Vector<Object> rowData5 = new Vector<>();
        rowData5.add("13:00~14:00");
        model.addRow(rowData5);

        Vector<Object> rowData6 = new Vector<>();
        rowData6.add("14:00~15:00");
        model.addRow(rowData6);

        Vector<Object> rowData7 = new Vector<>();
        rowData7.add("15:00~16:00");
        model.addRow(rowData7);

        Vector<Object> rowData8 = new Vector<>();
        rowData8.add("16:00~17:00");
        model.addRow(rowData8);

        Vector<Object> rowData9 = new Vector<>();
        rowData9.add("17:00~18:00");
        model.addRow(rowData9);

        Vector<Object> rowData10 = new Vector<>();
        rowData10.add("18:00~19:00");
        model.addRow(rowData10);

        Vector<Object> rowData11 = new Vector<>();
        rowData11.add("19:00~20:00");
        model.addRow(rowData11);

        Vector<Object> rowData12 = new Vector<>();
        rowData12.add("20:00~21:00");
        model.addRow(rowData12);

        Vector<Object> rowData13 = new Vector<>();
        rowData13.add("21:00~22:00");
        model.addRow(rowData13);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "0000");

            String query = "SELECT Available_Start_Time FROM TrainerSchedule WHERE TrainerName = ? AND SelectDay = ?";  // ?: 값이 동적으로 지정
            PreparedStatement AvailableStartTime = connection.prepareStatement(query);
            AvailableStartTime.setString(1, selectedTrainer);
            AvailableStartTime.setInt(2, selectedDay);
            ResultSet resultSet = AvailableStartTime.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int sizeOfColumn = metaData.getColumnCount();
            List<Map> list = new ArrayList<Map>();
            Map<String, Object> map;
            String column;
            while(resultSet.next()) {
                map = new HashMap<String, Object>();
                for (int indexOfcolumn = 0; indexOfcolumn < sizeOfColumn; indexOfcolumn++) {
                    column = metaData.getColumnName(indexOfcolumn + 1);
                    map.put(column, resultSet.getString(column));
                }
                list.add(map);
            }
            // list에서 value 값들을 String으로 형변환해서 배열에 추가
            List<String[]> stringArrays = new ArrayList<>();
            for (Map<String, Object> mapEntry : list) {
                String[] values = mapEntry.values().stream().map(Object::toString).toArray(String[]::new);
                stringArrays.add(values);
            }
            // stringArrays 출력
            for (String[] values : stringArrays) {
                for (String value : values) {
                    System.out.print(value + " ");
                }
                System.out.println(); // 다음 행으로 넘어갈 때 줄 바꿈
            }
            System.out.println("model.getRowCount(): " + model.getRowCount());
            System.out.println("stringArrays size: " + stringArrays.size());

            timeTable = new JTable(model);
            // startTime과 stringArrays를 비교하여 '불가능' 또는 '가능'을 표시
            for (int i = 0; i < model.getRowCount(); i++) {
                String currentStartTime = startTime[i];

                // stringArrays의 각 행에 대해 일치 여부 확인
                boolean isUnavailable = stringArrays.stream()
                        .anyMatch(rowValues -> Arrays.asList(rowValues).contains(currentStartTime));

                // "불가능" 또는 "가능"으로 설정
                model.setValueAt(isUnavailable ? "가능" : "불가능", i, 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        timeTable = new JTable(model);

        // 테이블 헤더 부분 설정
        JTableHeader header = timeTable.getTableHeader();
        header.setFont(new Font("맑은 고딕", Font.BOLD, 30));

        // 테이블 내 데이터 부분 설정
        timeTable.setFont(new Font("맑은 고딕", Font.PLAIN, 30));
        timeTable.setRowHeight(getFontMetrics(timeTable.getFont()).getHeight());



        // 테이블 헤더와 데이터 부분 가운데 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        timeTable.setDefaultRenderer(Object.class, centerRenderer);
        JTableHeader header1 = timeTable.getTableHeader();
        header1.setDefaultRenderer(centerRenderer);

        scrollPane = new JScrollPane(timeTable);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // ----------------------------------------------

        reserveButton = new JButton("예약");
        reserveButton.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        reserveButton.setBackground(Color.black);
        reserveButton.setPreferredSize(new Dimension(100, 100));
        reserveButton.setForeground(Color.white);
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 예약 버튼 클릭 시 동작할 내용 구현
                int selectedRow = timeTable.getSelectedRow();  // 선택한 행
                if (selectedRow != -1) {  // 행이 선택된 경우
                    String selectedTimeRange = (String) timeTable.getValueAt(selectedRow, 0);
                    String[] times = selectedTimeRange.split("~");
                    String startTime = times[0].trim(); // "~" 앞 부분의 시간

                    // 예약 가능 여부 확인
                    String reservationStatus = (String) timeTable.getValueAt(selectedRow, 1);

                    if ("가능".equals(reservationStatus)) {

                        // PtSessions 테이블에 예약 정보 저장
                        try {
                            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "0000");

                            // 가장 최근에 추가된 튜플의 ID를 가져오는 쿼리
                            String recentTupleIdQuery = "SELECT MAX(SessionID) AS recentId FROM PtSessions";
                            PreparedStatement recentTupleIdStatement = connection.prepareStatement(recentTupleIdQuery);
                            ResultSet resultSet = recentTupleIdStatement.executeQuery();

                            int recentTupleId = -1;

                            // 결과가 있을 경우 최근 ID를 가져옴
                            if (resultSet.next()) {
                                recentTupleId = resultSet.getInt("recentId");
                            }

                            // 자원 해제
                            resultSet.close();
                            recentTupleIdStatement.close();

                            if (recentTupleId != -1) {
                                // 최근에 추가된 튜플에 값을 추가하는 쿼리
                                String addValueQuery = "UPDATE PtSessions SET StartTime = ? WHERE SessionID = ?";  // 가장 최근에 추가된 튜플에 PT 시작 시간을 추가
                                PreparedStatement addValueStatement = connection.prepareStatement(addValueQuery);
                                addValueStatement.setString(1, startTime);
                                addValueStatement.setInt(2, recentTupleId);
                                addValueStatement.executeUpdate();

                                JOptionPane.showMessageDialog(null, "예약이 완료되었습니다.");

                                // 다음 페이지 이동 메서드 추가
                                MainPage mainPage = new MainPage(loggedInUsername, connection);
                                mainPage.setVisible(true);

                                // TrianerSchedule 테이블의 TrainerName, SelectDay, Avalilable_Start_Time에 해당되는 튜플을 삭제
                                String deleteTrainerScheduleQuery = "DELETE FROM TrainerSchedule WHERE TrainerName = ? AND SelectDay = ? AND Available_Start_Time = ?";
                                PreparedStatement deleteTrainerScheduleStatement = connection.prepareStatement(deleteTrainerScheduleQuery);
                                deleteTrainerScheduleStatement.setString(1, selectedTrainer);
                                deleteTrainerScheduleStatement.setInt(2, selectedDay);
                                deleteTrainerScheduleStatement.setString(3, startTime);
                                deleteTrainerScheduleStatement.executeUpdate();
//                                connection.close();
                            } else {
                                JOptionPane.showMessageDialog(null, "예약 중 오류가 발생했습니다.");
                            }

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "예약 중 오류가 발생했습니다.");
                        }
                    } else {
                        // "불가능"인 경우 예약 불가 메시지 출력
                        JOptionPane.showMessageDialog(null, "예약이 불가합니다.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "예약할 시간을 선택하세요.");
                }
            }
        });
        rightPanel.add(reserveButton, BorderLayout.SOUTH); // 예약 버튼을 패널의 남쪽에 배치

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800); // 크기를 1000x800으로 키움
        setTitle("헬스장 출입 관리 시스템 - PT 예약하기");
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치
    }

    private void openMainPage(String loggedInUsername) {
        MainPage mainPage = new MainPage(loggedInUsername,connection);
        mainPage.setVisible(true);
        setVisible(false);
        //dispose();
    }
}
