
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//DB 연결 설정 및 해제
public class DBConnManager {
    static String url = "jdbc:mysql://127.0.0.1:3306/java";
    static String user = "root"; // 여러분 계정 이름으로 수정
    static String pswd = "0000"; // 여러분 비밀번호로 수정
    Connection connection;

    // DB 연결 설정
    public void createDbConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,user,pswd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
