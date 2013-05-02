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
package de.jandavid.asxcel.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite for the application ASxcel
 * 
 * @author jdno
 */
@RunWith(Suite.class)
@SuiteClasses({
	DatabaseTest.class,
	ModelTest.class,
	EnterpriseTest.class
})
public class TestSuite {
	
	// Run all tests automatically

}
