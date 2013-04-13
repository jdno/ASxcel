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

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.jandavid.asxcel.model.Airport;
import de.jandavid.asxcel.model.Database;
import de.jandavid.asxcel.model.DatabaseResult;
import de.jandavid.asxcel.model.Enterprise;
import de.jandavid.asxcel.model.Model;
import de.jandavid.asxcel.model.Route;

/**
 * This is a test for the most important methods of the class 'Enterprise'.
 * Especially database operations regarding routes are tested.
 * 
 * @author jdno
 */
public class EnterpriseTest {

	/**
	 * This gets used by the tests.
	 */
	private Database db;
	
	/**
	 * This gets used by the tests.
	 */
	private Model model;
	
	/**
	 * This initiates the private attributes before every test. 
	 */
	@Before
	public void initializeModel() throws Exception {
		model = new Model("testDb.sqlite");
		model.loadEnterprise("TestEnterprise");
		db = model.getDatabase();
	}
	
	/**
	 * This tests the initialization process.
	 */
	@Test
	public void testInitialization() {
		Enterprise e = model.getEnterprise();
		
		assertEquals(1, e.getId());
		assertEquals(8, e.getRoutes().size());
	}
	
	/**
	 * This tests the creation of a route. 
	 */
	@Test
	public void testCreateRoute() throws SQLException {
		Airport origin = model.getAirport("TestAirport2");
		Airport destination = model.getAirport("TestAirport3");
		
		model.getEnterprise().createRoute(origin, destination);
		
		String query = "SELECT `id` FROM `routes` WHERE `origin` = '2' AND `destination` = '3'";
		DatabaseResult dr = db.executeQuery(query);
		assertTrue(dr.next());
		
		query = "DELETE FROM `routes` WHERE `id` = '" + dr.getInt(0) + "'";
		int rowCount = db.executeUpdate(query, new ArrayList<Object>(0));
		assertEquals(1, rowCount);
	}
	
	/**
	 * This tests the deletion of a route.
	 */
	@Test
	public void testDeleteRoute() throws SQLException {
		String query = "INSERT INTO `routes` (`origin`, `destination`, `enterprise`) VALUES ('2','3','1')";
		db.executeUpdate(query, new ArrayList<Object>(0));
		
		query = "SELECT `id` FROM `routes` WHERE `origin` = '2' AND `destination` = '3'";
		DatabaseResult dr = db.executeQuery(query);
		assertTrue(dr.next());
		
		model.getEnterprise().loadRoutes();
		ArrayList<Route> routes = model.getEnterprise().getRoutes();
		int id = 0;
		for(int i = 0; i < routes.size(); i++) {
			if(routes.get(i).getOrigin().getId() == 2 && routes.get(i).getDestination().getId() == 3) {
				id = i;
				break;
			}
		}
		model.getEnterprise().deleteRoute(id);
		
		query = "SELECT `id` FROM `routes` WHERE `origin` = '2' AND `destination` = '3'";
		dr = db.executeQuery(query);
		assertFalse(dr.next());
	}
}
