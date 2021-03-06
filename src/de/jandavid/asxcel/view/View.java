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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
	 * This method creates an enterprise by asking the user for
	 * the name and the country to start in, and passing it on
	 * to the model where the actual creation is done.
	 * @return The name of the enterprise.
	 * @throws Exception If the enterprise cannot be created this gets thrown.
	 */
	public String createEnterprise() throws Exception {
		String name = "";
		String airport = "";
		
		while(name == null || name.equals("")) {
			name = JOptionPane.showInputDialog(window, "Name your enterprise:", "Create new enterprise", JOptionPane.PLAIN_MESSAGE);
		}
		
		while(airport == null || airport.equals("")) {
			airport = JOptionPane.showInputDialog(window, "Name your main hub:", "Create new enterprise", JOptionPane.PLAIN_MESSAGE);
		}
		
		model.createEnterprise(name, airport);
		
		return name;
	}
	
	/**
	 * This method creates a filter for the table. It ask the user by
	 * what parameter he wants to filter and changes the table accordingly.
	 */
	public void createFilter() {
		// TODO implement filter mechanism for route table
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
		
		Object[] airportsOrigin = new Object[model.getAirports().size()];
		
		int mainHubInt = 0;
		Airport mainHub = model.getEnterprise().getMainHub();
		
		for(int i = 0; i < model.getAirports().size(); i++) {
			Airport currentAirport = model.getAirports().get(i);
			
			if(mainHub.equals(currentAirport)) mainHubInt = i;
			
			airportsOrigin[i] = model.getAirports().get(i).getName();
		}
		
		origin = (String)JOptionPane.showInputDialog(
			window,
			"Origin:",
			"Create new route",
			JOptionPane.PLAIN_MESSAGE,
			null,
			airportsOrigin,
			airportsOrigin[mainHubInt]);
		if(origin == null || origin.equals("")) return;
		
		ArrayList<Airport> destinations = filterAirports(origin);
		
		if(destinations.size() < 1) {
			JOptionPane.showMessageDialog(window, "You have to create a new airports first.", "Info", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		Object[] airportsDestination = new Object[destinations.size()];
		
		for(int i = 0; i < destinations.size(); i++) {
			airportsDestination[i] = destinations.get(i).getName();
		}
		
		destination = (String)JOptionPane.showInputDialog(
			window,
			"Destination:",
			"Create new route",
			JOptionPane.PLAIN_MESSAGE,
			null,
			airportsDestination,
			airportsDestination[0]);
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
	 * This method initiates the deletion of an airport by asking
	 * the user which airports he wants to have removed. 
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void deleteAirport() throws SQLException {
		String airportName = "";
		
		String[] airports = new String[model.getAirports().size()];
		
		for(int i = 0; i < airports.length; i++) {
			airports[i] = model.getAirports().get(i).getName();
		}
		
		airportName = (String)JOptionPane.showInputDialog(
			window,
			"Airport:",
			"Delete airport",
			JOptionPane.PLAIN_MESSAGE,
			null,
			airports,
			airports[0]);
		if(airportName == null || airportName.equals("")) return;
		
		int confirm = JOptionPane.showConfirmDialog(null, "Please confirm that you want to\n" +
				"delete the following airport:\n\n" + airportName,
				"Confirm", JOptionPane.YES_NO_OPTION);
		
		if(confirm == JOptionPane.NO_OPTION) return;
		
		if(model.getEnterprise().getMainHub().getName().equals(airportName)) {
			JOptionPane.showMessageDialog(window, "You must not delete your main hub.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(model.getEnterprise().doRoutesExistFor(model.getAirport(airportName))) {
			JOptionPane.showMessageDialog(window, "This airport is still part of some routes. Delete\n" +
					"the routes before deleting the airport.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		model.deleteAirport(airportName);
	}
	
	/**
	 * This method initiates the deletion of an enterprise by asking the user
	 * for the enterprise's name and a confirmation.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void deleteEnterprise() throws SQLException {
		String enterprise = "";
		String[] enterprises = getEnterprises();
		
		enterprise = (String)JOptionPane.showInputDialog(
				window,
				"Enterprise:",
				"Delete enterprise",
				JOptionPane.PLAIN_MESSAGE,
				null,
				enterprises,
				enterprises[0]);
		if(enterprise == null || enterprise.equals("")) return;
		
		if(enterprise.equals(model.getEnterprise().getName())) {
			JOptionPane.showMessageDialog(window, "You must not delete the currently active enterprise.", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int confirm = JOptionPane.showConfirmDialog(null, "Please confirm that you want to\n" +
				"delete the following airport:\n\n" + enterprise,
				"Confirm", JOptionPane.YES_NO_OPTION);
		
		if(confirm == JOptionPane.NO_OPTION) return;
		
		model.deleteEnterprise(enterprise);
	}
	
	/**
	 * This auxiliary method filters the list of airports provided by the
	 * model by removing all airports to which a route already exists.
	 * @param originName The name of the airport to start from.
	 * @return A list of all airports without a route to the origin.
	 */
	private ArrayList<Airport> filterAirports(String originName) {
		ArrayList<Airport> airports = new ArrayList<Airport>();
		Airport origin = model.getAirport(originName);
		ArrayList<Airport> destinations = model.getEnterprise().getDestinations(origin);
		
		for(Airport a: model.getAirports()) {
			if(a.compareTo(origin) != 0) {
				if(!destinations.contains(a) && a.compareTo(model.getEnterprise().getMainHub()) != 0) {
					airports.add(a);
				}
			}
		}
		
		return airports;
	}
	
	/**
	 * This auxiliary method gets the names of all enterprises from the database.
	 * @return An array with the names of the enterprises
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	private String[] getEnterprises() throws SQLException {
		String query = "SELECT `name` FROM `enterprises`";
		
		DatabaseResult dr = model.getDatabase().executeQuery(query);
		
		String[] enterprises = new String[dr.getRowCount()];
		
		for(int i = 0; i < dr.getRowCount(); i++) {
			dr.next();
			enterprises[i] = dr.getString(0);
		}
		
		return enterprises;
	}
	
	/**
	 * This methods opens an About window.
	 */
	public void showAbout() {
		JFrame about = new JFrame();
		about.setTitle("About ASxcel");
		Image icon = Toolkit.getDefaultToolkit().getImage(View.class.getResource("icon_64x64.png"));
		about.setIconImage(icon);
		about.setResizable(false);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = new Dimension(300,80);
		int xPos = (screenSize.width - size.width) / 2;
		int yPos = (screenSize.height - size.height) / 2;
		
		about.setSize(size);
		about.setLocation(xPos, yPos);
		about.getContentPane().setLayout(new BorderLayout());
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(0,1));
		JLabel title = new JLabel("<html><h2><b>ASxcel</html>");
		title.setHorizontalAlignment(JLabel.CENTER);
		center.add(title);
		JLabel version = new JLabel("Release 1.1");
		version.setHorizontalAlignment(JLabel.CENTER);
		center.add(version);
		JLabel imageIcon = new JLabel(new ImageIcon(icon));
		imageIcon.setHorizontalAlignment(JLabel.CENTER);
		imageIcon.setBorder(new EmptyBorder(5, 48, 5, 0));
		
		about.add(imageIcon, BorderLayout.WEST);
		about.add(center, BorderLayout.CENTER);
		
		about.setVisible(true);
	}
	
	/**
	 * This methods opens the table with the routes.
	 */
	public void showRoutes() {
		window.showRoutes();
	}
	
	/**
	 * This method displays the window.
	 */
	public void showWindow() {
		this.window = new Window(this);
	}

	/**
	 * @return the controller
	 */
	public Controller getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(Controller controller) {
		this.controller = controller;
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
