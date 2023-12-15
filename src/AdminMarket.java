import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminMarket extends JFrame {
    private Container c; // JFrame의 컨테이너
    private JPanel leftPanel; // 왼쪽 패널
    private JLabel titleLabel;
    private Connection connection; // 데이터베이스 연결 객체
    private String loggedInUsername; // 현재 로그인한 사용자의 이름
    private JEditorPane infoArea;

    AdminMarket(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        initComponents(); // 초기화 메서드 호출
        DisplayMarketBoard(); // 장터 게시글 표시 메서드 호출
    }

    private void initComponents() {
        c = this.getContentPane(); // 컨텐츠 팬 설정
        c.setLayout(new BorderLayout()); // 레이아웃 매니저 설정
        c.setBackground(Color.BLACK); // 배경색 설정

        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.BLACK);
        c.add(leftPanel, BorderLayout.WEST);

        titleLabel = new JLabel("OO헬스장 관리자 페이지.", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.DARK_GRAY);
        leftPanel.add(titleLabel, BorderLayout.NORTH);

        AdminPanelButtons ap = new AdminPanelButtons(loggedInUsername, connection);
        leftPanel.add(ap); // 사용자 정의 패널을 왼쪽 패널에 추가

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);

        infoArea = new JEditorPane(); // 정보 표시 영역 생성
        infoArea.setContentType("text/html"); // HTML 형식의 텍스트를 표시하기 위해 설정
        infoArea.setEditable(false); // 편집 불가능하도록 설정

        JScrollPane scrollPane = new JScrollPane(infoArea); // 스크롤 가능한 패널 생성
        c.add(scrollPane, BorderLayout.CENTER); // 스크롤 패널을 컨텐츠 팬의 가운데에 추가
    }

    private void DisplayMarketBoard() {
        try {
            String sql = "SELECT * FROM marketandboard";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(); // 쿼리 실행 및 결과셋 얻기
            String htmlText = "<html>"; // HTML 형식의 텍스트를 위한 문자열 초기화

            // 장터 정보를 한 번만 추가
            htmlText += "<div style='text-align: center; color: white; font-size: 28px; font-family: \"맑은 고딕\", sans-serif; background-color: black;'>";
            htmlText += "<b>장터</b> " + "<br>";
            htmlText += "</div>";
            htmlText += "<hr style='border-top: 2px solid white;'>";  // 구분선

            while (resultSet.next()) { // 결과셋을 반복하며 정보를 가져옴
                String category = resultSet.getString("Category"); // 카테고리 가져오기
                if (category.equals("장터글")) { // 카테고리가 '장터글'인 경우에만 처리
                    htmlText += "<div style='text-align: center; color: white; font-size: 28px; font-family: \"맑은 고딕\", sans-serif; background-color: black;'>";
                    htmlText += "<b>게시글 ID :</b> " + resultSet.getInt("MarketBoardID") + "<br>";
                    htmlText += "<b>제목 :</b> " + resultSet.getString("Title") + "<br>";
                    htmlText += "<b>판매자:</b> " + resultSet.getString("PostedEmail") + "<br>";
                    String buyerEmail = resultSet.getString("BuyerEmail");
                    if (buyerEmail != null) {
                        htmlText += "<b>구매자:</b> " + buyerEmail + "<br>";
                    } else {
                        htmlText += "<b>구매자:</b> <br>";
                    }
                    htmlText += "</div>";
                    htmlText += "<hr style='border-top: 2px solid white;'>";  // 구분선
                }
            }

            htmlText += "</html>"; // HTML 문자열 완성
            infoArea.setText(htmlText);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}