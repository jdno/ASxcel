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
 * The Model coordinates everything that is related to data and how it
 * is handled in the application. It connects to a database and manages
 * the enterprise the user is currently working with. 
 * 
 * @author jdno
 */
public class Model {
	
	/**
	 * This list contains all airports.
	 */
	private ArrayList<Airport> airports = new ArrayList<Airport>();
	
	/**
	 * This list contains all countries.
	 */
	private ArrayList<Country> countries = new ArrayList<Country>();

	/**
	 * The SQLite DB gets accessed through this instance of Database.
	 */
	private Database database;
	
	/**
	 * An enterprise holds all information the user is working on, e.g.
	 * routes and their characteristics.
	 */
	private Enterprise enterprise;
	
	/**
	 * The model connects to the database while initialization, and throws
	 * and exception if that fails. After opening the database no further
	 * steps are taken. It is the task of the controller to initialize
	 * loading an enterprise.
	 * @throws ClassNotFoundException If the SQLite driver cannot be found
	 * 		this gets thrown.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public Model(String databaseName) throws ClassNotFoundException, SQLException {
		database = new Database(databaseName);
	}
	
	/**
	 * This method creates a new airport.
	 * @param name The name of the new airport.
	 * @return The newly created airport.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public Airport createAirport(String name) throws SQLException {
		if(getAirport(name) == null) {
			Airport a = new Airport(this, name);
			
			airports.add(a);
			Collections.sort(airports);
			
			return a;
			
		} else {
			return getAirport(name);
		}
	}
	
	/**
	 * This method creates a new country.
	 * @param name The name of the new country.
	 * @return The newly created country.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public Country createCountry(String name) throws SQLException {
		if(getCountry(name) == null) {
			Country c = new Country(this, name);
			
			countries.add(c);
			Collections.sort(countries);
			
			return c;
		} else {
			return getCountry(name);
		}
	}
	
	/**
	 * This method creates a new enterprise, which in the next step can be
	 * loaded using the appropriate method of this class.
	 * @param name The name of the new enterprise.
	 * @param countryName The name of the enterprise's country.
	 * @throws Exception If the enterprise cannot be created this gets thrown.
	 */
	public void createEnterprise(String name, String airportName) throws Exception {
		String query = "INSERT OR IGNORE INTO `airports` (`name`) VALUES (?)";
		ArrayList<Object> params = new ArrayList<Object>(1);
		params.add(airportName);
		database.executeUpdate(query, params);
		
		params.clear();
		query = "INSERT INTO `enterprises` (`name`, `airport`) SELECT ? AS `enterprise`, `id` FROM " +
				"`airports` WHERE `name` = ?";
		params.add(name);
		params.add(airportName);
		database.executeUpdate(query, params);
		
		params.clear();
		query = "INSERT INTO `enterprise_has_airport` (`enterprise`, `airport`) " +
				"SELECT `e`.`id`, `a`.`id` FROM `enterprises` AS `e` " +
				"INNER JOIN `airports` AS `a` ON `e`.`airport` = `a`.`id` " +
				"WHERE `e`.`name` = ?";
		params.add(name);
		database.executeUpdate(query, params);
		
		loadEnterprise(name);
	}
	
	/**
	 * This method creates a new route with the given origin and destination.
	 * @param origin The airport the route starts at.
	 * @param destination The airport the route ends at.
	 * @return The newly created route.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public Route createRoute(Airport origin, Airport destination) throws SQLException {
		return enterprise.createRoute(origin, destination);
	}
	
	/**
	 * This method removes an airport by first triggering the removal of
	 * all connected routes and then deleting the airport itself from the
	 * database.
	 * @param airportName The name of the airport to be removed.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void deleteAirport(String airportName) throws SQLException {
		Airport airport = getAirport(airportName);

		String query = "DELETE FROM `airports` WHERE `id` = ?";
		ArrayList<Object> params = new ArrayList<Object>(2);
		params.add(airport.getId());
		
		database.executeUpdate(query, params);
		
		airports.remove(airport);
	}
	
	/**
	 * This method deletes an enterprise. Please take care that the currently active
	 * enterprise does not get deleted! An enterprise gets deleted by removing all
	 * information it has added to the database, most importantly its routes.
	 * @param enterpriseName The name of the enterprise to delete.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void deleteEnterprise(String enterpriseName) throws SQLException {
		String query = "SELECT `id` FROM `enterprises` WHERE `name` = '" + enterpriseName + "'";
		DatabaseResult dr = database.executeQuery(query);
		
		if(dr.next()) {
			int enterprise = dr.getInt(0);
			
			query = "DELETE FROM `enterprise_has_airport` WHERE `enterprise` = '" + enterprise + "'";
			database.executeUpdate(query);
			
			query = "DELETE FROM `routes` WHERE `enterprise` = '" + enterprise + "'";
			database.executeUpdate(query);
			
			query = "DELETE FROM `enterprises` WHERE `id` = '" + enterprise + "'";
			database.executeUpdate(query);
		}
	}
	
	/**
	 * This method loads all static data the Model needs to work.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void initializeModel() throws SQLException {
		loadCountries();
	}
	
	/**
	 * This method loads an airport from the database, and adds it to the list
	 * of available airports.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void loadAirports(int enterprise) throws SQLException {
		airports.clear();
		
		String query = "SELECT `a`.`id`, `a`.`name`, `a`.`iata`, `a`.`passengers`, " +
				"`a`.`cargo`, `a`.`size`, `a`.`transfer`, `c`.`name` FROM `airports` AS `a` " +
				"INNER JOIN `countries` AS `c` ON `a`.`country` = `c`.`id`" +
				"INNER JOIN `enterprise_has_airport` AS `e` ON `a`.`id` = `e`.`airport`" +
				"WHERE `e`.`enterprise` = '" + enterprise + "'";
		
		DatabaseResult dr = database.executeQuery(query);
		
		int id, pax, cargo;
		String name, iata, size;
		Country country;
		boolean transferPossible;
		
		while(dr.next()) {
			id = dr.getInt(0);
			name = dr.getString(1);
			iata = dr.getString(2);
			pax = dr.getInt(3);
			cargo = dr.getInt(4);
			size = dr.getString(5);
			transferPossible = dr.getInt(6) == 1 ? true : false;
			country = getCountry(dr.getString(7));
			
			airports.add(new Airport(this, id, name, country, iata, size, pax, cargo, transferPossible));
		}
		
		Collections.sort(airports);
	}
	
	/**
	 * This methods loads all existing countries from the database.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void loadCountries() throws SQLException {
		countries.clear();
		
		String query = "SELECT * FROM `countries`";
		
		DatabaseResult dr = database.executeQuery(query);
		
		Country c;
		while(dr.next()) {
			c = new Country(this, dr.getInt(0), dr.getString(1));
			countries.add(c);
		}
		
		Collections.sort(countries);
	}
	
	/**
	 * This method loads a the enterprise with the given name. An exception (plain!)
	 * gets thrown if the enterprise does not exists!
	 * @param name The name of the enterprise to load.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 * @throws Exception If the enterprise does not exists this gets thrown.
	 */
	public void loadEnterprise(String name) throws SQLException, Exception {
		enterprise = new Enterprise(this, name);
	}
	
	/**
	 * Get an airport by its name. Returns null if airport does not exist.
	 * @param name The name of the airport to look for.
	 * @return The airport with the given name or null.
	 */
	public Airport getAirport(String name) {
		for(Airport a: airports) {
			if(a.getName().equals(name)) return a;
		}
		
		return null;
	}
	
	/**
	 * Get a country by its name. Returns null if the country does not exist.
	 * @param name The name of the country to look for.
	 * @return The country with the given name or null.
	 */
	public Country getCountry(String name) {
		for(Country c: countries) {
			if(c.getName().equals(name)) return c;
		}
		
		return null;
	}
	
	/**
	 * @return the airports
	 */
	public ArrayList<Airport> getAirports() {
		return airports;
	}

	/**
	 * @return the countries
	 */
	public ArrayList<Country> getCountries() {
		return countries;
	}

	/**
	 * @return the database
	 */
	public Database getDatabase() {
		return database;
	}

	/**
	 * @return the enterprise
	 */
	public Enterprise getEnterprise() {
		return enterprise;
	}
}
