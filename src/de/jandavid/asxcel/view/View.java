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
package de.jandavid.asxcel.view;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import de.jandavid.asxcel.model.Airport;
import de.jandavid.asxcel.model.DatabaseResult;
import de.jandavid.asxcel.model.Model;

/**
 * The View coordinates the graphical user interface and
 * acts as an interface for the controller to change it.
 * 
 * @author jdno
 */
public class View {
	
	/**
	 * The ActionListener of this application.
	 */
	private Controller controller;
	
	/**
	 * The view pulls the data from the model.
	 */
	private Model model;
	
	/**
	 * This is the application window.
	 */
	private Window window;
	
	/**
	 * The view pulls the data to display from the model
	 * and creates the application's window at initialization.
	 * @param model The model to pull from.
	 */
	public View(Model model) {
		this.model = model;
		this.controller = new Controller(this);
		this.window = new Window(this);
	}
	
	/**
	 * This method creates a new airport by asking the user for the
	 * airports name. If the user closes the dialog or does not enter
	 * a name nothing will be created.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void createAirport() throws SQLException {
		String name = "";
		
		name = JOptionPane.showInputDialog(window, "Name of the airport:", "Create new airport", JOptionPane.PLAIN_MESSAGE);
		if(name == null || name.equals("")) {
			// We assume the user does not want to create a new airport.
		} else {
			model.createAirport(name);
		}
	}
	
	/**
	 * This method creates a new country by asking the user for the
	 * countries name. If the user closes the dialog or does not
	 * enter a name nothing will be created.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void createCountry() throws SQLException {
		String country = "";
		
		country = JOptionPane.showInputDialog(window, "Name of the country:", "Create new country", JOptionPane.PLAIN_MESSAGE);
		
		if(country == null || country.equals("")) {
			// We assume the user does not want to create a new country.
		} else {
			model.createCountry(country);
		}
		
		showRoutes();
	}
	
	/**
	 * This method creates an enterprise by asking the user for
	 * the name and the country to start in, and passing it on
	 * to the model where the actual creation is done.
	 * @return The name of the enterprise.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public String createEnterprise() throws SQLException {
		String name = "";
		String country = "";
		
		while(name == null || name.equals("")) {
			name = JOptionPane.showInputDialog(window, "Name your enterprise:", "Create new enterprise", JOptionPane.PLAIN_MESSAGE);
		}
		
		while(country == null || country.equals("")) {
			country = JOptionPane.showInputDialog(window, "Enter country:", "Create new enterprise", JOptionPane.PLAIN_MESSAGE);
		}
		
		model.createEnterprise(name, country);
		
		return name;
	}
	
	/**
	 * This method creates a route by asking the user for the origin
	 * and destination airport. After the user's input these values
	 * are passed to the model where the route gets created.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void createRoute() throws SQLException {
		String origin = "";
		String destination = "";
		
		if(model.getAirports().size() < 2) {
			JOptionPane.showMessageDialog(window, "You have to create two airports first.", "Info", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		Object[] airports = new Object[model.getAirports().size()];
		
		for(int i = 0; i < model.getAirports().size(); i++) {
			airports[i] = model.getAirports().get(i).getName();
		}
		
		origin = (String)JOptionPane.showInputDialog(
			window,
			"Origin:",
			"Create new route",
			JOptionPane.PLAIN_MESSAGE,
			null,
			airports,
			airports[0]);
		if(origin == null || origin.equals("")) return;
		
		destination = (String)JOptionPane.showInputDialog(
			window,
			"Destination:",
			"Create new route",
			JOptionPane.PLAIN_MESSAGE,
			null,
			airports,
			airports[0]);
		if(destination == null || destination.equals("")) return;
		
		if(origin.equals(destination)) {
			JOptionPane.showMessageDialog(window, "Origin and destination must not be the same.", "Error", JOptionPane.ERROR_MESSAGE);
			createRoute();
		} else {
			Airport start = model.getAirport(origin);
			Airport end = model.getAirport(destination);
			
			model.createRoute(start, end);
		}
		
		showRoutes();
	}
	
	/**
	 * This method opens a dialog where the user can select the
	 * enterprise to work with. Additionally to all existing
	 * enterprises an option to create a new one is present.
	 * @return The name of the enterprise.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public String chooseEnterprise() throws SQLException {
		String name = "";
		String query = "SELECT `name` FROM `enterprises`";
		
		DatabaseResult dr = model.getDatabase().executeQuery(query);
		
		Object[] enterprises = new Object[dr.getRowCount() + 1];
		
		for(int i = 0; i < dr.getRowCount(); i++) {
			dr.next();
			enterprises[i] = dr.getString(0);
		}
		enterprises[dr.getRowCount()] = "New...";
		
		while(name == null || name.equals("")) {
			name = (String)JOptionPane.showInputDialog(
					window,
                    "Choose the enterprise you want\n" +
                    "to manage:",
                    "Select enterprise",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    enterprises,
                    enterprises[0]);
		}
		
		return name;
	}
	
	/**
	 * This methods opens the table with the routes.
	 */
	public void showRoutes() {
		window.showRoutes();
	}

	/**
	 * @return the controller
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * @return the window
	 */
	public Window getWindow() {
		return window;
	}

}