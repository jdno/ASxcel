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

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class represents a country and got introduced
 * to the application to allow for future enhancements.
 * 
 * @author jdno
 */
public class Country implements Comparable<Country> {

	/**
	 * This is the ID of the country in the database.
	 */
	private int id;
	
	/**
	 * This is needed to access the database.
	 */
	private Model model;
	
	/**
	 * This is the name of the country.
	 */
	private String name;
	
	/**
	 * Initialize a country by its name in the database.
	 * @param name The country's name
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	protected Country(Model model, String name) throws SQLException {
		this.model = model;
		this.name = name;
		
		syncWithDb();
	}
	
	/**
	 * This constructor initializes a country with all its attributes. No
	 * synchronization with the database happens! This is meant as a help
	 * for the model to load a set of countries with one query from the
	 * database and initialize them all at once without further queries.
	 * @param model The model to use
	 * @param id The ID of the country
	 * @param name The name of the country
	 */
	protected Country(Model model, int id, String name) {
		this.model = model;
		this.id = id;
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Country c) {
		return name.compareTo(c.getName());
	}

	/**
	 * This is an auxiliary method that synchronizes an instance of Country
	 * with the database. Especially it checks if the country exists in the
	 * database and if not, it creates it.
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	private void syncWithDb() throws SQLException {
		String query = "SELECT * FROM `countries` WHERE name = ? LIMIT 1";
		ArrayList<Object> params = new ArrayList<Object>(1);
		params.add(name);
		
		DatabaseResult dr = model.getDatabase().executeQuery(query, params);
		
		if(dr.next()) {
			id = dr.getInt(0);
			name = dr.getString(1);
		} else {
			query = "INSERT INTO `countries` (`name`) VALUES (?)";
			model.getDatabase().executeUpdate(query, params);
			syncWithDb();
		}
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

}
