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
 * This class bundles all user-defined settings this
 * application offers. Furthermore it is responsible
 * for reading and writing the custom settings to the
 * database for persistence.
 * 
 * @author jdno
 */
public class Configuration {

	/**
	 * The user has the choice to highlight scheduled
	 * routes, or to turn off row backgrounds.
	 */
	private boolean highlightScheduledRoutes = true;
	
	/**
	 * This is the model with the database, where the
	 * settings are saved. 
	 */
	private Model model;
	
	/**
	 * The configuration gets initialized default values
	 * and tries to load the user-defined settings from
	 * the database. 
	 * @param model The model to use.
	 */
	public Configuration(Model model) {
		this.model = model;
	}
	
	/**
	 * This is an auxiliary method used by the setter to change values in the
	 * database without the need to create an own query first.
	 * @param key The key to change.
	 * @param value The value to set.
	 * @throws SQLException If an SQL error occurs this gets thrown.
	 */
	private void updateKey(String key, Object value) throws SQLException {
		String query = "REPLACE INTO `meta_data` (`value`) VALUES (?) WHERE `key` = ?";
		ArrayList<Object> params = new ArrayList<Object>(2);
		params.add(value);
		params.add(key);
		
		model.getDatabase().executeUpdate(query, params);
	}

	/**
	 * @return the highlightScheduledRoutes
	 */
	public boolean isHighlightScheduledRoutes() {
		return highlightScheduledRoutes;
	}

	/**
	 * @param highlightScheduledRoutes the highlightScheduledRoutes to set
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void setHighlightScheduledRoutes(boolean highlightScheduledRoutes) throws SQLException {
		updateKey("highlightScheduledRoutes", highlightScheduledRoutes ? 1 : 0);
		this.highlightScheduledRoutes = highlightScheduledRoutes;
	}
}
