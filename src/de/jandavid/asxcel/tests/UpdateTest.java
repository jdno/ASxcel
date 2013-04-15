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
package de.jandavid.asxcel.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.jandavid.asxcel.model.Database;
import de.jandavid.asxcel.model.DatabaseResult;

/**
 * @author jdno
 */
public class UpdateTest {
	
	private Database db;
	
	@Before
	public void initializeDatabase() throws ClassNotFoundException, SQLException {
		db = new Database("testDb.sqlite");
	}
	
	@Test
	public void testUpdate() throws SQLException {
		String query = "SELECT `value` FROM `meta_data` WHERE `key` = 'version'";
		DatabaseResult dr;
		
		try {
			dr = db.executeQuery(query);
		} catch (SQLException e) {
			query = "CREATE TABLE `meta_data` (`id` INTEGER PRIMARY KEY , `key` VARCHAR NOT NULL  UNIQUE , `value` VARCHAR NOT NULL )";
			db.executeUpdate(query, new ArrayList<Object>(0));
			
			query = "INSERT INTO `meta_data` (`key`, `value`) VALUES ('version', '1.0')";
			db.executeUpdate(query, new ArrayList<Object>(0));
			
			query = "SELECT `value` FROM `meta_data` WHERE `key` = 'version'";
			dr = db.executeQuery(query);
			
			assertTrue(dr.next());
			assertEquals("1.0", dr.getString(0));
		}
	}

}
