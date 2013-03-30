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

/**
 * This class acts as the ActionListener and handles the
 * interaction with the user.
 * 
 * @author jdno
 */
public class Controller implements ActionListener {
	
	/**
	 * The view coordinates the GUI.
	 */
	private View view;
	
	/**
	 * The controller processes ActionEvents triggered by
	 * the user.
	 * @param view The view coordinates the GUI.
	 */
	public Controller(View view) {
		this.view = view;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if(e.getActionCommand().equals("create_new_route")) {
				view.createRoute();
			} else if(e.getActionCommand().equals("create_new_country")) {
				view.createCountry();
			} else if(e.getActionCommand().equals("create_new_airport")) {
				view.createAirport();
			}
		} catch (SQLException exception) {
			JOptionPane.showMessageDialog(null, "During your last action an error occured.\n" +
					"There seems to be a problem with the\n" +
					"database. Please send a bug report with\n" +
					"your last steps to:\n" +
					"asxcel.support@jandavid.de", "Database error", JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}
		
	}

}
