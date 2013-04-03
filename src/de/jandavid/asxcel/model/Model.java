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
	public Model() throws ClassNotFoundException, SQLException {
		database = new Database();
		
		loadAirports();
		loadCountries();
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
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void createEnterprise(String name, String airportName) throws SQLException {
		Airport airport = createAirport(airportName);
		
		String query = "INSERT INTO `enterprises` (`name`, `airport`) VALUES (?,?)";
		ArrayList<Object> params = new ArrayList<Object>(2);
		params.add(name);
		params.add(airport.getId());
		
		database.executeUpdate(query, params);
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
	
	public void loadAirports() throws SQLException {
		String query = "SELECT `name` FROM `airports`";
		
		DatabaseResult dr = database.executeQuery(query);
		
		while(dr.next()) {
			Airport a = new Airport(this, dr.getString(0));
			airports.add(a);
		}
		
		Collections.sort(airports);
	}
	
	/**
	 * This methods loads all existing countries from the database.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void loadCountries() throws SQLException {
		String query = "SELECT `name` FROM `countries`";
		
		DatabaseResult dr = database.executeQuery(query);
		
		while(dr.next()) {
			Country c = new Country(this, dr.getString(0));
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
