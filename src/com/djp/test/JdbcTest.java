package com.djp.test;

import com.djp.tools.JdbcTools;
import org.apache.commons.beanutils.BeanUtils;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JdbcTest {
    public static void main(String[] args) throws Exception {
        /*Driver driver = new com.mysql.jdbc.Driver();
        String url = "jdbc:mysql://localhost:3306/yun1.0";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "123");
        Connection connection = driver.connect(url,info);
        System.out.println(connection);*/
        String sql = "select * from students";
        System.out.println(getAll(sql));

    }

    // 先数据库中插入一条数据
    public static void update(String sql, Object... args) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = JdbcTools.getConnection2();
            // 填充占位符
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

    // 查询数据库中的一条数据
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
            // 获取表中的字段
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                student = Student.class.newInstance();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    String column_Name = rsmd.getColumnName(i + 1);
                    Object value = rs.getObject(column_Name);
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

    // 获取数据库中的多条数据
    public static List<Student> getAll(String sql, Object... args) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Student> studentList = new ArrayList<>();
        try {
            conn = JdbcTools.getConnection2();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                // 创建对象
                Student student = Student.class.newInstance();
                // 获取表中的字段名称和对应的值
                for (int i = 0; i <metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i + 1);
                    Object columnValue = rs.getObject(columnName);
                    // 给对象的属性赋值
                    BeanUtils.setProperty(student, columnName, columnValue);
                }
                studentList.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcTools.release(rs, ps, conn);
        }
        return studentList;

    }

}

