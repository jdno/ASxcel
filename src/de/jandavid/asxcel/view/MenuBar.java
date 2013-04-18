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

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * The menus provide options to add and remove items, configure
 * the application and links to the online help.
 * 
 * @author jdno
 */
public class MenuBar extends JMenuBar {

	/**
	 * For future use.
	 */
	private static final long serialVersionUID = 2057840746061069452L;
	
	private ActionListener listener;
	
	/**
	 * This menu provides general options.
	 */
	private JMenu menuFile = new JMenu("File");
	
	/**
	 * This menu provides information about the application.
	 */
	private JMenu menuHelp = new JMenu("Help");
	
	/**
	 * The menu bar generates its menus at initialization.
	 */
	public MenuBar(ActionListener l) {
		listener = l;
		generateMenuFile();
		generateMenuHelp();
	}
	
	/**
	 * This auxiliary method generates the file menu.
	 */
	private void generateMenuFile() {
		JMenu menuCreate = new JMenu("New");
		JMenu menuDelete = new JMenu("Delete");
		
		JMenuItem createAirport = new JMenuItem("Airport");
		createAirport.setActionCommand("create_airport");
		createAirport.addActionListener(listener);
		createAirport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menuCreate.add(createAirport);
		
		JMenuItem createRoute = new JMenuItem("Route");
		createRoute.setActionCommand("create_route");
		createRoute.addActionListener(listener);
		createRoute.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menuCreate.add(createRoute);
		
		JMenuItem deleteAirport = new JMenuItem("Airport");
		deleteAirport.setActionCommand("delete_airport");
		deleteAirport.addActionListener(listener);
		menuDelete.add(deleteAirport);
		
		JMenuItem quit = new JMenuItem("Quit ASxcel");
		quit.setActionCommand("menu_quit");
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
			    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		quit.addActionListener(listener);
		
		menuFile.add(menuCreate);
		menuFile.addSeparator();
		menuFile.add(menuDelete);
		menuFile.addSeparator();
		menuFile.add(quit);
		
		add(menuFile);
	}

	/**
	 * This auxiliary method generates the help menu.
	 */
	private void generateMenuHelp() {
		JMenuItem about = new JMenuItem("About");
		about.setActionCommand("menu_about");
		about.addActionListener(listener);
		
		JMenuItem help = new JMenuItem("Online Help");
		help.setActionCommand("menu_help");
		help.addActionListener(listener);
		
		menuHelp.add(about);
		menuHelp.addSeparator();
		menuHelp.add(help);
		
		add(menuHelp);
	}
}
