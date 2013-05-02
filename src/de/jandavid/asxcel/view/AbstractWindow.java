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

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * AbstractWindow provides common attributes and methods all windows of this
 * application share. It gets initialized with a size and is by default not
 * resizable.
 * 
 * @author jdno
 */
public abstract class AbstractWindow extends JFrame {
	
	/**
	 * For future use.
	 */
	private static final long serialVersionUID = -567122131746283541L;
	
	/**
	 * The size of the window.
	 */
	protected Dimension size;
	
	/**
	 * The view coordinates the GUI.
	 */
	protected View view;
	
	/**
	 * The AbstractWindow gets initialized with its size, and provides attributes
	 * and methods every window should have. By default AbstractWindows are not
	 * resizable.
	 * @param size The size of the window
	 */
	public AbstractWindow(Dimension size) {
		this.size = size;
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		this.setResizable(false);
	}
	
	/**
	 * This method centers a window in the middle of the screen. It should be called after the
	 * size has been set.
	 */
	public void center() {
		Dimension size = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 2);
	}
	
	/**
	 * This displays a simple error dialog with the given message.
	 * @param message The error message to display to the user
	 */
	public void errorDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * This displays a simple message dialog with the message type set
	 * to information.
	 * @param message The message to display
	 */
	public void informationDialog(String message) {
		JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * This presents an input dialog to the user where he can enter a text.
	 * @param title The title of the dialog
	 * @param message The message to the user
	 * @return The input formated as a String
	 */
	public String inputDialog(String title, String message) {
		return JOptionPane.showInputDialog(this, message, title, JOptionPane.PLAIN_MESSAGE);
	}
	
	/**
	 * This presents an input dialog to the user where he can choose from a list
	 * of predefined values. The list is handed to the dialog in form of an array
	 * with the option to preselect a specific element by providing its position
	 * in the array.
	 * @param title The title of the dialog
	 * @param message The message to the user
	 * @param options The list of options from which the user can choose
	 * @param selection The preselected element (set to 0 if you have no preferences)
	 * @return The choice formated as a String
	 */
	public String inputDialog(String title, String message, String[] options, int selection) {
		return (String) JOptionPane.showInputDialog(
				this,
				message,
				title,
				JOptionPane.PLAIN_MESSAGE,
				null,
				options,
				options[selection]);
	}

	/**
	 * This method displays a dialog the user has to confirm by choosing "Yes" or "No"
	 * as possible options. If the user selects "Yes" true gets returned, if the user
	 * declines by pressing "No" false will be the return value.
	 * @param message The question the user should answer
	 * @return True of the user confirms, false if he declines.
	 */
	public boolean yesNoDialog(String message) {
		int confirm = JOptionPane.showConfirmDialog(this, message, "Confirm", JOptionPane.YES_NO_OPTION);
		
		if(confirm == JOptionPane.NO_OPTION) {
			return false;
		} else {
			return true;
		}
	}

}
