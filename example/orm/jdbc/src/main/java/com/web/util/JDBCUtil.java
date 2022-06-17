package com.web.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JDBCUtil {

    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

    private static final String URL = "jdbc:mysql://192.168.221.129:3306/test?useUnicode=true&characterEncoding=utf-8";

    private static final String USER_NAME = "root";

    private static final String PASSWORD = "123";

    /**
     * 加载驱动
     */
    static {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        return connection;
    }

    /**
     * 预编译
     * @param sql
     * @return
     * @throws Exception
     */
    public static PreparedStatement getPreparedStatement(String sql, List<Object> params) throws Exception {
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        for (int i = 0;i < params.size();i++) {
            Object o = params.get(i);
            if (o instanceof String) {
                preparedStatement.setString(i, (String)o);
            } else if (o instanceof Integer) {
                preparedStatement.setInt(i, (int)o);
            }
        }
        return preparedStatement;
    }

    /**
     * 获取结果
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public static ResultSet getResultSet(String sql, List<Object> params) throws Exception {
        ResultSet resultSet = getPreparedStatement(sql, params).executeQuery();
        return resultSet;
    }
}
