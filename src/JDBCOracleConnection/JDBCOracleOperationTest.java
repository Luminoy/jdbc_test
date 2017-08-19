package JDBCOracleConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class JDBCOracleOperationTest {

	public static void main(String[] args) {
		Connection conn = JDBCUtils.getOracleConnection();
		String tName = "XXTP_LUMIN_STUDENT_TL1";
		JDBCUtils.createOracleTable(conn, tName);

		// 插入
		JDBCUtils.insertDataIntoOracleTable(conn, tName);
		ResultSet resultSet = JDBCUtils.getOracleQueryResult(conn, tName);
		JDBCUtils.printResultSet(resultSet);

		// 更新
		JDBCUtils.updateDataFromOracleTable(conn, tName);
		ResultSet resultSet2 = JDBCUtils.getOracleQueryResult(conn, tName);
		JDBCUtils.printResultSet(resultSet2);
		
		JDBCUtils.deleteDataFromOracleTable(conn, tName);
		JDBCUtils.dropOracleTable(conn, tName);
		JDBCUtils.closeConnection(conn);
	}
}
