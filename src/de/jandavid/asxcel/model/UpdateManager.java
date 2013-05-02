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
package de.jandavid.asxcel.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

/**
 * The UpdateManager handles updates to this application. Updates are
 * done by checking the version value in the database and comparing it
 * to the hard-coded version number of the application's code. This way
 * an updated application can detect that the database is not up-to-date.
 * If an update is available the UpdateManager applies all available
 * patches one by one until the database is running the same version as
 * the code.
 * 
 * @author jdno
 */
public class UpdateManager {

	/**
	 * The database to update
	 */
	private Database database;

	/**
	 * The most current version of the database (hard coded!)
	 */
	private int currentVersion = 2;

	/**
	 * The version of the current database
	 */
	private int detectedVersion = 1;

	/**
	 * The UpdateManager is responsible for updating the database
	 * and other files in case a the application's and the database's
	 * version differ. To achieve this he needs the database.
	 * @param database The database to check for updates
	 */
	public UpdateManager(Database database) {
		this.database = database;
	}

	/**
	 * This method installs all updates that are available and not
	 * installed. In case of an SQLException no changes are made to
	 * the database.
	 * @throws IOException If no backup file can be created this gets thrown.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public void installUpdates() throws IOException, SQLException {
		if (detectedVersion == 1) {
			updateTo2();
		}
	}

	/**
	 * This checks if an update is available by reading the according database field.
	 * If an exception occurs the default behavior is to assume an update is necessary.
	 * This is done because the first version of the database does not have the table
	 * table that gets checked.
	 * @return True if an update can be performed, false otherwise.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	public boolean updateAvailable() {
		try {
			String query = "SELECT `value` FROM `meta_data` WHERE `key` = 'version' LIMIT 1";
			DatabaseResult dr = database.executeQuery(query);

			if (dr.next()) {
				detectedVersion = dr.getInt(0);

				if (detectedVersion < currentVersion) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} catch (SQLException e) {
			return true;
		}
	}

	/**
	 * This auxiliary method backups the database. This is done before every update.
	 * @param from The current version of the database.
	 * @throws IOException If the file cannot be copied this gets thrown.
	 */
	private void backup(int from) throws IOException {
		File originalDb = new File(database.getFileName());
		File backupDb = new File(database.getFileName() + ".bak." + String.valueOf(from));
		Files.copy(originalDb.toPath(), backupDb.toPath());
	}

	/**
	 * Update to version 2
	 * 
	 * The main features of version 2 are:
	 * - table meta_data with information about the application, most importantly the database
	 * version
	 * - table enterprise_has_airport with an n:m mapping, used by the view to reduce the number
	 * of airports displayed to the user
	 * @throws IOException If no backup of the database can be made this gets thrown.
	 * @throws SQLException If a SQL error occurs this gets thrown.
	 */
	private void updateTo2() throws IOException, SQLException {
		backup(1);

		try {
			database.getConnection().setAutoCommit(false);

			database.executeUpdate("CREATE TABLE IF NOT EXISTS `meta_data` (`id` INTEGER PRIMARY KEY ,"
					+ "`key` VARCHAR NOT NULL UNIQUE , `value` VARCHAR NOT NULL )");
			database.executeUpdate("INSERT INTO `meta_data` (`key`, `value`) VALUES ('version', '2')");
			database.executeUpdate("CREATE TABLE IF NOT EXISTS `enterprise_has_airport` (`id` INTEGER PRIMARY KEY , "
					+ "`enterprise` INTEGER NOT NULL , `airport` INTEGER NOT NULL , "
					+ "FOREIGN KEY (`enterprise`) REFERENCES `enterprises`(`id`) , "
					+ "FOREIGN KEY (`airport`) REFERENCES `airports`(`id`))");

			DatabaseResult enterprises = database.executeQuery("SELECT `id` FROM `enterprises`");
			DatabaseResult airports = database.executeQuery("SELECT `id` FROM `airports`");

			while (enterprises.next()) {
				int e = enterprises.getInt(0);

				while (airports.next()) {
					int a = airports.getInt(0);
					database.executeUpdate("INSERT INTO `enterprise_has_airport` (`enterprise`,`airport`) VALUES ('"
							+ e + "', '" + a + "')");
				}

				airports.beforeFirst();
			}
		} catch (SQLException e1) {
			database.getConnection().rollback();
			throw new SQLException(e1);
		} finally {
			database.getConnection().commit();
			database.getConnection().setAutoCommit(true);
		}
	}

}
