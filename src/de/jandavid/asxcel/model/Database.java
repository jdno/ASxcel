/**
 * This file is part of ASxcel.
 *
 * ASxcel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ASxcel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ASxcel.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.jandavid.asxcel.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class opens the connection to the database, creates a statement that
 * will be used by the modules to interact with the database, and closes
 * everything when the applications terminates.
 * 
 * @author jdno
 */
public class Database {
	
	/**
	 * The connection to the database
	 */
	private Connection connection;
	
	/**
	 * The database gets openend and a statement is created which can be
	 * used to interact with it.
	 * @throws ClassNotFoundException If the driver cannot be found this
	 * 		exception gets thrown.
	 * @throws SQLException If the statement cannot be created this
	 * 		exception gets thrown.
	 */
	public Database(String name) throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:" + name);
	}
	
	/**
	 * This method creates a PreparedStatement, executes it and returns the
	 * result in form of a DatabaseResult.
	 * @param query The query to execute
	 * @return The result as a DatabaseResult
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	public DatabaseResult executeQuery(String query) throws SQLException {
		PreparedStatement ps = this.connection.prepareStatement(query);
		
		DatabaseResult dr = new DatabaseResult(ps.executeQuery());
		
		ps.close();
		
		return dr;
	}
	
	/**
	 * This method creates a PreparedStatement, fills it with the given parameters, executes
	 * it and returns the result in form of a DatabaseResult.
	 * @param query The query to execute
	 * @param parameters The parameters to use
	 * @return The result as a DatabaseResult
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	public DatabaseResult executeQuery(String query, ArrayList<Object> parameters) throws SQLException {
		PreparedStatement ps = this.connection.prepareStatement(query);
		
		for(int i = 1; i <= parameters.size(); i++) {
			ps.setObject(i, parameters.get(i - 1));
		}
		
		DatabaseResult dr = new DatabaseResult(ps.executeQuery());
		
		ps.close();
		
		return dr;
	}
	
	/**
	 * This method creates a PreparedStatement, fills it with the given parameters, executes
	 * it and returns the result of the executeUpdate method.
	 * @param query The query to execute
	 * @param parameters The parameters to use
	 * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or 
	 * 		(2) 0 for SQL statements that return nothing
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	public int executeUpdate(String query, ArrayList<Object> parameters) throws SQLException {
		PreparedStatement ps = this.connection.prepareStatement(query);
		
		for(int i = 1; i <= parameters.size(); i++) {
			ps.setObject(i, parameters.get(i - 1));
		}
		
		int result = ps.executeUpdate();
		
		ps.close();
		
		return result;
	}

	/**
	 * Close the connection to the database.
	 * @throws SQLException If the statement or connection cannot be closed
	 * 		this gets thrown.
	 */
	public void closeConnection() throws SQLException {
		connection.close();
	}
	
	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

}
