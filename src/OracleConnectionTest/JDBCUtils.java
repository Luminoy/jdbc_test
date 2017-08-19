package OracleConnectionTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.List;

public class JDBCUtils {
	private static String dbUrl = "jdbc:oracle:thin:@172.29.41.138:1521:DEMO";
	private static String dbUserName = "scott";
	private static String dbPassword = "tiger";

	// 获取数据库连接
	public static Connection getOracleConnection() {
		Connection connection = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(dbUrl, dbUserName,
					dbPassword);

		} catch (ClassNotFoundException e) {
			System.out.println("数据库类名查找失败！");
		} catch (SQLException e) {
			System.out.println("数据库连接参数错误！");
		}
		if (connection == null) {
			System.out.println("数据库连接失败！");
			return null;
		} else {
			System.out.println("数据库连接成功！");
			return connection;
		}
	}

	// 建立student表
	public static boolean createOracleTable(Connection conn, String tName) {
		Statement statement = null;
		try {
			statement = conn.createStatement();
			statement.executeUpdate("create table " + tName + "("
					+ "id number(10) primary key not null,"
					+ "name varchar2(50)," + "sex varchar2(10),"
					+ "birthday date" + ")");
		} catch (SQLException e) {
			System.out.println("创建表出错！");
			return false;
		}
		System.out.println("创建表成功！");
		return true;
	}

	// 插入若干行数据
	public static boolean insertDataIntoOracleTable(Connection conn,
			List dataList, String tName) {
		PreparedStatement preStatement = null;
		Savepoint sp = null;
		try {
			if (dataList == null) {
				System.out.println("插入数据源为空！");
				return false;
			}
			// 关闭自动提交事务设置
			conn.setAutoCommit(false);
			// 设置一个存储点
			sp = conn.setSavepoint();

			preStatement = conn.prepareStatement("insert into " + tName
					+ "(id, name, sex, birthday) Values(?,?,?,?)");

			// 设置插入数据
			for (int i = 0; i < dataList.size(); i++) {
				Object[] objs = (Object[]) dataList.get(i);
				for (int j = 0; j < objs.length; j++) {
					preStatement.setObject(j + 1, objs[j]);
				}
				preStatement.executeUpdate();
			}

			// 手动提交事务
			conn.commit();
			// 恢复自动提交事务设置
			conn.setAutoCommit(true);
		} catch (Exception e) {
			System.out.println(tName + "表数据插入过程中出错！");
			System.out.println("出错信息：" + e.getMessage());
			try {
				System.out.println("事务回滚至存储点...");
				// 回滚
				conn.rollback(sp);
				preStatement.close();
				conn.setAutoCommit(true);
				System.out.println("回滚成功！");
			} catch (SQLException e1) {
				System.out.println("回滚失败！");
			}
			return false;
		}
		return true;
	}

	// 删除所有数据
	public static boolean deleteDataFromOracleTable(Connection conn,
			String tName) {
		PreparedStatement preStatement = null;
		Savepoint sp = null;
		try {
			// 关闭自动提交事务设置
			conn.setAutoCommit(false);
			// 设置一个存储点
			sp = conn.setSavepoint();

			preStatement = conn.prepareStatement("delete from " + tName);
			preStatement.executeUpdate();

			// 手动提交事务
			conn.commit();
			// 恢复自动提交事务设置
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			System.out.println(tName + "表数据删除过程中出错！");
			System.out.println("出错信息：" + e.getLocalizedMessage());
			try {
				System.out.println("事务回滚至存储点...");
				// 回滚
				conn.rollback(sp);
				preStatement.close();
				conn.setAutoCommit(true);
				System.out.println("回滚成功！");
			} catch (SQLException e1) {
				System.out.println("回滚失败！");
			}
			return false;
		}
		return true;
	}

	// 查询数据，获得可滚动的结果集
	public static ResultSet getOracleQueryResult(Connection conn, String tName) {
		Statement statement = null;
		ResultSet resultset = null;
		String sql = "select * from " + tName;
		if (conn != null) {
			try {
				// 设置可滚动结果集
				statement = conn.createStatement(
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				resultset = statement.executeQuery(sql);

			} catch (SQLException e) {
				System.out.println("查询表出错！");
			}
		}
		return resultset;
	}

	// 删除表
	public static boolean dropOracleTable(Connection conn, String tName) {
		Statement statement = null;
		try {
			statement = conn.createStatement();
			statement.execute("drop table " + tName);
			System.out.println(tName + "表已删除！");
		} catch (SQLException e) {
			System.out.println(tName + "表删除失败！");
			System.out.println("出错信息：" + e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	// 打印ResultSet结果集
	public static void printResultSet(ResultSet resultSet) {
		if (resultSet == null) {
			System.out.println("结果集为空");
			return;
		}
		String dno = null;
		String dname = null;

		try {
			System.out.println("顺序查：");
			while (resultSet.next()) {
				dno = resultSet.getString(1);
				dname = resultSet.getString(2);
				System.out.println(dno + ": " + dname);
			}

			System.out.println("逆序查：");
			while (resultSet.previous()) {
				dno = resultSet.getString(1);
				dname = resultSet.getString(2);
				System.out.println(dno + ": " + dname);
			}
		} catch (SQLException e) {
			System.out.println("打印ResultSet出错！");
		}
	}

	// 关闭连接
	public static void closeConnection(Connection conn) {
		try {
			if (conn == null) {
				return;
			}
			conn.close();
			System.out.println("数据库连接已关闭");
		} catch (SQLException e) {
			System.out.println("关闭数据库连接出错！");
		}
	}
}
