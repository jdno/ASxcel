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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This application has the purpose to help the user manage
 * his enterprise by providing him with a new way to collect
 * and view data. This class is responsible for collecting
 * all information the user enters about his enterprise. 
 * 
 * @author jdno
 */
public class Enterprise {
	
	/**
	 * Where the enterprise's main office is located.
	 */
	private Country country;
	
	/**
	 * This is the enterprise's ID in the database.
	 */
	private int id;
	
	/**
	 * The model is needed to access the database.
	 */
	private Model model;
	
	/**
	 * This is the name the user gave his enterprise.
	 */
	private String name;
	
	/**
	 * This is a list of routes a user has established or
	 * is planning to introduce.
	 */
	private ArrayList<Route> routes = new ArrayList<Route>();
	
	/**
	 * An enterprise is the logical collection of the user's
	 * information. It is identified by its name.
	 * @param name The (unique) name of the enterprise.
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	public Enterprise(Model model, String name) throws SQLException, Exception {
		this.model = model;
		this.name = name;
		
		syncWithDb();
	}
	
	/**
	 * This method creates a new route and adds it to the list of routes.
	 * @param origin The airport where the route starts.
	 * @param destination The airport where the route ends.
	 * @return The newly created route.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public Route createRoute(Airport origin, Airport destination) throws SQLException {
		for(Route r: routes) {
			if(r.getOrigin().compareTo(origin) == 0) {
				if(r.getDestination().compareTo(destination) == 0) {
					return r;
				}
			}
		}
		
		Route r = new Route(model, origin, destination);
		
		routes.add(r);
		
		return r;
	}
	
	/**
	 * This method loads all routes belonging to the current enterprise
	 * from the database.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void loadRoutes() throws SQLException {
		String query = "SELECT `a1`.`name` AS `origin`, `a2`.`name` AS `destination` FROM `routes` AS `r` " +
				"INNER JOIN `airports` AS `a1` ON `r`.`origin` = `a1`.`id` " +
				"INNER JOIN `airports` AS `a2` ON `r`.`destination` = `a2`.`id` " +
				"WHERE `r`.`enterprise` = '" + id + "'";
		
		DatabaseResult dr = model.getDatabase().executeQuery(query);
		
		while(dr.next()) {
			Airport origin = new Airport(model, dr.getString(0));
			Airport destination = new Airport(model, dr.getString(1));
			
			Route r = new Route(model, origin, destination);
			
			routes.add(r);
		}
		
		Collections.sort(routes);
	}
	
	/**
	 * This method synchronizes an enterprise with the database, assuming
	 * the enterprise has been created already. If this is the case this 
	 * instance of Enterprise gets filled with its data, else a new enterprise
	 * gets added to the database with the given name.
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	private void syncWithDb() throws SQLException, Exception {
		String query = "SELECT `e`.`id`, `e`.`name`, `c`.`name` FROM `enterprises` AS `e` " +
				"INNER JOIN `countries` AS `c` ON `e`.`country` = `c`.`id` " +
				"WHERE `e`.`name` = ? LIMIT 1";
		ArrayList<Object> params = new ArrayList<Object>(1);
		params.add(name);
		
		DatabaseResult dr = model.getDatabase().executeQuery(query, params);
		
		if(dr.next()) {
			id = dr.getInt(0);
			name = dr.getString(1);
			country = new Country(model, dr.getString(2));
			
			loadRoutes();
		} else {
			throw new Exception("Enterprise was not found");
		}
	}

	/**
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the routes
	 */
	public ArrayList<Route> getRoutes() {
		return routes;
	}

}