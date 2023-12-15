import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

public class AdminPTSet extends JFrame {
    private Container c;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel;
    private Connection connection; // 데이터베이스 연결을 위한 변수
    private String loggedInUsername; // 로그인한 사용자 이름을 저장하는 변수

    AdminPTSet(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername; // 로그인한 사용자 이름
        this.connection = connection; // 데이터베이스 연결
        initComponents(); // UI 컴포넌트 초기화 메소드 호출
    }

    // UI 컴포넌트 초기화 메소드 정의
    private void initComponents() {
        c = this.getContentPane(); // 컨텐츠 팬 설정
        c.setLayout(new BorderLayout()); // 레이아웃 관리자 설정
        c.setBackground(Color.BLACK); // 배경색을 검은색으로 설정

        leftPanel = new JPanel(new BorderLayout()); // 왼쪽 패널 생성
        leftPanel.setBackground(Color.BLACK); // 패널 배경색을 검은색으로 설정
        c.add(leftPanel, BorderLayout.WEST); // 왼쪽 패널을 컨테이너의 서쪽에 배치

        titleLabel = new JLabel("OO헬스장 관리자 페이지.", SwingConstants.CENTER); // 제목 라벨 생성 및 설정
        titleLabel.setForeground(Color.WHITE); // 글자색을 흰색으로 설정
        titleLabel.setOpaque(true); // 라벨의 배경색 설정을 위해 투명도 사용
        titleLabel.setBackground(Color.DARK_GRAY); // 라벨의 배경색을 어두운 회색으로 설정
        leftPanel.add(titleLabel, BorderLayout.NORTH); // 제목 라벨을 왼쪽 패널의 북쪽에 배치

        AdminPanelButtons ap = new AdminPanelButtons(loggedInUsername, connection); // 사용자 정의된 AdminPanelButtons 객체 생성
        leftPanel.add(ap); // AdminPanelButtons를 왼쪽 패널에 추가

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 닫기 동작 설정
        setSize(1000, 800); // 프레임 크기 설정
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지"); // 프레임 제목 설정
        setResizable(false); // 프레임 크기 조절 금지
        setLocationRelativeTo(null); // 프레임을 화면 가운데로 위치

        rightPanel = new JPanel(new BorderLayout()); // 오른쪽 패널 생성
        rightPanel.setBackground(Color.BLACK); // 패널 배경색을 검은색으로 설정

        AdminCalender calenderPanel = new AdminCalender(loggedInUsername, connection); // 사용자 정의된 AdminCalender 객체 생성
        c.add(rightPanel, BorderLayout.CENTER); // 오른쪽 패널을 컨테이너의 중앙에 배치
        rightPanel.add(calenderPanel.p_center, BorderLayout.CENTER); // AdminCalender의 중앙 패널을 오른쪽 패널의 중앙에 배치

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 닫기 동작 설정
        setSize(1000, 800); // 프레임 크기 설정
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지"); // 프레임 제목 설정
        setResizable(false); // 프레임 크기 조절 금지
        setLocationRelativeTo(null); // 프레임을 화면 가운데로 위치
    }
}