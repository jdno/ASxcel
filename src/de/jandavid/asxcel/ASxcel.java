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
package de.jandavid.asxcel;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.UIManager;

import de.jandavid.asxcel.model.Model;
import de.jandavid.asxcel.model.UpdateManager;
import de.jandavid.asxcel.view.Controller;
import de.jandavid.asxcel.view.View;

/**
 * ASxcel is an application to help players of the
 * browser game AirlineSim (www.airlinesim.aero)
 * plan new routes. It provides an interface where
 * new routes can be added, with their distance and
 * information about the airports, to get a better
 * picture of possible expansions in the future.
 * 
 * @author jdno
 */
public class ASxcel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		try {
			Model model = new Model("asxcel.sqlite");
			UpdateManager um = new UpdateManager(model.getDatabase());
			
			if(um.updateAvailable()) {
				try {
					um.installUpdates();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				} catch (SQLException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
			
			model.initializeModel();
			View view = new View(model);
			Controller controller = new Controller(model, view);
			view.setController(controller);
			
			view.showWindow();
			controller.initializeEnterprise();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
