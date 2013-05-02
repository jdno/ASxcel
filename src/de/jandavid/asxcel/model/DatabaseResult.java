/**
 * ASxcel - A small tool for the browser game AirlineSim
 *
 * Copyright 2013 jdno <https://github.com/jdno/>
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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class is the equivalent to a ResultSet. It exists to solve the issue 
 * that a ResultSet gets closed if the statement which created the ResultSet
 * gets closed. Because of this behavior it was impossible to process a ResultSet
 * in a module, which made this class necessary.
 * 
 * @author jdno
 */
public class DatabaseResult {
	
	/**
	 * This points to row
	 */
	private int iterator = -1;
	
	/**
	 * This list contains the columns' names
	 */
	private LinkedList<String> columnNames = new LinkedList<String>();
	
	/**
	 * This list contains the rows, which itself consist of a list
	 */
	private ArrayList<ArrayList<String>> content = new ArrayList<ArrayList<String>>();
	
	/**
	 * A DatabaseResult is built from a ResultSet, and contains most but
	 * not all of its information. Other than the ResultSet a DatabaseResult
	 * does not get lost if a SQL statement gets closed, and should therefore
	 * be used to hand data from a query over to a module.
	 * @param rs The ResultSet to process
	 * @throws SQLException If the ResultSet cannot be processed correctly this
	 * 		exception gets thrown.
	 */
	public DatabaseResult(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		
		for(int i = 1; i <= rsmd.getColumnCount(); i++) {
			columnNames.add(rsmd.getColumnName(i));
		}
		
		while(rs.next()) {
			ArrayList<String> column = new ArrayList<String>(rsmd.getColumnCount());		
			for(int i = 1; i <= rsmd.getColumnCount(); i++) {
				column.add(rs.getString(i));
			}
			content.add(column);
		}
	}
	
	/**
	 * This method moves the pointer behind the last row.
	 */
	public void afterLast() {
		iterator = getRowCount() - 1;
	}
	
	/**
	 * This method moves the pointer in front of the first row.
	 */
	public void beforeFirst() {
		iterator = -1;
	}
	
	/**
	 * Get the column count.
	 * @return The column count
	 */
	public int getColumnCount() {
		return columnNames.size();
	}
	
	/**
	 * Get the name of a specific column.
	 * @param column The index of the column
	 * @return The name of the column
	 */
	public String getColumnName(int column) {
		return columnNames.get(column);
	}
	
	/**
	 * Get the row count.
	 * @return The row count
	 */
	public int getRowCount() {
		return content.size();
	}
	
	/**
	 * Get the content of a cell as an Integer.
	 * @param column The column in the current row
	 * @return The content as an Integer
	 */
	public int getInt(int column) {
		return Integer.valueOf(getString(column));
	}
	
	/**
	 * Get the content of a cell as a String.
	 * @param column The column in the current row
	 * @return The content as a String
	 */
	public String getString(int column) {
		if(iterator > -1 && iterator < getRowCount() && column > -1 && column < getColumnCount()) {
			return content.get(iterator).get(column);
		} else {
			return null;
		}
	}
	
	/**
	 * Get the content of a cell by the column's name.
	 * @param columnName The name of the column
	 * @return The content of the column with the given name, or null
	 * 		if no such column name exists
	 */
	public String getString(String columnName) {
		int column = columnByName(columnName);
		
		if(column < getColumnCount() && column >= 0) {
			return getString(column);
		} else {
			return null;
		}
	}
	
	/**
	 * This method moves the pointer one element ahead and returns true
	 * if this is possible, or it returnes false if no next element exists.
	 * @return True if a next element exists, false otherwise
	 */
	public boolean next() {
		if(iterator < getRowCount() - 1) {
			iterator++;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Get the index of a column by its name.
	 * @param columnName The name of the column
	 * @return The index of the column or -1 if no colum with the given name
	 * 		exits.
	 */
	private int columnByName(String columnName) {
		for(int i = 0; i < columnNames.size(); i++) {
			if(columnNames.get(i).equals(columnName)) {
				return i;
			}
		}
		
		return -1;
	}
	
}
