package com.bin.servlet;

import com.bin.bean.MyBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.*;

public class TestServlet1 extends TestServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        //commonJDBC();
        //JNDI();
        test();
        super.service(request, response);
    }

    private void test() {
        Context initCxt;
        try {
            initCxt = new InitialContext();
            MyBean myBean = (MyBean) initCxt.lookup("java:comp/env/bean/MyBeanFactory");
            System.out.println("netty:" + myBean.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void JNDI() {

        Context context;
        DataSource dataSource = null;
        try {
            context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/mysqlDataSource");
            System.out.println(dataSource == null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("select * from t_user");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commonJDBC() {
        System.out.println("##########TestServlet1###############");
        String url = "jdbc:mysql://127.0.0.1:3306/spring?characterEncoding-utf8&amp;useSSL=true";
        String userName = "root";
        String pwd = "1234567";
        String sql = "select * from t_user";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(url, userName, pwd); PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("ID"));
            }
            System.out.println("执行结果:" + preparedStatement.execute());
        } catch (SQLException ee) {
            ee.printStackTrace();
        }
    }
}
