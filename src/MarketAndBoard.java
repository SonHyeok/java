import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarketAndBoard extends JFrame {
    private Container c;
    private JTable table;
    private JFrame topFrame;
    private String getMarketandBoardInfoSQL = "SELECT MarketBoardID, PostedEmail, Title, Detail, Price, BuyerEmail, Category FROM marketandboard;";
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel, titleText;
    private String loggedInUsername;
    private ResultSet rs;
    private PreparedStatement stmt;
    private Connection connection;
    private JButton DetailBtn, WriteBtn, EditBtn;
    JButton[] buttons = new JButton[12];

    //생성자로 GUI를 초기화 하는 메소드를 사용
    MarketAndBoard(String loggedInUsername, Connection connection) {
        this.loggedInUsername = loggedInUsername;
        this.connection = connection;
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {  // GUI 구성 요소 초기화
        UserPanelButtons userPanelButtons = new UserPanelButtons(loggedInUsername, connection);
        c = this.getContentPane();
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

        userPanelButtons.addLeftButtons(buttonPanel, buttons, topFrame);
        leftPanel.add(buttonPanel, BorderLayout.CENTER);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.BLACK);

        titleText = new JLabel("게시판 및 장터");
        titleText.setForeground(Color.WHITE);
        titleText.setBackground(Color.BLACK);
        titleText.setFont(new Font("맑은 고딕", Font.BOLD, 50));
        titleText.setHorizontalAlignment(JLabel.CENTER);

        rightPanel.add(titleText, BorderLayout.NORTH);

        c.add(rightPanel, BorderLayout.CENTER);
        // 게시판을 표시하는 메서드 호출
        showBoard(rightPanel);

        JPanel newButtonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        newButtonPanel.setBackground(Color.BLACK);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setTitle("헬스장 출입 관리 시스템 - 메인 페이지");
        setResizable(false);
        setLocationRelativeTo(null);
    }

    // 게시판 정보를 표시하는 메서드
    public void showBoard(JPanel rightPanel) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("번호");
        model.addColumn("게시글 유형");
        model.addColumn("제목");
        model.addColumn("내용");
        model.addColumn("가격");
        model.addColumn("상태");

        table = new JTable(model);
        DetailBtn = new JButton("자세히보기");
        WriteBtn = new JButton("작성하기");
        EditBtn = new JButton("수정하기");

        try {
            stmt = connection.prepareStatement(getMarketandBoardInfoSQL);
            rs = stmt.executeQuery();

            // 게시판 정보를 테이블에 추가하는 코드
            while (rs.next()) {
                String arr[] = new String[6];
                String marketBoardID = rs.getString("MarketBoardID");
                String title = rs.getString("Title");
                String detail = rs.getString("Detail");
                String price = rs.getString("Price");
                String buyerEmail = rs.getString("BuyerEmail");
                String category = rs.getString("Category");

                String info1 = null, info2 = null, info3 = null, info4 = null, info5 = null;
                if (category.equals("게시글")) {
                    info1 = category;
                    info2 = title;
                    info3 = detail;
                } else if (category.equals("장터글")) {
                    info1 = category;
                    info2 = title;
                    info3 = detail;
                    info4 = price;
                    info5 = buyerEmail;
                }
                // 정보를 배열에 저장하고 모델에 추가
                arr[0] = marketBoardID;
                arr[1] = info1;
                arr[2] = info2;
                arr[3] = info3;
                arr[4] = info4;
                if (info5 != null) {
                    arr[5] = "완료";
                } else {
                    arr[5] = info5;
                }
                model.addRow(arr);
            }

            rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(DetailBtn);
            buttonPanel.add(WriteBtn);
            buttonPanel.add(EditBtn);
            rightPanel.add(buttonPanel, BorderLayout.SOUTH);

            // DetailBtn의 액션 리스너 설정
            DetailBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) { // -1로 설정하여 사용자가 선택하지 않으면 게시글 자세히 보기를 할수 없도록 함
                        String selectedMarketBoardID = (String) table.getValueAt(selectedRow, 0);
                        new MarketBoardDetail(selectedMarketBoardID, connection, loggedInUsername);
                    }
                }
            });

            // WriteBtn의 액션 리스너 설정
            WriteBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writeNewPost();
                }
            });

            // EditBtn의 액션 리스너 설정
            EditBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 선택된 행의 정보를 가져와서 해당 게시물의 작성자 확인
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String selectedMarketBoardID = (String) table.getValueAt(selectedRow, 0);
                        String postAuthor = getPostAuthor(selectedMarketBoardID);

                        // 작성자가 현재 로그인한 사용자와 같으면 수정 가능하도록 처리
                        if (loggedInUsername.equals(postAuthor)) {
                            editPost(selectedMarketBoardID);
                        } else {
                            JOptionPane.showMessageDialog(topFrame, "본인의 글만 수정할 수 있습니다.");
                        }
                    }
                }
            });


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 새로운 게시물 작성 메소드
    private void writeNewPost() {
        // 새 게시물 작성을 위한 JDialog 생성
        JDialog writeDialog = new JDialog(topFrame, "글 작성하기", true);
        writeDialog.setSize(400, 300);
        writeDialog.setLayout(new GridLayout(7, 2));

        JTextField titleField = new JTextField();
        JTextField detailField = new JTextField();
        JTextField priceField = new JTextField();

        // 게시 유형 선택을 위한 라디오 버튼 추가
        JRadioButton postButton = new JRadioButton("게시글");
        JRadioButton marketButton = new JRadioButton("장터글");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(postButton);
        buttonGroup.add(marketButton);

        JButton submitButton = new JButton("작성 완료");

        writeDialog.add(new JLabel("게시 유형:"));
        writeDialog.add(postButton);
        writeDialog.add(new JLabel(""));
        writeDialog.add(marketButton);
        writeDialog.add(new JLabel("제목:"));
        writeDialog.add(titleField);
        writeDialog.add(new JLabel("내용:"));
        writeDialog.add(detailField);
        writeDialog.add(new JLabel("가격:"));
        writeDialog.add(priceField);
        writeDialog.add(submitButton);

        // radioListener 액션리스너 설정 통해 이용자 선택(게시글, 장터글)에 따라 가격 설정이 되고 안되고를 설정
        ActionListener radioListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (postButton.isSelected()) {

                    priceField.setEnabled(false);
                } else if (marketButton.isSelected()) {

                    priceField.setEnabled(true);
                }
            }
        };

        postButton.addActionListener(radioListener);
        marketButton.addActionListener(radioListener);

        // submitButton에 대한 Action 리스너
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 선택된 라디오 버튼 확인
                String category = marketButton.isSelected() ? "장터글" : "게시글";

                // 필드에서 값 가져오기
                String title = titleField.getText();
                String detail = detailField.getText();
                String value = priceField.getText();

                // 가격 필드가 비어있지 않은지 확인
                Integer price = null;
                if (!value.isEmpty()) {
                    try {
                        // 값을 Integer로 파싱 시도
                        price = Integer.parseInt(value);
                    } catch (NumberFormatException ex) {
                        // 입력이 유효한 정수가 아닌 경우 처리
                        ex.printStackTrace();
                        // 입력이 유효한 정수가 아닌 경우 처리
                        return;
                    }
                }

                // 게시물 유형에 따라 필요한 작업 수행
                savePostToDatabase(category, title, detail, price);

                writeDialog.dispose();
                topFrame.dispose();
                MarketAndBoard newMarketAndBoard = new MarketAndBoard(loggedInUsername, connection);
                newMarketAndBoard.setVisible(true);
            }
        });
        writeDialog.setVisible(true);
    }

    // 데이터베이스에 게시물 저장하는 메서드
    private void savePostToDatabase(String category, String title, String detail, Integer price) {
        try {
            // SQL 문 준비
            String sql = "INSERT INTO marketandboard (PostedEmail, Title, Detail, Price, Category) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, loggedInUsername);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, detail);
            if(category == "게시글"){
                price = null;
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            } else{
                preparedStatement.setInt(4, price);
            }
            preparedStatement.setString(5, category);

            // SQL 문 실행
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // getPostAuthor 메서드 구현
    private String getPostAuthor(String marketBoardID) {
        String author = null;
        try {
            String getAuthorSQL = "SELECT PostedEmail FROM marketandboard WHERE MarketBoardID = ?";
            PreparedStatement getAuthorStmt = connection.prepareStatement(getAuthorSQL);
            getAuthorStmt.setString(1, marketBoardID);
            ResultSet authorResult = getAuthorStmt.executeQuery();

            if (authorResult.next()) {
                author = authorResult.getString("PostedEmail");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return author;
    }

    // editPost 메서드 구현 사용자의 선택에 따라 글을 수정
    private void editPost(String marketBoardID) {
        JDialog editDialog = new JDialog(topFrame, "글 수정하기", true);
        editDialog.setSize(400, 300);
        editDialog.setLayout(new GridLayout(7, 2));

        JTextField titleField = new JTextField();
        JTextField detailField = new JTextField();
        JTextField priceField = new JTextField();

        JRadioButton postButton = new JRadioButton("게시글");
        JRadioButton marketButton = new JRadioButton("장터글");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(postButton);
        buttonGroup.add(marketButton);

        JButton submitButton = new JButton("수정 완료");

        editDialog.add(new JLabel("게시 유형:"));
        editDialog.add(postButton);
        editDialog.add(new JLabel(""));
        editDialog.add(marketButton);
        editDialog.add(new JLabel("제목:"));
        editDialog.add(titleField);
        editDialog.add(new JLabel("내용:"));
        editDialog.add(detailField);
        editDialog.add(new JLabel("가격:"));
        editDialog.add(priceField);
        editDialog.add(submitButton);

        ActionListener radioListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (postButton.isSelected()) {
                    priceField.setEnabled(false);
                } else if (marketButton.isSelected()) {
                    priceField.setEnabled(true);
                }
            }
        };

        postButton.addActionListener(radioListener);
        marketButton.addActionListener(radioListener);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 장터글인지 게시글인지에 따라 가격을 설정함 게시글일 경우 null 처리
                String category = marketButton.isSelected() ? "장터글" : "게시글";
                String title = titleField.getText();
                String detail = detailField.getText();
                String value = priceField.getText();

                Integer price = null;
                if (!value.isEmpty()) {
                    try {
                        price = Integer.parseInt(value);
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        return;
                    }
                }

                updatePostInDatabase(marketBoardID, category, title, detail, price);

                editDialog.dispose();
                topFrame.dispose();
                MarketAndBoard newMarketAndBoard = new MarketAndBoard(loggedInUsername, connection);
                newMarketAndBoard.setVisible(true);
            }
        });

        // 기존 게시물 내용을 가져와서 필드에 채움
        fillFieldsWithExistingContent(marketBoardID, titleField, detailField, priceField, postButton, marketButton);

        editDialog.setVisible(true);
    }

    // 데이터베이스에서 기존 게시물 내용을 가져와 필드에 채우는 메소드
    private void fillFieldsWithExistingContent(String marketBoardID, JTextField titleField, JTextField detailField,
                                               JTextField priceField, JRadioButton postButton, JRadioButton marketButton) {
        try {
            String getPostSQL = "SELECT Title, Detail, Price, Category FROM marketandboard WHERE MarketBoardID = ?";
            PreparedStatement getPostStmt = connection.prepareStatement(getPostSQL);
            getPostStmt.setString(1, marketBoardID);
            ResultSet postResult = getPostStmt.executeQuery();

            if (postResult.next()) {
                String title = postResult.getString("Title");
                String detail = postResult.getString("Detail");
                String price = postResult.getString("Price");
                String category = postResult.getString("Category");

                titleField.setText(title);
                detailField.setText(detail);

                if (category.equals("게시글")) {
                    postButton.setSelected(true);
                    priceField.setEnabled(false);
                } else if (category.equals("장터글")) {
                    marketButton.setSelected(true);
                    priceField.setText(price);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 데이터베이스에서 게시물을 업데이트하는 메소드
    private void updatePostInDatabase(String marketBoardID, String category, String title, String detail, Integer price) {
        try {
            String updateSQL = "UPDATE marketandboard SET Title = ?, Detail = ?, Price = ?, Category = ? WHERE MarketBoardID = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSQL);
            updateStmt.setString(1, title);
            updateStmt.setString(2, detail);
            if (category.equals("게시글")) {
                price = null;
                updateStmt.setNull(3, java.sql.Types.INTEGER);
            } else {
                updateStmt.setInt(3, price);
            }
            updateStmt.setString(4, category);
            updateStmt.setString(5, marketBoardID);

            updateStmt.executeUpdate();
            JOptionPane.showMessageDialog(topFrame, "게시물이 성공적으로 수정되었습니다.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(topFrame, "게시물 수정에 실패했습니다.");
        }
    }
}