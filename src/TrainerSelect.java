import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class TrainerSelect extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel, buttonPanel, trainerPanel;
    private JLabel titleLabel, titleText;
    private JButton[] buttons, trainerButtons;
    private JTextArea infoArea;
    private Connection connection;
    private String selectTrainerName;
    // 로그인한 회원에 대한 사용자 정보를 저장하는 필드
    private String loggedInUsername;

    TrainerSelect(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;  // 사용자 정보를 저장할 필드
        this.connection = connection;
        initComponents();
    }

    private void initComponents() {
        AllPanelButtons allPanelButtons = new AllPanelButtons(loggedInUsername,connection);
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

        allPanelButtons.addLeftButtons(buttonPanel); // 패널에 버튼 추가
        leftPanel.add(buttonPanel, BorderLayout.CENTER); // 버튼 추가된 왼쪽 패널 add


        leftPanel.add(buttonPanel, BorderLayout.CENTER);

        // 우측 패널 생성
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.white); // 패널 배경색을 검은색으로 설정
        c.add(rightPanel, BorderLayout.CENTER); // 패널을 중앙에 배치

        // 제목
        titleText = new JLabel("트레이너 선택하기");
        titleText.setForeground(Color.BLACK);
        titleText.setBackground(Color.WHITE);
        titleText.setFont(new Font("맑은 고딕", Font.BOLD, 50));  // 폰트
        titleText.setHorizontalAlignment(JLabel.CENTER);  // 글자 가운데 배치
        rightPanel.add(titleText, BorderLayout.NORTH);

        // ----------------------------------------------

        // 트레이너 선택 버튼 네 개 추가
        trainerPanel = new JPanel(new GridLayout(4, 1, 30, 30)); // 2행 2열의 그리드 레이아웃, 간격은 10
        //trainerPanel.setBackground(Color.WHITE); // 패널 배경색을 검정색으로 설정
        infoArea = new JTextArea();

        // 트레이너 정보를 표시하는 버튼 추가
        trainerButtons = new JButton[4];
        String[] trainerNames = {"김봄", "김여름", "김가을", "김겨울"};
        for (int i = 0; i < 4; i++) {
            System.out.println(getTrainerName(i));
            trainerButtons[i] = new JButton("트레이너 : " + getTrainerName(i));
            trainerButtons[i].setForeground(Color.white); // 버튼 글자색을 흰색으로 설정
            trainerButtons[i].setBackground(Color.BLACK); // 버튼 배경색을 어두운 회색으로 설정
            // 글자 크기를 변경하는 코드
            Font buttonFont = trainerButtons[i].getFont();
            trainerButtons[i].setFont(new Font(buttonFont.getName(), Font.PLAIN, 25));

            trainerButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedTrainerIndex = -1;
                    for (int j = 0; j < 4; j++) {
                        if (e.getSource() == trainerButtons[j]) {
                            selectedTrainerIndex = j;
                            break;
                        }
                    }

                    if (selectedTrainerIndex != -1) {
                        String trainerName = getTrainerName(selectedTrainerIndex);
                        String trainerInfo = getTrainerInfo(trainerName);
                        // JTextArea에 트레이너 정보 설정
                        infoArea.setText(trainerInfo);

                        // 새로운 창에서 트레이너 정보 보여주기
                        showTrainerInfoDialog(trainerInfo);                    }
                }
            });


            trainerPanel.add(trainerButtons[i]); // 그리드 레이아웃 패널에 버튼 추가
        }

        infoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(infoArea);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        rightPanel.add(trainerPanel, BorderLayout.CENTER);

        // ----------------------------------------------

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800); // 크기를 1000x800으로 키움
        setTitle("헬스장 출입 관리 시스템 - 트레이너 선택하기");
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치
    }

    // 트레이너 정보를 가져오는 메소드
    private String getTrainerInfo(String trainerName) {
        String trainerInfo = ""; // 트레이너 정보 문자열

        // 데이터베이스에서 트레이너 정보를 가져오는 쿼리 작성 및 실행
        try {
            String query = "SELECT TrainerName, TrainerRate, TrainerYear FROM Trainer WHERE TrainerName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, trainerName);
            ResultSet resultSet = preparedStatement.executeQuery();

            // 결과가 있을 경우 정보를 가져옴
            if (resultSet.next()) {
                String name = resultSet.getString("TrainerName");
                int rate = resultSet.getInt("TrainerRate");
                int year = resultSet.getInt("TrainerYear");

                // 트레이너 정보 문자열 생성
                trainerInfo = "트레이너 이름: " + name + "\n점수: " + rate + "\n경력: " + year;
            }

            // 자원 해제
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return trainerInfo;
    }

    // 새로운 창에서 트레이너 정보 보여주기
    private void showTrainerInfoDialog(String trainerInfo) {
        JTextArea textArea = new JTextArea(trainerInfo);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 새 창 닫기
                ((Window) SwingUtilities.getRoot((Component) e.getSource())).dispose();
            }
        });

        JButton selectButton = new JButton("선택");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 선택 버튼이 눌리면 데이터베이스에 저장하고 창을 닫기
                saveToDatabase(); // 여기에 데이터베이스에 저장하는 로직을 추가하세요.

                // 기존 창으로 이동 또는 다음 화면으로 이동하는 코드를 추가하세요.
                PTReservationDate ptReservationDate = new PTReservationDate(loggedInUsername, connection, selectTrainerName);
                ptReservationDate.setVisible(true);
                //new MainPage().setVisible(true);
                setVisible(false);

                // 새 창 닫기
                ((Window) SwingUtilities.getRoot((Component) e.getSource())).dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        buttonPanel.add(selectButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);


        JOptionPane.showOptionDialog(this, panel, "트레이너 정보", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
    }

    // 선택된 버튼의 TrainerName 가져오기
    private String getTrainerName(int index) {
        // 이 부분은 버튼에 맞게 TrainerName을 리턴해주는 로직이어야 합니다.
        // 예를 들어, 각 버튼에 해당하는 TrainerName이 저장된 배열이나 리스트가 있다고 가정하고 구현합니다.
        ArrayList<String> names = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("select TrainerName from Trainer;");
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                names.add(rs.getString("TrainerName"));
            }
        }catch(SQLException e){
        }
        return names.get(index);
    }

    // 선택 버튼이 눌렸을 때, 데이터베이스에 선택한 트레이너 이름을 저장하는 메소드
    private void saveToDatabase() {
        String trainerInfo = infoArea.getText();  // JTextArea에서 텍스트 가져오기
        String trainerName = extractTrainerName(trainerInfo);  // 트레이너 이름 추출
        System.out.println(trainerName);
        selectTrainerName = trainerName;

        // 트레이너 이름 db에 저장 로직
        try {
            // 트레이너가 존재하는지 확인
            if (TrainerExist(trainerName)) {
                // 트레이너가 존재하면 PtSessions에 삽입
                String query = "INSERT INTO PtSessions (TrainerName, MemberEmail) Values (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, trainerName);
                preparedStatement.setString(2, loggedInUsername);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } else {
                // 트레이너가 존재하지 않으면 예외 처리 또는 사용자에게 알림을 추가할 수 있음
                JOptionPane.showMessageDialog(this, "해당 트레이너가 존재하지 않습니다.", "에러", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 트레이너가 존재하는지 확인하는 메소드
    private boolean TrainerExist(String trainerName) {
        try {
            String query = "SELECT COUNT(*) FROM java.Trainer WHERE TrainerName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, trainerName);
            ResultSet resultSet = preparedStatement.executeQuery();

            // 결과가 있을 경우 첫 번째 열의 값을 확인
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

            // 자원 해제
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // JTextArea에서 트레이너 이름을 추출하는 메소드
    private String extractTrainerName(String trainerInfo) {
        String[] lines = trainerInfo.split("\n");

        if (lines.length > 0) {
            String trainerNameLine = lines[0];
            String[] tokens = trainerNameLine.split(":");

            if (tokens.length > 1) {
                return tokens[1].trim(); // 공백을 제거하여 반환
            }
        }

        // 적절한 형식을 찾지 못한 경우
        return "Unknown";
    }

}