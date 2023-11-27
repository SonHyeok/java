import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.*;
import java.util.List;

import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;

public class TrainerRate extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel, titleText;
    private JButton[] buttons;
    private JTable timeTable;
    private JScrollPane scrollPane;
    private Connection connection;
    // 로그인한 회원에 대한 사용자 정보를 처리하는 필드
    private String loggedInUsername;

    TrainerRate(String loggedInUsername, Connection connection) throws SQLException {
        this.loggedInUsername = loggedInUsername;  // 사용자 정보를 저장할 필드
        this.connection = connection;
        initComponents();
    }

    public void initComponents() throws SQLException {
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

// 트레이너 선택 버튼---------------------------------------
        JButton selectTrainer = new JButton("트레이너 선택으로 이동");
        selectTrainer.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        selectTrainer.setBackground(Color.BLACK);
        selectTrainer.setForeground(Color.WHITE);
        selectTrainer.setPreferredSize(new Dimension(100, 100));
        selectTrainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TrainerSelect(loggedInUsername,connection);
            }
        });
        rightPanel.add(selectTrainer,BorderLayout.SOUTH);

        //------------------------------------------------------

        // 제목
        titleText = new JLabel("PT 예약하기");
        titleText.setForeground(Color.BLACK);
        titleText.setBackground(Color.WHITE);
        titleText.setFont(new Font("맑은 고딕", Font.BOLD, 50));  // 폰트
        titleText.setHorizontalAlignment(JLabel.CENTER);  // 글자 가운데 배치
        rightPanel.add(titleText, BorderLayout.NORTH);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("트레이너");
        model.addColumn("평가 순위");

        String trainerInfoSql = "SELECT TrainerName,TrainerRate from Trainer;";
        // beforeFirst() 사용하기 위한 옵션 추가
        PreparedStatement tInfo = connection.prepareStatement(trainerInfoSql, TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet trainerInfos = tInfo.executeQuery();

//        if (tInfo != null)		try { tInfo.close(); }		catch (Exception e) {}

        // 시간 데이터 고정 입력
        Vector<Object> rowData1 = new Vector<>();
        if(trainerInfos.next()){
            rowData1.add(trainerInfos.getString("TrainerName"));
            model.addRow(rowData1);
        }

        Vector<Object> rowData2 = new Vector<>();
        if(trainerInfos.next()){
            rowData2.add(trainerInfos.getString("TrainerName"));
            model.addRow(rowData2);
        }

        Vector<Object> rowData3 = new Vector<>();
        if(trainerInfos.next()){
            rowData3.add(trainerInfos.getString("TrainerName"));
            model.addRow(rowData3);
        }
        Vector<Object> rowData4 = new Vector<>();
        if(trainerInfos.next()){
            rowData4.add(trainerInfos.getString("TrainerName"));
            model.addRow(rowData4);
        }

        Vector<Object> rowData5 = new Vector<>();
        if(trainerInfos.next()){
            rowData5.add(trainerInfos.getString("TrainerName"));
            model.addRow(rowData5);
        }

        Vector<Object> rowData6 = new Vector<>();
        if(trainerInfos.next()){
            rowData6.add(trainerInfos.getString("TrainerName"));
            model.addRow(rowData6);
        }

        Vector<Object> rowData7 = new Vector<>();
        if(trainerInfos.next()){
            rowData7.add(trainerInfos.getString("TrainerName"));
            model.addRow(rowData7);
        }

        timeTable = new JTable(model);
        //------------------------------------------------------------------

        trainerInfos.beforeFirst(); // next()로 이동했던 커서 처음으로 재이동
        List<String> list = new ArrayList<>();
        while(trainerInfos.next()) {
            list.add(trainerInfos.getString("TrainerRate")); // 추천수 추가
        }

        // 추천 수 표시
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(list.get(i),i,1);
        }


        //-----------------------------------------------------------------------

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
