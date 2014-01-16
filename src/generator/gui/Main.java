/*
 * dgMaster: A versatile, open source data generator.
 *(c) 2007 M. Michalakopoulos, mmichalak@gmail.com
 */



/*
 * Main.java
 *
 * Created on 26 January 2007, 00:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package generator.gui;

import generator.misc.ApplicationContext;

import org.apache.log4j.xml.DOMConfigurator;


public class Main
{
    
    /** Creates a new instance of Main */
    public Main()
    {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        
        
        // optionally we can start the gui under a different configuration folder
    	String definitionsConfigFolder ="";
    	
    	if (args.length > 0 ) {
    		definitionsConfigFolder = args[0];
    		if(definitionsConfigFolder.equals(".")){
    			definitionsConfigFolder = "";
    		}else if(!definitionsConfigFolder.endsWith("/")){
    			definitionsConfigFolder=definitionsConfigFolder+"/";
    		}
    	}
    	
    	DOMConfigurator.configure(definitionsConfigFolder+"generator.xml");
    	ApplicationContext.setDefinitionsConfigurationFolder(definitionsConfigFolder);
        
    	
    	//Start the GUI
        MainForm frmMain = new MainForm();
        
        frmMain.setVisible(true);
    }
    
}
