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

}
