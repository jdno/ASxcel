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
