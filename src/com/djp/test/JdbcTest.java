package com.djp.test;

import com.djp.tools.JdbcTools;
import org.apache.commons.beanutils.BeanUtils;


import java.sql.*;


public class JdbcTest {
    public static void main(String[] args) throws Exception {
        /*Driver driver = new com.mysql.jdbc.Driver();
        String url = "jdbc:mysql://localhost:3306/yun1.0";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "123");
        Connection connection = driver.connect(url,info);
        System.out.println(connection);*/
        System.out.println(testResultSetMetaData());
    }

    // 先数据库中插入一条数据
    public static void update(String sql, Object... args) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = JdbcTools.getConnection2();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcTools.release(null, ps, connection);
        }

    }

    // 数据库中查询一条数据
    public static Student query(String sql, Object... args) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Student student = null;
        try {
            conn = JdbcTools.getConnection2();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                student = new Student(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("age"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JdbcTools.release(rs, ps, conn);
        }

        return student;
    }

    // 获取表中的字段
    public static Student testResultSetMetaData() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Student student = null;
        try {
            conn = JdbcTools.getConnection2();
            ps = conn.prepareStatement("select * from students where id =?");
            ps.setInt(1, 1);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    String column_Name = rsmd.getColumnName(i+1);
                    Object value = rs.getObject(column_Name);
                    student = Student.class.newInstance();
                    BeanUtils.setProperty(student,column_Name,value);

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcTools.release(rs, ps, conn);
        }
        return student;
    }

}
