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

/**
 * A route is a connection between to airports. It
 * is defined by its origin and destination, and has
 * additional parameters like the distance between
 * these two airports, the load and if it is scheduled
 * or not.
 * 
 * @author jdno
 */
public class Route implements Comparable<Route> {

	/**
	 * This is the airport where the route ends.
	 */
	private Airport destination;
	
	/**
	 * This is the distance of this route.
	 */
	private int distance;
	
	/**
	 * The route's ID in the database;
	 */
	private int id;
	
	/**
	 * This is a list of loads over a certain period
	 * of time, coming from the destination.
	 */
	private int loadFrom;
	
	/**
	 * This is a list of loads over a certain period
	 * of time, going to the destination.
	 */
	private int loadTo;
	
	/**
	 * This is needed to access the database.
	 */
	private Model model;
	
	/**
	 * This is the airport where the route starts.
	 */
	private Airport origin;
	
	/**
	 * This indicates if the route is already in use
	 * or if it still in planing.
	 */
	private boolean scheduled;
	
	/**
	 * A route gets initialized with two airports and a link to the
	 * model.
	 * @param model This is needed for access to the database.
	 * @param destination The airport this route ends.
	 * @param origin The airport this route starts.
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	public Route(Model model, Airport origin, Airport destination) throws SQLException {
		this.model = model;
		this.destination = destination;
		this.origin = origin;
		
		syncWithDb();
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Route o) {
		return origin.compareTo(o.getOrigin());
	}
	
	/**
	 * This method synchronizes a route with the database, assuming the route
	 * has been created already. If this is the case this instance of Route
	 * gets filled with its data, else a new route gets added to the
	 * database with the given origin and destination.
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	private void syncWithDb() throws SQLException {
		String query = "SELECT `r`.`id`, `r`.`distance`, `r`.`loadFrom`, `r`.`loadTo`, " +
				"`r`.`scheduled` FROM `routes` AS `r` WHERE `r`.`origin` = ? " +
				"AND `r`.`destination` = ? LIMIT 1";
		ArrayList<Object> params = new ArrayList<Object>(3);
		params.add(origin.getId());
		params.add(destination.getId());
		
		DatabaseResult dr = model.getDatabase().executeQuery(query, params);
		
		if(dr.next()) {
			id = dr.getInt(0);
			distance = dr.getInt(1);
			loadFrom = dr.getInt(2);
			loadTo = dr.getInt(3);
			scheduled = dr.getInt(4) == 1 ? true : false;
		} else {
			query = "INSERT INTO `routes` (`origin`, `destination`, `enterprise`) VALUES (?,?,?)";
			params.add(model.getEnterprise().getId());
			model.getDatabase().executeUpdate(query, params);
			syncWithDb();
		}
	}

	/**
	 * This is an auxiliary method used by the setter to change values in the
	 * database without the need to create an own query first.
	 * @param field The name of the column in the database.
	 * @param value The value to set.
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	private void updateField(String field, Object value) throws SQLException {
		String query = "UPDATE `routes` SET '" + field + "' = ? WHERE `id` = '" + id + "'";
		ArrayList<Object> params = new ArrayList<Object>(1);
		params.add(value);
		
		model.getDatabase().executeUpdate(query, params);
	}
	
	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	public void setDistance(int distance) throws SQLException {
		updateField("distance", distance);
		this.distance = distance;
	}

	/**
	 * @return the loadFrom
	 */
	public int getLoadFrom() {
		return loadFrom;
	}

	/**
	 * @param loadFrom the loadFrom to set
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	public void setLoadFrom(int loadFrom) throws SQLException {
		updateField("loadFrom", loadFrom);
		this.loadFrom = loadFrom;
	}

	/**
	 * @return the loadTo
	 */
	public int getLoadTo() {
		return loadTo;
	}

	/**
	 * @param loadTo the loadTo to set
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	public void setLoadTo(int loadTo) throws SQLException {
		updateField("loadTo", loadTo);
		this.loadTo = loadTo;
	}

	/**
	 * @return the scheduled
	 */
	public boolean isScheduled() {
		return scheduled;
	}

	/**
	 * @param scheduled the scheduled to set
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	public void setScheduled(boolean scheduled) throws SQLException {
		updateField("scheduled", scheduled ? 1 : 0);
		this.scheduled = scheduled;
	}

	/**
	 * @return the destination
	 */
	public Airport getDestination() {
		return destination;
	}

	/**
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * @return the origin
	 */
	public Airport getOrigin() {
		return origin;
	}

}
