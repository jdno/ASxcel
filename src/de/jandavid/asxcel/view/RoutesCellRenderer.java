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

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import de.jandavid.asxcel.model.Model;
import de.jandavid.asxcel.model.Route;

/**
 * This custom renderer is responsible for highlighting the routes depending
 * on they being scheduled or not. Scheduled routes are displayed green, and
 * the rest red.
 * 
 * @author jdno
 */
public class RoutesCellRenderer extends JLabel implements TableCellRenderer {
	
	/**
	 * For future use.
	 */
	private static final long serialVersionUID = 2054383280196261028L;

	/**
	 * This is the color scheduled routes have when they are selected.
	 */
	private Color greenSelected = new Color(153, 255, 153);
	
	/**
	 * This is the color scheduled routes have when they are unselected.
	 */
	private Color greenUnselected = new Color(204, 255, 204);
	
	/**
	 * The model that holds the data.
	 */
	private Model model;
	
	/**
	 * This is the color unscheduled routes have when they are selected.
	 */
	private Color redSelected = new Color(255, 153, 153);
	
	/**
	 * This is the color unscheduled routes have when they are unselected.
	 */
	private Color redUnselected = new Color(255, 204, 204);

	/**
	 * The RoutesCellRenderer sets the background color of routes depending
	 * on whether they are scheduled or not.
	 * @param table The table to work with.
	 * @param model The model holding the data.
	 */
	public RoutesCellRenderer(JTable table, Model model) {
		this.model = model;
		this.setOpaque(true);
		this.setBorder(new EmptyBorder(2, 2, 2, 2));
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		Route r = model.getEnterprise().getRoutes().get(table.convertRowIndexToModel(row));
		
		if(value == null) value = "";
		this.setText(String.valueOf(value));
		
		if(value instanceof Integer) {
			this.setHorizontalAlignment(JLabel.RIGHT);
		}
		
		if(r.isScheduled()) {
			if(isSelected) {
				setBackground(greenSelected);
			} else {
				setBackground(greenUnselected);
			}
		} else {
			if(isSelected) {
				setBackground(redSelected);
			} else {
				setBackground(redUnselected);
			}
		}
		
		return this;
	}

}
