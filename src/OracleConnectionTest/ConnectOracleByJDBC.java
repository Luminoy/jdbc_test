package OracleConnectionTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class ConnectOracleByJDBC {

	public static List getData() {
		List dataList = null;

		try {
			dataList = new LinkedList<Object[]>();
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			dataList.add(new Object[] { 450001, "Kobe.Bryant", "male",
					sdf.parse("1977-08-24") });
			dataList.add(new Object[] { 450002, "Michael.Jordan", "male",
					sdf.parse("1962-04-23") });
			dataList.add(new Object[] { 450005, "James.Laurance", "female",
					sdf.parse("1987-11-12") });
			dataList.add(new Object[] { 450006, "JR.Smith", "male",
					sdf.parse("1985-07-07") });
		} catch (ParseException e) {
			System.out.println("日期解析出错！");
		}

		return dataList;
	}

	public static void main(String[] args) {
		Connection conn = JDBCUtils.getOracleConnection();
		String tName = "XXTP_LUMIN_STUDENT_TL1";
		JDBCUtils.createOracleTable(conn, tName);

		List dataList = getData();
		JDBCUtils.insertDataIntoOracleTable(conn, dataList, tName);
		ResultSet resultSet = JDBCUtils.getOracleQueryResult(conn, tName);
		JDBCUtils.printResultSet(resultSet);

		JDBCUtils.deleteDataFromOracleTable(conn, tName);
		JDBCUtils.dropOracleTable(conn, tName);
		JDBCUtils.closeConnection(conn);
	}
}
