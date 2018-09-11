package com.djp.tools;



import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JdbcTools {
    // 获取数据库连接
    public static Connection getConnection2() throws Exception{

        // 读取类路径下的文件
        InputStream in = JdbcTools.class.getClassLoader().getResourceAsStream("db.properties");
        // 加载流到properties
        Properties properties = new Properties();
        properties.load(in);
        // 从pro中获取内容
        String driverClass = properties.getProperty("driver");
        String jdbcUrl = properties.getProperty("jdbcUrl");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        // 加载驱动
        Class.forName(driverClass);
        // 获取连接
        return DriverManager.getConnection(jdbcUrl, user, password);
    }
    // 关闭资源
    public static void release(ResultSet resultSet, Statement statement, Connection connection)
            throws Exception {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
