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
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * The AboutWindow contains some information about the application itself
 * and about its author, the license and other stuff that is useful but
 * not critical to know.
 * 
 * @author jdno
 */
public class AboutWindow extends AbstractWindow {

	/**
	 * For future use.
	 */
	private static final long serialVersionUID = -3196187368959043440L;
	
	/**
	 * The icon of the application.
	 */
	private Image icon = Toolkit.getDefaultToolkit().getImage(View.class.getResource("icon_64x64.png"));
	
	/**
	 * Upon initialization a small window gets open that displays some
	 * information about the application. 
	 */
	public AboutWindow() {
		super(new Dimension(300,80));
		
		setTitle("About ASxcel");
		setIconImage(icon);
		
		getContentPane().setLayout(new GridLayout(0,1));

		add(generateApplicationInfo());
		
		center();
		setVisible(true);
	}
	
	/**
	 * This auxiliary method generates a JPanel with information about the
	 * application like the version, decorated with its icon.
	 * @return A JPanel containing several labels
	 */
	private JPanel generateApplicationInfo() {
		JPanel appInfo = new JPanel();
		appInfo.setLayout(new BorderLayout());
		
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(0,1));
		JLabel title = new JLabel("<html><h2><b>ASxcel</html>");
		title.setHorizontalAlignment(JLabel.CENTER);
		center.add(title);
		
		JLabel version = new JLabel("Release 1.1");
		version.setHorizontalAlignment(JLabel.CENTER);
		center.add(version);
		
		JLabel imageIcon = new JLabel(new ImageIcon(icon));
		imageIcon.setHorizontalAlignment(JLabel.CENTER);
		imageIcon.setBorder(new EmptyBorder(5, 48, 5, 0));
		
		appInfo.add(imageIcon, BorderLayout.WEST);
		appInfo.add(center, BorderLayout.CENTER);

		return appInfo;
	}

}
