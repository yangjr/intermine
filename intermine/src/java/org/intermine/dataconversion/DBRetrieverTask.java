package org.intermine.dataconversion;

/*
 * Copyright (C) 2002-2004 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import org.intermine.metadata.Model;
import org.intermine.objectstore.ObjectStoreWriter;
import org.intermine.objectstore.ObjectStoreWriterFactory;
import org.intermine.sql.Database;
import org.intermine.sql.DatabaseFactory;
import org.intermine.task.ConverterTask;

import org.apache.tools.ant.BuildException;

/**
 * Initiates retrieval and conversion of data from a source database
 *
 * @author Andrew Varley
 * @author Mark Woodbridge
 * @author Matthew Wakeling
 */
public class DBRetrieverTask extends ConverterTask
{
    protected String database;

    /**
     * Set the database name
     * @param database the database name
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Run the task
     * @throws BuildException if a problem occurs
     */
    public void execute() throws BuildException {
        if (database == null) {
            throw new BuildException("database attribute is not set");
        }
        if (model == null) {
            throw new BuildException("model attribute is not set");
        }

        ObjectStoreWriter osw = null;
        ItemWriter writer = null;
        try {
            Database db = DatabaseFactory.getDatabase(database);
            Model m = Model.getInstanceByName(model);
            osw = ObjectStoreWriterFactory.getObjectStoreWriter(osName);
            writer = new ObjectStoreItemWriter(osw);
            DBReader reader = new ReadAheadDBReader(db, m);
            System.err .println("Processing data from DB " + db.getURL());
            new DBConverter(m, db, reader, writer).process();
            reader.close();
            doSQL(osw.getObjectStore());
        } catch (Exception e) {
            throw new BuildException(e);
        } finally {
            try {
                writer.close();
                osw.close();
            } catch (Exception e) {
                throw new BuildException(e);
            }
        }
    }
}
