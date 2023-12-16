import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarketBoardDetail extends JFrame {
    private Container c;
    private JLabel titleLabel, detailLabel, priceLabel, buyerLabel;
    private JButton purchaseButton, closeButton;
    private String marketBoardID;
    private ResultSet rs;
    private PreparedStatement stmt;
    private Connection connection;
    private String loggedInUsername;

    // MarketBoardDetail 생성자
    public MarketBoardDetail(String marketBoardID, Connection connection, String loggedInUsername) {
        this.marketBoardID = marketBoardID;
        this.connection = connection;
        this.loggedInUsername = loggedInUsername;
        initComponents();

    }

    // UI 구성을 위한 초기화 메소드
    private void initComponents() {
        c = this.getContentPane();
        c.setLayout(new BorderLayout());

        // 데이터베이스에서 상세 정보 가져오기 위한 SQL 문
        String getMarketBoardDetailSQL = "SELECT PostedEmail, Title, Detail, Price, BuyerEmail, Category FROM marketandboard WHERE MarketBoardID = ?";

        try {
            stmt = connection.prepareStatement(getMarketBoardDetailSQL);
            stmt.setString(1, marketBoardID);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String PostedEmail = rs.getString("PostedEmail");
                String title = rs.getString("Title");
                String detail = rs.getString("Detail");
                String price = rs.getString("Price");
                String buyerEmail = rs.getString("BuyerEmail");
                String category = rs.getString("Category");

                // 제목 및 내용 설정
                titleLabel = new JLabel("제목 : " + title);
                detailLabel = new JLabel("내용 : " + detail);

                if ("게시글".equals(category)) {
                    // 게시글 카테고리일 경우 가격, 구매자 정보, 버튼을 표시하지 않음
                    // 장터글 카테고리일 경우 가격, 구매자 정보, 구매 버튼 추가
                } else {
                    priceLabel = new JLabel("가격 : " + price);
                    buyerLabel = new JLabel("구매자 이메일 : " + (buyerEmail != null));
                    if(buyerEmail == null && !PostedEmail.equals(loggedInUsername)){
                        // 구매자가 없을 경우와 내가 작성한 장터글이 아닌경우에 구매 버튼을 표시하고 ActionListener 추가
                        purchaseButton = new JButton("구매하기");
                        purchaseButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    // 현재 사용자의 포인트 가져오는 SQL 문
                                    String getUserPointsSQL = "SELECT MemberPoint FROM members WHERE MemberEmail = ?";
                                    int userPoints = 0;

                                    try (PreparedStatement stmt = connection.prepareStatement(getUserPointsSQL)) {
                                        stmt.setString(1, loggedInUsername);
                                        try (ResultSet rs = stmt.executeQuery()) {
                                            if (rs.next()) {
                                                userPoints = rs.getInt("MemberPoint");
                                            }
                                        }
                                    }
                                    int itemPrice = Integer.parseInt(rs.getString("Price"));

                                    // 포인트가 충분하면 상품 구매 처리
                                    if (userPoints >= itemPrice) {
                                        updateMemberPoints(loggedInUsername, userPoints - itemPrice);
                                        marketPurchase(marketBoardID, loggedInUsername);

                                        JOptionPane.showMessageDialog(null, "구매가 완료되었습니다.");
                                        closeAndOpenNewWindow();
                                    } else {
                                        JOptionPane.showMessageDialog(null, "포인트가 충분하지 않습니다.");
                                    }
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        });
                        c.add(purchaseButton, BorderLayout.EAST);
                    }
                    // 장터글 카테고리의 경우 가격과 구매자 정보 추가
                    c.add(priceLabel, BorderLayout.SOUTH);
                    c.add(buyerLabel, BorderLayout.SOUTH);
                }

                // 제목과 내용 레이블 추가
                c.add(titleLabel, BorderLayout.NORTH);
                c.add(detailLabel, BorderLayout.CENTER);
            }

            // 닫기 버튼 추가 및 ActionListener 추가
            closeButton = new JButton("닫기");
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            c.add(closeButton, BorderLayout.SOUTH);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setTitle("게시글 상세 정보");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    // 회원 포인트 업데이트 메소드
    private void updateMemberPoints(String username, int newPoints) throws SQLException {
        String updatePointsSQL = "UPDATE members SET MemberPoint = ? WHERE MemberEmail = ?";
        stmt = connection.prepareStatement(updatePointsSQL);
        stmt.setInt(1, newPoints);
        stmt.setString(2, username);
        stmt.executeUpdate();
    }

    // 상품 구매 처리 메소드
    private void marketPurchase(String marketBoardID, String buyerEmail) throws SQLException {
        String markPurchaseSQL = "UPDATE marketandboard SET BuyerEmail = ? WHERE MarketBoardID = ?";
        stmt = connection.prepareStatement(markPurchaseSQL);
        stmt.setString(1, buyerEmail);
        stmt.setString(2, marketBoardID);
        stmt.executeUpdate();
    }

    // 현재 창 닫고 새로운 창 열기 메소드
    private void closeAndOpenNewWindow() {
        // 현재 MarketBoardDetail 창 닫기
        dispose();

        // 현재 MarketAndBoard 창 닫기
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof MarketAndBoard) {
                window.dispose();
            }
        }

        // 새로운 MarketAndBoard 창 열기 => 새로고침 처럼 사용함
        MarketAndBoard newMarketAndBoard = new MarketAndBoard(loggedInUsername, connection);
        newMarketAndBoard.setVisible(true);
    }
}