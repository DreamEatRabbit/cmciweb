package com.cmci.common.service;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Service(value="com.cmci.common.service")
public class CommonService {

    public String testStr(String val) {
        return "ABCD";
    }

    public Map<String, Object> getTestObject(String userIdParam) {
        PreparedStatement pstmt = null;
        Connection conn = getConnTest();
        ResultSet rs = null;
        String query = "SELECT ID, USER_ID, USER_NAME, USER_PWD, DEPT_CODE, DEPT_NAME FROM cmcidb1.ST_COM_USER_INFO WHERE ID = ?";
        Map<String, Object> rMap = new HashMap<>();
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, userIdParam);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                rMap.put("id", rs.getString("id"));
                rMap.put("userId", rs.getString("user_id"));
                rMap.put("userName", rs.getString("user_name"));
                rMap.put("userPwd", rs.getString("user_pwd"));
            }

            rs.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } finally {
            try {if (rs!=null) rs.close();} catch (Exception e) {}
            try {if (pstmt!=null) pstmt.close();} catch (Exception e) {}
            try {if (conn != null) conn.close();} catch (Exception e) {}
        }
        return rMap;
    }

    public Connection getConnTest() {
        try {
            String dbURL = "jdbc:mysql://192.168.219.135:3306/cmcidb1";
            String dbID = "cmci_user1";
            String dbPW = "cmci_pwd1!";
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(dbURL, dbID, dbPW);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}