import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
    private String recommendTrainerSQL = "UPDATE trainer SET TrainerRate = TrainerRate + 1 WHERE TrainerName = ?";
    private Container c;
    private ResultSet rs;
    private PreparedStatement stmt;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel, titleText;
    JButton[] buttons = new JButton[12];
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
        UserPanelButtons userPanelButtons = new UserPanelButtons(loggedInUsername,connection);
        c = this.getContentPane();
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

        leftPanel.add(buttonPanel, BorderLayout.CENTER);

        // 우측 패널 생성
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.white); // 패널 배경색을 검은색으로 설정
        c.add(rightPanel, BorderLayout.CENTER); // 패널을 중앙에 배치

// 트레이너 선택 버튼---------------------------------------
        JButton selectTrainer = new JButton("트레이너 선택으로 이동");
        JButton recommendTrainer = new JButton("트레이너 추천");
        JPanel ButtonPanel = new JPanel(new FlowLayout());
        selectTrainer.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        selectTrainer.setBackground(Color.BLACK);
        selectTrainer.setForeground(Color.WHITE);
        selectTrainer.setPreferredSize(new Dimension(300, 100));
        selectTrainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                topFrame.dispose();
                new TrainerSelect(loggedInUsername,connection); //트레이너 선택 창으로 넘어감
            }
        });

        // 트레이너 추천 버튼 생성 및 이벤트 설정
        recommendTrainer.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        recommendTrainer.setBackground(Color.BLACK);
        recommendTrainer.setForeground(Color.WHITE);
        recommendTrainer.setPreferredSize(new Dimension(300, 100));
        recommendTrainer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTrainer;
                int row = timeTable.getSelectedRow(); // 선택된 트레이너의 행 가져옴
                if (row == -1) { // 아무것도 선택되지 않은 경우
                    JOptionPane.showMessageDialog(null, "추천할 트레이너를 선택해주세요.");
                }else{ // 트레이너가 선택된 경우
                    selectedTrainer = String.valueOf(timeTable.getValueAt(row,0));
                    try {
                        stmt = connection.prepareStatement(recommendTrainerSQL);
                        stmt.setString(1,selectedTrainer); // 추천수 1 추가

                        int num = stmt.executeUpdate();
                        if(num > 0){ // 추천이 성공적으로 완료된 경우
                            JOptionPane.showMessageDialog(null, "추천 완료되었습니다!");
                            
                            // 페이지 갱신
                            topFrame.dispose(); 
                            new TrainerRate(loggedInUsername,connection);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });

        ButtonPanel.add(recommendTrainer);
        ButtonPanel.add(selectTrainer);

        rightPanel.add(ButtonPanel, BorderLayout.SOUTH);

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
}
