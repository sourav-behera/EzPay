package com.nwg.ezpay.repository;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Connection;

public class JDBCTest {
	public static void main(String[] args) {
		String url = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
		String username = "dev";
		String password = "dev";
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			if (conn != null) {
				System.out.println("Connection successful");
			} else {
				System.out.println("Failed to connect to database");
			}
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM transaction_tab");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				String id = resultSet.getString(1);
				String type = resultSet.getString(2);
				Double amount = resultSet.getDouble(3);
				String status = resultSet.getString(4);
				Date date = resultSet.getDate(5);
				System.out.printf("%s \t %s \t %.2f \t %s \t %s\n", id, type, amount, status, date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
