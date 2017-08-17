package OracleConnectionTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectOracleByJDBC {

	public static Connection getOracleConnection() {
		Connection connection = null;

		String url = "jdbc:oracle:thin:@172.29.41.138:1521:DEMO";
		String uname = "scott";
		String upwd = "tiger";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(url, uname, upwd);

		} catch (ClassNotFoundException e) {
			System.out.println("driver not found!");
			// e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("get connection error!");
			// e.printStackTrace();
		}
		if (connection == null) {
			System.out.println("connection failed!");
			return null;
		} else {
			System.out.println("connection succeed!");
			return connection;
		}
	}

	public static ResultSet getOracleQueryResult(Connection conn, String sql) {
		Statement statement = null;
		ResultSet resultset = null;

		if (conn != null) {
			try {
				statement = conn.createStatement(
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				resultset = statement.executeQuery(sql);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultset;
	}
	
	public static void main(String[] args) {
		Connection conn = getOracleConnection();
		String sql = "select deptno, dname from dept";
		ResultSet resultSet = getOracleQueryResult(conn, sql);
		String dno = null;
		String dname = null;
		System.out.println("顺序查：");
		try {
			while (resultSet.next()) {
				dno = resultSet.getString(1);
				dname = resultSet.getString(2);
				System.out.println(dno + ": " + dname);
			}
		
		System.out.println("逆序查：");
		while(resultSet.previous()) {
			dno = resultSet.getString(1);
			dname = resultSet.getString(2);
			System.out.println(dno + ": " + dname);
			}
		conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
