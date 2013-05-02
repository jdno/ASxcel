/**
 * ASxcel - A small tool for the browser game AirlineSim
 *
 * Copyright 2013 jdno <https://github.com/jdno/>
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

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import de.jandavid.asxcel.model.Model;
import de.jandavid.asxcel.model.Route;

/**
 * This custom renderer is responsible for highlighting the routes depending
 * on they being scheduled or not. Scheduled routes are displayed green, and
 * the rest red. It works for Booleans, but for Strings and Integer an own
 * renderer exists.
 * 
 * @author jdno
 */
public class BooleanCellRenderer extends JCheckBox implements TableCellRenderer {

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
	 * For future use.
	 */
	private static final long serialVersionUID = 8340334282089601011L;
	
	/**
	 * The BooleanCellRenderer sets the background color of routes depending
	 * on whether they are scheduled or not.
	 * @param model The model holding the data.
	 */
	public BooleanCellRenderer(Model model) {
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
		this.setSelected((Boolean) value);
		this.setHorizontalAlignment(JCheckBox.CENTER);
		
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
