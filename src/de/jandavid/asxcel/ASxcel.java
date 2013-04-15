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

import java.sql.SQLException;

import javax.swing.UIManager;

import de.jandavid.asxcel.model.Model;
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
			View view = new View(model);
			String enterprise = view.chooseEnterprise();
			
			if(enterprise.equals("New...")) {
				enterprise = view.createEnterprise();
			}
			
			model.loadEnterprise(enterprise);
			view.showRoutes();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			
		}
	}

}
