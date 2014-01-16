/*
 * MetaDataHandler.java
 *
 * Created on 27 October 2006, 21:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package generator.db;
import generator.jarloader.JarClassLoader;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DBMetaDataManager
{
    private Logger logger = Logger.getLogger(DBMetaDataManager.class);
    private String driverLocation;
    private String driverClass;
    private String url, user, password;
    private Connection connection;
    private JarClassLoader jarClassLoader;
    
    public DBMetaDataManager()
    {
    }
    
    public void setConnectionProperties(String driverLocation, String dbClass, String url, String user, String password)
    {
        this.setDriverLocation(driverLocation);
        this.driverClass = dbClass;
        this.url = url;
        this.user = user;
        this.password = password;
        jarClassLoader = new JarClassLoader(driverLocation);
        
        logger.debug("db class:"+this.driverClass + ", url:"+this.url + ", user:" + this.user + ", passwd:"+this.password);
    }
    
    public boolean isConnected()
    {
        boolean isConnected=connection!=null?true:false;
        
        logger.debug("isConnected:"+isConnected);
        return isConnected;
    }
    
    public void connect() throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException
    {
        Class<?> c = jarClassLoader.loadClass(driverClass, true);
        Driver d = (Driver)c.newInstance();
        
        DriverWrapper dw = new DriverWrapper(d);
        DriverManager.registerDriver(dw);
        connection = DriverManager.getConnection(url,user,password);
    }
    
    public Connection getConnection()
    {
        return connection;
    }
    
    public ResultsData<String> getCatalogues()
    {
        List<String> alCatalogs = new ArrayList<String>();
        ResultsData<String> rsdDB = new ResultsData<String>(alCatalogs,0);
        
        DatabaseMetaData dbmd;
        try
        {
            dbmd = connection.getMetaData();
            ResultSet rs = dbmd.getCatalogs();
            while(rs.next())
                rsdDB.add( rs.getString(1) );
        }
        catch (SQLException ex)
        {
            logger.error("SQLException:" + ex.getMessage());
        }
        
        logger.debug("Got DB names,size:"+alCatalogs.size());
        return rsdDB;
    }
    
    
    public ResultsData<String> getSchemas(String catalog)
    {
        
        ArrayList<String> alCatalogs = new ArrayList<String>();
        ResultsData<String> rsdSchema = new ResultsData<String>(alCatalogs,0);
        @SuppressWarnings("unused")
		String sSchema, sCatalogue;
        
        DatabaseMetaData dbmd;
        try
        {
            dbmd = connection.getMetaData();
            
            ResultSet rs = dbmd.getSchemas();
            while(rs.next())
            {
                sSchema    = rs.getString(1);
                sCatalogue = rs.getString(2);
                if(sCatalogue.equalsIgnoreCase(catalog))
                    rsdSchema.add( rs.getString(1) );
            }
        }
        catch (Exception ex)
        {
            logger.error("Error retrieving schemas for catalog:" + catalog,ex);
            rsdSchema.setStatus(Err.QUERY_ERR);
        }
        logger.debug("Got schema names,size:"+alCatalogs.size());
        return rsdSchema;
    }
    
    
    public ResultsData<String> getSchemas()
    {
        
        ArrayList<String> alSchemas = new ArrayList<String>();
        ResultsData<String> rsdSchema = new ResultsData<String>(alSchemas,0);
        
        DatabaseMetaData dbmd;
        try
        {
            dbmd = connection.getMetaData();
            ResultSet rs = dbmd.getSchemas();
            while(rs.next())
                rsdSchema.add( rs.getString(1) );
        }
        catch (SQLException ex)
        {
            logger.error("Error retrieving schemas (without catalog)",ex);
            rsdSchema.setStatus(Err.QUERY_ERR);
        }
        logger.debug("Got schemas names,size:"+alSchemas.size());
        return rsdSchema;
    }
    
    
    
    
    public ResultsData<String> getTables(String db, String schema)
    {
        List<String>   alTables = new ArrayList<String>();
        ResultsData<String> rsdTable = new ResultsData<String>(alTables,0);
        
        DatabaseMetaData dbmd;
        try
        {
            dbmd = connection.getMetaData();
//            String[] tables ={"TABLE"};
            ResultSet rs = dbmd.getTables(db,schema,null,new String[] {"TABLE"});
            while(rs.next())
                rsdTable.add( rs.getString(3) );
        }
        catch (SQLException ex)
        {
            logger.error("Error retrieving tables for db,schema:" + db +","+schema, ex);
            rsdTable.setStatus(Err.QUERY_ERR);
        }
        logger.debug("Got table names,size:"+alTables.size());
        return rsdTable;
    }
    
    
    //1: retrieve tables from catalog,
    //2: retrieve tables from schema
    public ResultsData<String> getTables(int pos, String filter)
    {
        ArrayList<String>   alTables = new ArrayList<String>();
        ResultsData<String> rsdTable = new ResultsData<String>(alTables,0);
        
        DatabaseMetaData dbmd;
        try
        {
            dbmd = connection.getMetaData();
            ResultSet rs;
            if(pos==1)
            {
                rs = dbmd.getTables(filter,null,null,new String[] {"TABLE"});
            }
            else
            {
                rs = dbmd.getTables(null,filter,null,new String[] {"TABLE"});
            }
            while(rs.next())
                rsdTable.add( rs.getString(3) );
        }
        catch (SQLException ex)
        {
            logger.error("Error retrieving tables for db:" + filter, ex);
            rsdTable.setStatus(Err.QUERY_ERR);
        }
        logger.debug("Got table names,size:"+alTables.size());
        return rsdTable;
    }
    
    
    public ResultsData<String> getTablesByVendor()
    {
        ArrayList<String>   alTables = new ArrayList<String>();
        ResultsData<String> rsdTable = new ResultsData<String>(alTables,0);
        String sQuery;
        Statement stmt;
        try
        {
            stmt = connection.createStatement();
            sQuery = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE='TABLE'";
            ResultSet rs = stmt.executeQuery(sQuery);
            while(rs.next())
                rsdTable.add( rs.getString(1) );
        }
        catch (SQLException ex)
        {
            logger.error("Error retrieving tables by vendor", ex);
            rsdTable.setStatus(Err.QUERY_ERR);
        }
        logger.debug("Got table names,size:"+alTables.size());
        return rsdTable;
    }    
    
    
    public DBTable getTableInfo(String catalog, String table)
    {
        DBTable   dbTable=null;
        List<DBField> alDBFields = new ArrayList<DBField>();
        DatabaseMetaData dbmd;
        ResultSet rs;
        try
        {
            dbmd = connection.getMetaData();
            rs   = dbmd.getColumns(catalog,null,table,null);
            
            while(rs.next())
            {
                DBField dbField = new DBField( rs.getString("COLUMN_NAME"),
                        rs.getString("TYPE_NAME"),
                        rs.getInt("COLUMN_SIZE"),
                        rs.getShort("DATA_TYPE"),
                        false);
                if(dbField.getField().toLowerCase().indexOf("id")>0)
                    dbField.setKey(true);
                alDBFields.add(dbField);
                logger.debug("fieldname retrieved:"+ dbField.getField());
            }
            //if there are no fields, it means, this method is not supported
            //try a different one
            if(alDBFields.size()==0)
            {                
                dbTable = getTableInfo(table);
                logger.warn("Getting table info failed for catalog,table:"+catalog+", "+table+". Tried to get info by direct call,fields list size is:"+dbTable.getNumFields());
            }
            else
            {
                logger.debug("columns:"+ alDBFields.size());
                dbTable = new DBTable(table, alDBFields);
            }
            rs.close();
            
            
        }
        catch (SQLException ex)
        {
            logger.error("SQLException:" + ex.getMessage());
        }
        logger.debug("Got field names for table:"+table);
        return dbTable;
    }
    
    
    public DBTable getTableInfo(String table)
    {
        DBTable   dbTable=null;
        List<DBField> alDBFields = new ArrayList<DBField>();
        Statement stmt;
        ResultSet rs;
        try
        {
            stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM "+table +" WHERE 1=0");
            if(rs.next())
            {
                
            }
            ResultSetMetaData rsmd = rs.getMetaData();                        
            
            for(int i=1; i<=rsmd.getColumnCount(); i++)
            {
                DBField dbField = new DBField( rsmd.getColumnName(i), rsmd.getColumnTypeName(i), rsmd.getColumnDisplaySize(i), (short)rsmd.getColumnType(i),false);
                if(dbField.getField().toLowerCase().indexOf("id")>0)
                    dbField.setKey(true);
                alDBFields.add(dbField);
                logger.debug("fieldname retrieved:"+ dbField.getField());
            }
            logger.debug("columns:"+ alDBFields.size());
            dbTable = new DBTable(table, alDBFields);
            rs.close();
            
        }
        catch (SQLException ex)
        {
            logger.error("SQLException:" + ex.getMessage());
        }
        logger.debug("Got field names for table:"+table);
        return dbTable;
    }
    
    
    
    public void disconnect()
    {
        try
        {
            connection.close();
        }
        catch( Exception ex )
        {
            logger.error("Error during connection close:" + ex.getMessage());
        }
        logger.debug("Disconnected!");
    }

	public String getDriverLocation() {
		return driverLocation;
	}

	public void setDriverLocation(String driverLocation) {
		this.driverLocation = driverLocation;
	}
}




class DriverWrapper implements Driver
{
    private Driver driver;
    
    public DriverWrapper(Driver driver)
    {
        this.driver = driver;
    }
    
    public Connection connect(String url, Properties info) throws SQLException
    {
        return driver.connect(url, info);
    }
    
    public boolean acceptsURL(String url) throws SQLException
    {
        return driver.acceptsURL(url);
    }
    
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException
    {
        return driver.getPropertyInfo(url,info);
    }
    
    public int getMajorVersion()
    {
        return driver.getMajorVersion();
    }
    
    public int getMinorVersion()
    {
        return driver.getMinorVersion();
    }
    
    public boolean jdbcCompliant()
    {
        return driver.jdbcCompliant();
    }

	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
    
}