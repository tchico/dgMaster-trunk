/*
 * dgMaster: A versatile, open source data generator.
 *(c) 2007 M. Michalakopoulos, mmichalak@gmail.com
 */


package generator.misc;
import generator.db.DBGeneratorDefinition;
import generator.db.DBMetaDataManager;
import generator.db.SQLJavaMapping;
import generator.extenders.RandomiserInstance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;


public class Utils
{
    Logger logger = Logger.getLogger("generator.Utils");
    
    
    public Object createObject(String className)
    {
        Object object = null;
        try
        {
            Class<?> classDefinition = Class.forName(className);
            object = classDefinition.newInstance();
        }
        catch (InstantiationException e)
        {
            logger.error("InstantiationException",e);
        }
        catch (IllegalAccessException e)
        {
            logger.error("IllegalAccessException",e);
        }
        catch (ClassNotFoundException e)
        {
            logger.error("ClassNotFoundException",e);
        }
        return object;
    }
    
    
//    public Vector<RandomiserType> loadRandomiserTypes()
//    {
//        Vector<RandomiserType> vData;
//        RandomDefinitionsBuilder builder = new RandomDefinitionsBuilder();
//        builder.setFilename("SystemDefinitions.xml");
//        builder.setSAXHandler(new RandomTypeSAXHandler() );
//        vData = builder.getElements();
//        
//        return vData;
//    }
    
    @SuppressWarnings("unchecked")
	public Vector<RandomiserType> loadRandomiserTypes(String definitionFile)
    {
        Vector<RandomiserType> vData;
        RandomDefinitionsBuilder builder = new RandomDefinitionsBuilder();
        builder.setFilename(definitionFile);
        builder.setSAXHandler(new RandomTypeSAXHandler() );
        vData = builder.getElements();
        
        return vData;
    }  
    
    public void loadStageDataContextData(String file){
    	SAXParserFactory factory = SAXParserFactory.newInstance();  
        
        logger.debug("About to parse xml file: " + file);
        try
        {
            javax.xml.parsers.SAXParser saxParser;
            saxParser = factory.newSAXParser();
            saxParser.parse( new File(file), new StageDataContextSAXHandler() );
        }
        catch (Throwable err)
        {
            logger.error("Error during parsing xml file: "+err.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
	public Vector<RandomiserInstance> loadRandomiserInstances()
    {
        Vector<RandomiserInstance> vData;
        
        RandomDefinitionsBuilder builder = new RandomDefinitionsBuilder();
        builder.setFilename("Repository.xml");
        builder.setSAXHandler(new RandomInstanceSAXHandler() );
        vData = builder.getElements();
        
        for(int i=0; i<vData.size(); i++)
        {
            RandomiserInstance ri = vData.elementAt(i);
            LinkedHashMap<String,String>  li;
            logger.debug("Name:"+ ri.getName() );
            logger.debug("Description:"+ ri.getDescription() );
            logger.debug("RandomiserType:" + ri.getRandomiserType() );
            li = ri.getProperties();
            
            Set<String> keys;
            if(li!=null)
            {
                keys = li.keySet();

                for(String key : keys)
                {
                    String value = (String) li.get(key);
                    logger.debug("Loaded property (key,value): " + key + "," +value);
                }
            }
            else
            {
                logger.warn("There are no properties for randomiser instance:"+ri.getName());
            }
        }
        return vData;
    }
    
    @SuppressWarnings("unchecked")
	public Vector<RandomiserInstance> loadRandomiserInstances(String repositoryFile)
    {
        Vector<RandomiserInstance> vData;
        
        RandomDefinitionsBuilder builder = new RandomDefinitionsBuilder();
        builder.setFilename(repositoryFile);
        builder.setSAXHandler(new RandomInstanceSAXHandler() );
        vData = builder.getElements();
        
        for(int i=0; i<vData.size(); i++)
        {
            RandomiserInstance ri = vData.elementAt(i);
            LinkedHashMap<String,String>  li;
            logger.debug("Name:"+ ri.getName() );
            logger.debug("Description:"+ ri.getDescription() );
            logger.debug("RandomiserType:" + ri.getRandomiserType() );
            li = ri.getProperties();
            
            Set<String> keys;
            if(li!=null)
            {
                keys = li.keySet();

                for(String key : keys)
                {
                    String value = (String) li.get(key);
                    logger.debug("Loaded property (key,value): " + key + "," +value);
                }
            }
            else
            {
                logger.warn("There are no properties for randomiser instance:"+ri.getName());
            }
        }
        return vData;
    }
    
    @SuppressWarnings("unchecked")
	@Deprecated
    public Vector<DataFileDefinition> loadDataFileDefinitions_old()
    {
        Vector<DataFileDefinition> vData;
        RandomDefinitionsBuilder builder = new RandomDefinitionsBuilder();
        
        builder.setFilename("TextFileDefinitions.xml");
        builder.setSAXHandler(new DataFileOutputSAXHandler() );
        vData = builder.getElements();
        
        return vData;
    }
    
    @SuppressWarnings("unchecked")
	public Vector<DataFileDefinition> loadDataFileDefinitions(String definitionFile)
    {
        Vector<DataFileDefinition> vData;
        RandomDefinitionsBuilder builder = new RandomDefinitionsBuilder();
        
        builder.setFilename(definitionFile);
        builder.setSAXHandler(new DataFileOutputSAXHandler() );
        vData = builder.getElements();
        
        return vData;
    }
    
    
    @SuppressWarnings("unchecked")
	public Vector<DBFileDefinition> loadDBFileDefinitions(String dbFileDefinitionsFolderPath)
    {
        Vector<DBFileDefinition> vData;
        RandomDefinitionsBuilder builder = new RandomDefinitionsBuilder();
        
        if(dbFileDefinitionsFolderPath == null) dbFileDefinitionsFolderPath = ""; 
        builder.setFilename(dbFileDefinitionsFolderPath + "/" + "DBFileDefinitions.xml");
        builder.setSAXHandler(new DBDefinitionSAXHandler() );
        vData = builder.getElements();
        
        return vData;
    }    
    
    @SuppressWarnings("unchecked")
	public Vector<DBDriverInfo> loadDBDriversInfo(String dbDriversFolderPath)
    {
        Vector<DBDriverInfo> vData;
        RandomDefinitionsBuilder builder = new RandomDefinitionsBuilder();
        
        if(dbDriversFolderPath == null) dbDriversFolderPath = ""; 
        builder.setFilename(dbDriversFolderPath + "/" + "DBDrivers.xml");
        builder.setSAXHandler(new DBDriverSAXHandler() );
        vData = builder.getElements();
        
        return vData;
    }


    @SuppressWarnings("unchecked")
	public Vector<SQLJavaMapping> loadSQLJavaMappings(String dbMappingsFolderPath)
    {
        Vector<SQLJavaMapping> vData;
        RandomDefinitionsBuilder builder = new RandomDefinitionsBuilder();

        if(dbMappingsFolderPath == null) dbMappingsFolderPath = ""; 
        builder.setFilename(dbMappingsFolderPath + "/" + "JavaDBMappings.xml");
        builder.setSAXHandler( new JavaDbMappingsSAXHandler() );
        vData = builder.getElements();

        return vData;

    }
    
    /**
     * Finds the properties for this database probider. From there,
     * the driver class and the location where the driver is to be loaded,
     * can be found.
     *
     */
    public DBDriverInfo findDBDriver(String dbProvider, Vector<DBDriverInfo> vDBDriverInfo)
    {
       
        DBDriverInfo dbDriverInfo = null;
        
        boolean found = false;
        int i=0;
        
        logger.debug("Retrieving db driver information:"+dbProvider);
        while( i<vDBDriverInfo.size() && !found)
        {
            dbDriverInfo = vDBDriverInfo.elementAt(i);
            if(dbDriverInfo.getName().equalsIgnoreCase(dbProvider))
            {
                found = true;
            }
            i++;
        }
        
        logger.debug("Retrieving db driver information:"+dbProvider + ". Found:"+ found);
        return dbDriverInfo;
    }
    

    /**
     * Returns a database connection object (or null if there is an error), by:
     * a) extracting the driver name and the jar file from dbDriverInfo
     * b) looking at the specific driver configuration
     * @param dbDriverInfo
     * @param dbGenConfig
     * @return
     */
    public Connection getDBConnection(DBDriverInfo dbDriverInfo, DBGeneratorDefinition dbGenConfig)
    {
        DBMetaDataManager dbMeta = new DBMetaDataManager();
        Connection connection;

        String locationClass, driverClass;
        String url, user, password;

        locationClass = dbDriverInfo.getLocationClass();
        driverClass = dbDriverInfo.getLoadClass();
        url = dbGenConfig.getDbURL();
        user = dbGenConfig.getUser();
        password = dbGenConfig.getPassword();

        dbMeta.setConnectionProperties(locationClass, driverClass, url, user, password);
        try
        {
            dbMeta.connect();
        } catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        } catch (InstantiationException ex)
        {
            ex.printStackTrace();
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        } catch (IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
        connection = dbMeta.getConnection();

        return connection;
    }

    
    public int getRandomiserJavaType(RandomiserInstance ri, Vector<RandomiserType> vRT)
    {
        RandomiserType rt = null;
        
        boolean found = false;
        int i=0;
        
        //logger.debug("Trying to find randomiser type for:"+ri.getRandomiserType());
        while( i<vRT.size() && !found)
        {
            rt = vRT.elementAt(i);
            if(rt.getName().equalsIgnoreCase(ri.getRandomiserType()))
                found = true;
            i++;
        }
        if(found==false)
            logger.fatal("Failed trying to find randomiser type: " + ri.getRandomiserType());
        //logger.debug("Trying to find randomiser type for:"+ri.getRandomiserType() +". Found: "+found+ ", returned type "+ rt.getName() + ","+rt.getJtype());
        return rt.getJtype();
    }
    
    public Vector<String> readFile(String filename) throws FileNotFoundException
    {
        InputStreamReader fileReader = null;
		fileReader = new InputStreamReader(new FileInputStream(filename) , Charset.forName("UTF-8")  );
     
        BufferedReader bufReader = new BufferedReader(fileReader);
        Vector<String> vData = new Vector<String>();
        String line;
        try
        {
            while( (line=bufReader.readLine())!=null )
            {
                if(line.indexOf("//")>=0)
                    continue;
                vData.add(line);
                //logger.info(line);
            }
            fileReader.close();
        }
        catch (IOException ex)
        {
            logger.warn("Error while reading :"+filename,ex);
        }
        finally
        {
            try
            {
                fileReader.close();
            }
            catch(IOException e)
            {
                logger.error("Error closing the file:"+filename,e);
            }
        }
        
        return vData;
    }
    
    public int getDays(GregorianCalendar g1, GregorianCalendar g2)
    {
        int elapsed = 0;
        GregorianCalendar gc1, gc2;
        if (g2.after(g1))
        {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
        }
        else
        {
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }
        gc1.clear(Calendar.MILLISECOND);
        gc1.clear(Calendar.SECOND);
        gc1.clear(Calendar.MINUTE);
        gc1.clear(Calendar.HOUR_OF_DAY);
        gc2.clear(Calendar.MILLISECOND);
        gc2.clear(Calendar.SECOND);
        gc2.clear(Calendar.MINUTE);
        gc2.clear(Calendar.HOUR_OF_DAY);
        while ( gc1.before(gc2) )
        {
            gc1.add(Calendar.DATE, 1);
            elapsed++;
        }
        return elapsed;
    }
    
    public int getDayswithTime(GregorianCalendar g1, GregorianCalendar g2)
    {
        int elapsed = 0;
        GregorianCalendar gc1, gc2;
        if (g2.after(g1))
        {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
        }
        else
        {
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }
        gc1.clear(Calendar.MILLISECOND);
        gc2.clear(Calendar.MILLISECOND);
        while ( gc1.before(gc2) )
        {
            gc1.add(Calendar.DATE, 1);
            elapsed++;
        }
        return elapsed;
    }   
    
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    public ImageIcon createImageIcon(String path)
    {
        URL imgURL = this.getClass().getClassLoader().getResource(path);
        if (imgURL != null)
            return new ImageIcon(imgURL);
        else
        {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static String getErrorMessage(Exception ex)
    {
        StackTraceElement el[] = ex.getStackTrace();
        StringBuffer sb = new StringBuffer();
        sb.append("Error message : " + ex.getMessage()+"\r\n");
        sb.append("Exception type: " + ex.getClass()+"\r\n");
        sb.append("Stack trace follows:\r\n");
        for(int i=0; i<el.length; i++)
        {
            sb.append(el[i].toString());
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
