/*
 * dgMaster: A versatile, open source data generator.
 *(c) 2007 M. Michalakopoulos, mmichalak@gmail.com
 */


package generator.misc;

import generator.gui.FileOutputPanel;
import generator.stagebuild.StageBuilderConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Michael
 * Saves a data file definitions vector in TextFileDefinitions.xml
 * When a collection of new classes needs to be saved in an XML file, 
 * a similar class will have to be provided and the saveData method 
 * will have to be called. It is hard to convert these classes into some kind 
 * of design pattern, because each class will actually provide its own XML structure.
 */
public class FileOutDataSaver
{
    private Logger logger = Logger.getLogger(FileOutDataSaver.class);
    private XMLSaver xmlSaver;
    private Document dom=null;
    private Element  root;
    
    
    /** 
     * Saves the data to the XML file. This is the method that the caller will
     * actually call to save the DataDefinition data. When a collection of new
     * classes needs to be saved in an XML file, a similar class will have to be
     * provided and the saveData method will have to be called. It is hard to 
     * convert these classes into some kind of design pattern, becase each 
     * class will actually provide its own XML structure.
     *
     * <p> Preconditions: Vector contains valid DataFileDefinition objects
     * <p> Post-effects: Contents of vector are saved in TextFileDefinitions.xml
     *                   CAUTION: Any actual data existing in file will be overwritten!!!
     *
     * @param vData a Vector of DataDefinition objects to be saved in file TextFileDefinitions.xml
     * @param fileOutputPanel 
     */
    public void saveData(Vector<DataFileDefinition> vData, FileOutputPanel fileOutputPanel)
    {
        //[*] maybe i can get the root element out of the dom?
        xmlSaver = new XMLSaver();
        dom = xmlSaver.createDocument("datafile-definitions");
        root = dom.getDocumentElement();
        
        //all data will be linked here
        Element elemDataFileDefinition;
        
        //each dataFileDefinition has a vector linked to it. The vector has
        //DataFileItem objects
        DataFileDefinition dataFileDefinition;
        Vector<DataFileItem> vDataFileItems;
        DataFileItem dataItem;
                
        for(int i=0; i<vData.size(); i++)
        {
            dataFileDefinition     = vData.elementAt(i);
            elemDataFileDefinition = addElementDFD(dataFileDefinition);
            addElementDFDDescription(elemDataFileDefinition, dataFileDefinition);
            vDataFileItems = dataFileDefinition.getOutDataItems();
            for(int j=0; j<vDataFileItems.size(); j++)
            {
                dataItem = vDataFileItems.elementAt(j);
                addDataItem(elemDataFileDefinition,dataItem);
            }
        }        
        
        //try to find the file and if not launch the file picker
        File chosenFile = FileOutDataSaver.confirmReplacement(fileOutputPanel, "Definitions.xml");
        
        //save the file
        xmlSaver.writeXMLContent(dom, chosenFile.getAbsolutePath());
    }
    

    private Element addElementDFDDescription(Element elemDFD, DataFileDefinition dfd)
    {
        Element description = dom.createElement("description");
        description.setTextContent(dfd.getDescription());
        elemDFD.appendChild(description);
        logger.debug("description: "+dfd.getDescription());
        return elemDFD;
    }    
    
    private Element addElementDFD(DataFileDefinition dfd)
    {
        Element elemDFD = dom.createElement("File-output-definition");
        elemDFD.setAttribute("name",dfd.getName());
        elemDFD.setAttribute("filename",dfd.getOutFilename());
        elemDFD.setAttribute("delimiter",dfd.getDelimiter());
        elemDFD.setAttribute("numOfRecs",""+dfd.getNumOfRecs());
        root.appendChild(elemDFD);
        logger.debug("File-output-definition: "+dfd.getName());
        return elemDFD;
    }

    
    private void addDataItem(Element elemDFD, DataFileItem dataItem)
    {
        Element elemDataItem = dom.createElement("data-item");
        elemDataItem.setAttribute("randomiser-instance",dataItem.getRandomiserInstanceName());
        elemDataItem.setAttribute("width",""+dataItem.getWidth());
        elemDataItem.setAttribute("encloseChar",dataItem.getEncloseChar());
        elemDataItem.setAttribute("alignment",""+dataItem.getAlignment());
        elemDataItem.setAttribute("order", ""+dataItem.getOrder());
        
        elemDFD.appendChild(elemDataItem);
        logger.debug("added a data-item element");        
    }   
    
    
    
    /**
     * searches in the base config folder for files suffixed as the parameter, and launches a confirm replacement popup
     * in case it's the only one.If it's not the only file, or an error finding files occurrs, then it launches the file picker window.
     * @param sourcePanel
     * @param searchSuffixString
     * @return choosen file
     */
    public static File confirmReplacement(JPanel sourcePanel, String searchSuffixString){
    	 String baseConfigurationFolder = ApplicationContext.getBaseConfigurationFolder();
         File[] propertiesFiles;
         String suffix = null;
 		try {
 			propertiesFiles = ApplicationContext.findFileInFolder(baseConfigurationFolder, ".properties");
 			if(propertiesFiles.length > 0){
 	        	Properties propsFile = new Properties();
 	        	try {
 					propsFile.load(new FileInputStream(propertiesFiles[0]));
 					suffix = propsFile.getProperty(StageBuilderConfig.PROPERTY_DEFINITION_SUFFIX);
 		    		suffix = (suffix != null && suffix.isEmpty())?null:suffix;
 				} catch (FileNotFoundException e) {
 					e.printStackTrace();
 				} catch (IOException e) {
 					e.printStackTrace();
 				}
 	        }
 		} catch (FileNotFoundException e1) {
 			e1.printStackTrace();
 		}
         
         File chosenFile = null;
         boolean openFindFile = true;
         try {
 			File[] foundFiles = ApplicationContext.findFileInFolder(baseConfigurationFolder, (suffix != null)?suffix:searchSuffixString);
 			if(foundFiles.length == 1){
 				int reply = JOptionPane.showConfirmDialog(null, "Do you want to replace "+foundFiles[0].getName()+"?", "Close?",  JOptionPane.YES_NO_OPTION);
 				if (reply == JOptionPane.YES_OPTION)
 				{
 					chosenFile = foundFiles[0];
 					openFindFile = false;
 				}
 			}
 		} catch (FileNotFoundException e) {
 			e.printStackTrace();
 		}
         
         if(openFindFile){
 			//Create a file chooser
 	    	final JFileChooser fc = new JFileChooser();
 	    	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
 	    	fc.setCurrentDirectory(new File(baseConfigurationFolder));
 	    	fc.showOpenDialog(sourcePanel);
 	    	chosenFile = fc.getSelectedFile();
 		}
         return chosenFile;
    }
    
}
