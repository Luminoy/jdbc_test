package OracleConnectionTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class JDBCUtils {
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

	public static boolean createOracleTable(Connection conn, String tName) {
		Statement statement = null;
		try {
			statement = conn.createStatement();
			statement.execute("create table " + tName + "("
					+ "id number(10) primary key not null,"
					+ "name varchar2(50)," + "sex varchar2(5),"
					+ "birthday date" + ")");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean insertDataIntoOracleTable(Connection conn,
			String tName, List<Object[]> lists) {
		PreparedStatement preStatement = null;
		try {
			preStatement = conn.prepareStatement("insert into " + tName
					+ "(id, name, sex, birthday) Values(?,?,?,?)");
			conn.setAutoCommit(false);
			int size = lists.size();
			Object[] obj = null;
			for (int i = 0; i < size; i++) {
				obj = lists.get(i);
				for (int j = 0; j < obj.length; j++) {
					preStatement.setObject(j + 1, obj[j]);
				}
				preStatement.addBatch();
			}

			preStatement.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
				conn.setAutoCommit(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}		
		return true;
	}

	public static boolean dropOracleTable(Connection conn, String tName) {
		Statement statement = null;
		try {
			statement = conn.createStatement();
			statement.execute("drop table " + tName);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
