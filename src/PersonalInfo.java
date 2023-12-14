import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonalInfo extends JFrame {
    private String editInfoSQL = "UPDATE members SET membername = ?,memberpassword = ?, memberpn = ? WHERE memberemail = ?";
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel,titleText;
    JButton[] buttons = new JButton[12];
    private String loggedInUsername;
    Connection connection;
    JButton editBtn;
    JFrame topFrame;
    private ResultSet rs;
    private PreparedStatement stmt;
    PersonalInfo(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        initComponents();
    }



    private void initComponents() {
        UserPanelButtons userPanelButtons = new UserPanelButtons(loggedInUsername,connection);
        c = this.getContentPane();

        // 최상위 프레임 정보 변수에 저장
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(c);

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

        titleText = new JLabel("개인정보 변경");
        titleText.setForeground(Color.BLACK);
        titleText.setBackground(Color.WHITE);
        titleText.setFont(new Font("맑은 고딕", Font.BOLD, 50));  // 폰트
        titleText.setHorizontalAlignment(JLabel.CENTER);  // 글자 가운데 배치

        rightPanel.add(titleText, BorderLayout.NORTH);
        editUserInfo(rightPanel); // 정보 변경하는 컴포넌트들 오른쪽 패널에 추가
        c.add(rightPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void editUserInfo(JPanel rightPanel){
        JPanel editPanel = new JPanel();
        JTextField Name = new JTextField(20);
        JTextField PN = new JTextField(20);
        JPasswordField password = new JPasswordField(20);
        editBtn = new JButton("수정하기");
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    // 정보 변경하는 작업
                    stmt = connection.prepareStatement(editInfoSQL);
                    stmt.setString(1,Name.getText());
                    stmt.setString(2,password.getText());
                    stmt.setString(3,PN.getText());
                    stmt.setString(4,loggedInUsername);
                    int num = stmt.executeUpdate();
                    if(num > 0){ // 정보 변경 성공한 경우 메세지 출력
                        JOptionPane.showMessageDialog(null, "정보가 변경되었습니다!");

                        //상위 프레임 종료
                        topFrame.dispose();

                        // 다음 페이지 이동 메서드 추가
                        MainPage mainPage = new MainPage(loggedInUsername, connection);
                        mainPage.setVisible(true);
                    }
                }catch(SQLException ae){

                }
            }
        });

        editPanel.add(new JLabel("이름 : "));
        editPanel.add(Name);
        editPanel.add(new JLabel("전화번호 : "));
        editPanel.add(PN);
        editPanel.add(new JLabel("비밀번호 : "));
        editPanel.add(password);
        editPanel.add(editBtn);
        rightPanel.add(editPanel,BorderLayout.CENTER);


    }
}

