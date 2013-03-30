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
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.jandavid.asxcel.model.Country;

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
		
		add(generateFilter());
		add(generateCreate());
	}
	
	/**
	 * This auxiliary methods bundles the steps necessary to create the
	 * panel with the options to create stuff.
	 * @return The panel with the create options.
	 */
	private JPanel generateCreate() {
		JPanel createPanel = new JPanel();
		createPanel.setLayout(new GridLayout(1,0));
		
		JLabel desc = new JLabel("Create new", JLabel.CENTER);
		createPanel.add(desc);
		
		JButton airport = new JButton("Airport");
		airport.addActionListener(view.getController());
		airport.setActionCommand("create_new_airport");
		airport.setFocusable(false);
		createPanel.add(airport);
		
		JButton route = new JButton("Route");
		route.addActionListener(view.getController());
		route.setActionCommand("create_new_route");
		route.setFocusable(false);
		createPanel.add(route);
		
		createPanel.add(new JPanel());
		
		return createPanel;
	}
	
	/**
	 * This auxiliary method bundles the steps necessary to create the
	 * panel with the filter options.
	 * @return The panel with the filter options.
	 */
	private JPanel generateFilter() {
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new GridLayout(1,0));
		
		JLabel desc = new JLabel("Filter by", JLabel.CENTER);
		filterPanel.add(desc);
		
		ArrayList<Country> countries = view.getModel().getCountries();
		String[] countryNames = new String[countries.size() + 1];
		
		countryNames[0] = "All countries";
		for(int i = 0; i < countries.size(); i++) {
			countryNames[i+1] = countries.get(i).getName();
		}
		
		JComboBox<String> countryBox = new JComboBox<String>(countryNames);
		filterPanel.add(countryBox);
		
		JPanel minDistPanel = new JPanel();
		minDistPanel.setLayout(new BorderLayout());
		JLabel minDistDesc = new JLabel("minimal distance");
		minDistPanel.add(minDistDesc, BorderLayout.WEST);
		JTextField minDistance = new JTextField();
		minDistance.setText("0");
		minDistance.setHorizontalAlignment(JTextField.RIGHT);
		minDistPanel.add(minDistance, BorderLayout.CENTER);
		filterPanel.add(minDistPanel);
		
		JPanel maxDistPanel = new JPanel();
		maxDistPanel.setLayout(new BorderLayout());
		JLabel maxDistDesc = new JLabel("maximal distance");
		maxDistPanel.add(maxDistDesc, BorderLayout.WEST);
		JTextField maxDistance = new JTextField();
		maxDistance.setText("0");
		maxDistance.setHorizontalAlignment(JTextField.RIGHT);
		maxDistPanel.add(maxDistance, BorderLayout.CENTER);
		filterPanel.add(maxDistPanel);
		
		return filterPanel;
	}
}
