package OracleConnectionTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectOracleByJDBC {

	public static void main(String[] args) {
		Connection conn = JDBCUtils.getOracleConnection();
		String tbName = "xxtp_lumin_student_tl";
		JDBCUtils.createOracleTable(conn, tbName);
		String sql = "select * from " + tbName;
		ResultSet resultSet = JDBCUtils.getOracleQueryResult(conn, sql);
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
			e.printStackTrace();
		} 
	}
}
