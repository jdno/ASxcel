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
package de.jandavid.asxcel.view.Routes;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.jandavid.asxcel.view.View;

/**
 * This is the footer at the end of the Routes view, which contains
 * settings to filter routes based on their distances and options
 * to create new airports, countries or routes.
 * 
 * @author jdno
 */
public class Footer extends JPanel {

	/**
	 * For future use.
	 */
	private static final long serialVersionUID = 5801208436017387598L;
	
	/**
	 * The view is needed to access the model.
	 */
	private View view;
	
	/**
	 * The footer creates its two panels at initialization, on with
	 * options for filtering, one with options to create objects.
	 * @param view The view with access to the model.
	 */
	public Footer(View view) {
		this.view = view;
		
		this.setLayout(new GridLayout(2,1));
		
		add(generateCreate());
		add(generateDelete());
	}
	
	/**
	 * This auxiliary methods bundles the steps necessary to create the
	 * panel with the options to create stuff.
	 * @return The panel with the create options.
	 */
	private JPanel generateCreate() {
		JPanel createPanel = new JPanel();
		createPanel.setLayout(new GridLayout(1,0));
		
		JLabel desc = new JLabel("Create", JLabel.CENTER);
		createPanel.add(desc);
		
		JButton airport = new JButton("Airport");
		airport.addActionListener(view.getController());
		airport.setActionCommand("create_airport");
		createPanel.add(airport);
		
		JButton route = new JButton("Route");
		route.addActionListener(view.getController());
		route.setActionCommand("create_route");
		createPanel.add(route);
		
		/* JButton filter = new JButton("Filter");
		filter.addActionListener(view.getController());
		filter.setActionCommand("create_filter");
		filter.setFocusable(false);
		createPanel.add(filter); */
		
		createPanel.add(new JPanel());
		
		return createPanel;
	}
	
	/**
	 * This auxiliary methods bundles the steps necessary to create the
	 * panel with the options to delete stuff.
	 * @return The panel with the delete options.
	 */
	private JPanel generateDelete() {
		JPanel createPanel = new JPanel();
		createPanel.setLayout(new GridLayout(1,0));
		
		JLabel desc = new JLabel("Delete", JLabel.CENTER);
		createPanel.add(desc);
		
		JButton airport = new JButton("Airport");
		airport.addActionListener(view.getController());
		airport.setActionCommand("delete_airport");
		airport.setFocusable(false);
		createPanel.add(airport);
		
		createPanel.add(new JPanel());
		
		/* JButton filter = new JButton("Filter");
		filter.addActionListener(view.getController());
		filter.setActionCommand("delete_filter");
		filter.setFocusable(false);
		createPanel.add(filter); */
		
		createPanel.add(new JPanel());
		
		return createPanel;
	}

}
