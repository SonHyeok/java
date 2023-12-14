import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

public class TrainingMatePost extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel, titleText;
    private String loggedInUsername;
    private ResultSet rs;
    private PreparedStatement stmt;
    JButton[] buttons = new JButton[12];

    String[] times = {"06:00","07:00","08:00","09:00", "10:00", "11:00", "12:00"
            , "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00","22:00","23:00"};
    String[] places = {"유산소 존","웨이트 존","스트레칭 존","카운터 앞","정수기 앞"};
    ArrayList<String> dates = new ArrayList<>();

    Connection connection;

    String inspectSQL = "SELECT * FROM workoutmate WHERE memberemail = ? and workoutdate = ? and workouttime = ?";
    String postSQL = "INSERT INTO workoutmate(memberemail,workoutdate,workouttime,workoutpart,meetingplace) VALUES(?,?,?,?,?)";

    TrainingMatePost(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection; // connection 변수를 클래스 멤버 변수에 할당
        initComponents();
    }


    private void initComponents() {
        UserPanelButtons userPanelButtons = new UserPanelButtons(loggedInUsername,connection);
        c = this.getContentPane();
        // 최상위 프레임 정보 변수에 저장
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

        titleText = new JLabel("운동메이트 게시하기");
        titleText.setForeground(Color.BLACK);
        titleText.setBackground(Color.WHITE);
        titleText.setFont(new Font("맑은 고딕", Font.BOLD, 50));  // 폰트
        titleText.setHorizontalAlignment(JLabel.CENTER);  // 글자 가운데 배치

        rightPanel.add(titleText, BorderLayout.NORTH);

        c.add(rightPanel, BorderLayout.CENTER);
        Mate(rightPanel); //오른쪽 패널 채우기

        JPanel newButtonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        newButtonPanel.setBackground(Color.BLACK);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private static JRadioButton getSelectedBtn(ButtonGroup bg){ // 현재 선택된 라디오 버튼의 값 반환하는 메소드
        Enumeration<AbstractButton> elements = bg.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton button = elements.nextElement();
            if (button.isSelected() && button instanceof JRadioButton) {
                return (JRadioButton) button;
            }
        }
        return null;
    }

    private void Mate(JPanel rightPanel){ // 운동메이트 게시글 등록을 위한 컴포넌트 생성하는 메소드
        JComboBox timeBox;
        JComboBox<String> dateBox;
        JComboBox placeBox;
        JButton postBtn = new JButton("게시");


        // 운동 파트 버튼 생성 및 버튼 그룹 생성
        ButtonGroup workoutParts = new ButtonGroup();
        JRadioButton sholderBtn = new JRadioButton("어깨");
        JRadioButton chestBtn = new JRadioButton("가슴");
        JRadioButton backBtn = new JRadioButton("등");
        JRadioButton tricepsBtn = new JRadioButton("삼두");
        JRadioButton bicepsBtn = new JRadioButton("이두");
        JRadioButton absBtn = new JRadioButton("복근");
        JRadioButton legBtn = new JRadioButton("하체");
        JRadioButton cardioBtn = new JRadioButton("유산소");

        // 버튼 그룹에 파트 버튼 추가
        workoutParts.add(sholderBtn);
        workoutParts.add(chestBtn);
        workoutParts.add(backBtn);
        workoutParts.add(tricepsBtn);
        workoutParts.add(bicepsBtn);
        workoutParts.add(absBtn);
        workoutParts.add(legBtn);
        workoutParts.add(cardioBtn);

        // 박스 레이아웃에 필요한 박스 생성
        Box hBox = Box.createHorizontalBox();

        // 박스에 버튼들 추가 및 간격 설정
        hBox.add(sholderBtn);
        hBox.add(Box.createHorizontalStrut(50));

        hBox.add(chestBtn);
        hBox.add(Box.createHorizontalStrut(50));

        hBox.add(backBtn);
        hBox.add(Box.createHorizontalStrut(50));

        hBox.add(tricepsBtn);
        hBox.add(Box.createHorizontalStrut(50));

        hBox.add(bicepsBtn);
        hBox.add(Box.createHorizontalStrut(50));

        hBox.add(absBtn);
        hBox.add(Box.createHorizontalStrut(50));

        hBox.add(legBtn);
        hBox.add(Box.createHorizontalStrut(50));

        hBox.add(cardioBtn);
        hBox.add(Box.createHorizontalStrut(50));

        // 운동 파트 버튼들 추가할 패널 생성 및 테두리 생성후 추가
        JPanel matePanel = new JPanel(new BorderLayout());
        matePanel.setBorder(new TitledBorder(new EtchedBorder(),"운동 파트"));
        matePanel.add(hBox, BorderLayout.CENTER);


        //------------------------------------------------------------------------------

        // 날짜 데이터 arrayList에 추가
        for(int i = 1; i < 31; i++){
            dates.add(String.valueOf(i));
        }

        // ArrayList를 ComboBoxModel로 변환
        ComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(dates.toArray(new String[0]));

        // 콤보박스에 데이터 삽입해서 객체 생성
        dateBox = new JComboBox<>(comboBoxModel);
        placeBox = new JComboBox(places);
        timeBox = new JComboBox(times);

        // 운동 약속 잡는 패널 생성
        JPanel promisePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,80,150));

       // 패널에 요소들 추가 및 테두리 생성
        promisePanel.add(new JLabel("시간"));
        promisePanel.add(timeBox);

        promisePanel.add(new JLabel("날짜"));
        promisePanel.add(dateBox);

        promisePanel.add(new JLabel("만남 위치"));
        promisePanel.add(placeBox);

        promisePanel.setBorder(new TitledBorder(new EtchedBorder(),"운동 약속 정보"));

        // 앞에서 생성한 박스들 담을 박스 생성 및 컴포넌트 추가
        Box totalBox = Box.createVerticalBox();
        totalBox.add(promisePanel);
        totalBox.add(matePanel);

        rightPanel.add(totalBox, BorderLayout.CENTER);
        rightPanel.add(postBtn,BorderLayout.SOUTH);

        postBtn.addActionListener(new ActionListener() { // 게시 버튼 이벤트
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 운동파트가 선택되지 않았을 경우
                    if(getSelectedBtn(workoutParts) == null){
                        JOptionPane.showMessageDialog(null, "운동 파트를 선택해주세요.");}
                    // 운동파트 선택된 경우
                    else{
                        // 이미 게시한 날짜, 시간인지 검사
                        stmt = connection.prepareStatement(inspectSQL);
                        stmt.setString(1,loggedInUsername);
                        stmt.setString(2, String.valueOf(dateBox.getSelectedItem()));
                        stmt.setString(3,String.valueOf(timeBox.getSelectedItem()));

                        rs = stmt.executeQuery();

                        if(rs.next()){
                            JOptionPane.showMessageDialog(null, "이미 게시한 시간입니다!");
                        }
                        else{ // 게시된 시간이 아닌경우 게시글 작성
                            stmt = connection.prepareStatement(postSQL);
                            stmt.setString(1,loggedInUsername);
                            stmt.setString(2, String.valueOf(dateBox.getSelectedItem()));
                            stmt.setString(3,String.valueOf(timeBox.getSelectedItem()));
                            stmt.setString(4,getSelectedBtn(workoutParts).getText());
                            stmt.setString(5,String.valueOf(placeBox.getSelectedItem()));
                            stmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "게시되었습니다!");
                        }
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }


}
