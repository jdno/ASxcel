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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import de.jandavid.asxcel.model.Model;

/**
 * This class acts as the ActionListener and handles the
 * interaction with the user.
 * 
 * @author jdno
 */
public class Controller implements ActionListener {
	
	/**
	 * The model provides the data.
	 */
	private Model model;
	
	/**
	 * The view coordinates the GUI.
	 */
	private View view;
	
	/**
	 * The controller processes ActionEvents triggered by
	 * the user.
	 * @param view The view coordinates the GUI.
	 */
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if(e.getActionCommand().equals("change_enterprise")) {
				initializeEnterprise();
			} else if(e.getActionCommand().equals("create_airport")) {
				view.createAirport();
			} else if(e.getActionCommand().equals("create_filter")) {
				// view.createFilter();
			} else if(e.getActionCommand().equals("create_route")) {
				view.createRoute();
			} else if(e.getActionCommand().equals("delete_airport")) {
				view.deleteAirport();
			} else if(e.getActionCommand().equals("delete_filter")) {
				// view.deleteFilter();
			} else if(e.getActionCommand().equals("menu_about")) {
				view.showAbout();
			} else if(e.getActionCommand().equals("menu_help")) {
				String url = "https://github.com/jdno/ASxcel/wiki";
				
				if(java.awt.Desktop.isDesktopSupported() ) {
					java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
			 
					if(desktop.isSupported(java.awt.Desktop.Action.BROWSE) ) {
			        	try {
			        		java.net.URI uri = new java.net.URI(url);
							desktop.browse(uri);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			} else if(e.getActionCommand().equals("menu_quit")) {
				System.exit(0);
			}
		} catch (SQLException exception) {
			JOptionPane.showMessageDialog(null, "During your last action an error occured.\n" +
					"There seems to be a problem with the\n" +
					"database. Please send a bug report with\n" +
					"your last steps to:\n" +
					"asxcel.support@jandavid.de", "Database error", JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This method handles the initialization (or the reloading) of an
	 * enterprise. It ask the user for the name, triggers the creation
	 * if the user wants a new one, and loads it from the model.
	 * @throws Exception Thrown if the given enterprise cannot be found.
	 */
	public void initializeEnterprise() throws Exception {
		String enterprise = view.chooseEnterprise();
		
		if(enterprise.equals("New...")) {
			enterprise = view.createEnterprise();
		}
		
		model.loadEnterprise(enterprise);
		view.showRoutes();
	}
}
