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
package de.jandavid.asxcel.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import de.jandavid.asxcel.view.Routes.Table;

/**
 * This is the window the application gets "displayed in".
 * 
 * @author jdno
 */
public class ContentWindow extends AbstractWindow {
	
	/**
	 * For future use.
	 */
	private static final long serialVersionUID = -3116025408041446105L;
	
	/**
	 * On initialization the Window sets its parameters and
	 * gets displayed.
	 * @param view The view that coordinates the GUI.
	 */
	public ContentWindow(View view) {
		super(new Dimension(1024, 600));
		this.view = view;
		
		getContentPane().setLayout(new BorderLayout());
		
		setTitle("ASxcel");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ContentWindow.class.getResource("icon_64x64.png")));
		
		MenuBar menu = new MenuBar(view.getController());
		setJMenuBar(menu);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		center();
		
		setVisible(true);
	}
	
	/**
	 * This method opens the table that displays the routes.
	 */
	public void showRoutes() {
		getContentPane().removeAll();
		Table routes = new Table(view);
		JScrollPane pane = new JScrollPane(routes);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(pane, BorderLayout.CENTER);
		pack();
		repaint();
	}
	
}
