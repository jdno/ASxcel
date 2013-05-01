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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.jandavid.asxcel.model.Database;
import de.jandavid.asxcel.model.DatabaseResult;

/**
 * This test initializes a database, and test primitive read and write access by simply
 * executing some queries in the it.
 * 
 * @author jdno
 */
public class DatabaseTest {
	
	/**
	 * This instance gets used by the tests.
	 */
	private Database db;
	
	@Before
	public void initializeDatabase() throws ClassNotFoundException, SQLException {
		db = new Database("resources" + File.separator + "testDb.sqlite");
	}

	/**
	 * This test simply initializes the test database.
	 */
	@Test
	public void testInitialization() throws ClassNotFoundException, SQLException {
		db = new Database("resources" + File.separator + "testDb.sqlite");
	}

	/**
	 * This tests performs a simple read operation by checking the name of the first
	 * enterprise in the table.
	 */
	@Test
	public void testReadAccess() throws SQLException {
		String query = "SELECT `name` FROM `enterprises` WHERE `id` = '1' LIMIT 1";
		
		DatabaseResult dr = db.executeQuery(query);
		
		assertTrue(dr.next());
		assertEquals("TestEnterprise", dr.getString(0));
	}

	/**
	 * This tests tries a write operation, and creates, reads and deletes a new enterprise.
	 */
	@Test
	public void testWriteAccess() throws SQLException {
		String query = "INSERT INTO `enterprises` (`name`, `airport`) VALUES ('TestEnterprise2', '1')";
		db.executeUpdate(query, new ArrayList<Object>(0));
		
		query = "SELECT `name` FROM `enterprises` WHERE `name` = 'TestEnterprise2' LIMIT 1";
		DatabaseResult dr = db.executeQuery(query);
		assertTrue(dr.next());
		
		query = "DELETE FROM `enterprises` WHERE `name` = 'TestEnterprise2'";
		int rowCount = db.executeUpdate(query, new ArrayList<Object>(0));
		assertEquals(1, rowCount);
		
		query = "SELECT `name` FROM `enterprises` WHERE `name` = 'TestEnterprise2' LIMIT 1";
		dr = db.executeQuery(query);
		assertFalse(dr.next());
	}
	
}
