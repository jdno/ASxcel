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
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * This is the window the application gets "displayed in".
 * 
 * @author jdno
 */
public class Window extends JFrame {
	
	/**
	 * For future use.
	 */
	private static final long serialVersionUID = -3116025408041446105L;

	/**
	 * The view coordinates the GUI.
	 */
	private View view;
	
	/**
	 * On initialization the Window sets its parameters and
	 * gets displayed.
	 * @param view The view that coordinates the GUI.
	 */
	public Window(View view) {
		this.view = view;
		
		getContentPane().setLayout(new BorderLayout());
		
		setTitle("ASxcel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(new Footer(view), BorderLayout.SOUTH);
		
		initializeSize();
		
		setVisible(true);
	}
	
	/**
	 * This method opens the table that displays the routes.
	 */
	public void showRoutes() {
		getContentPane().removeAll();
		JScrollPane pane = new JScrollPane(new RoutesTable(view));
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(pane, BorderLayout.CENTER);
		getContentPane().add(new Footer(view), BorderLayout.SOUTH);
		pack();
		repaint();
	}
	
	/**
	 * This auxiliary method sets the size of the window and
	 * centers it in the middle of the screen.
	 */
	private void initializeSize() {
		int height = 600;
		int width = 1024;
		Dimension size = new Dimension(width, height);
		
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		this.setResizable(false);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);
	}
	
}
