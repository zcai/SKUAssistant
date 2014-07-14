package com.taotu51.topclient.miscellaneous;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseInventoryManager {

	public static String getImagePath(int itemID) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return null;
		}
	 
		System.out.println("MySQL JDBC Driver Registered!");
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = DriverManager
			.getConnection("jdbc:mysql://localhost:3306/test","root", "9707034");
			statement = connection.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement.executeQuery("select ITEM_IMAGE,PURCHASE_STORE,ITEM_DESCRIPTION from iteminfo where ITEM_ITEMUNIQID="+itemID);	 
			System.out.println(resultSet.getFetchSize()+"fetch size---------");
			if (resultSet.first()){
				String img = resultSet.getString("ITEM_IMAGE");
				System.out.println(img+"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				String store = resultSet.getString("PURCHASE_STORE");
				System.out.println(store+"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				String desc = resultSet.getString("ITEM_DESCRIPTION");
				return img+"#:#"+store+"#:#"+desc;
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			System.out.println("Connection Failed!");
			e.printStackTrace();
			return null;
		}

	}
}
