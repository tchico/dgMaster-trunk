/*
 * JarResources.java
 *
 * Created on 21 May 2007, 00:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package generator.jarloader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

/**
 * JarResources: JarResources maps all resources included in a
 * Zip or Jar file. Additionaly, it provides a method to extract one
 * as a blob.
 */
public final class JarResources
{
    Logger logger = Logger.getLogger(JarResources.class);
    
    // jar resource mapping tables
    private Hashtable<String, Integer> htSizes=new Hashtable<String, Integer>();
    private Hashtable<String, byte[]> htJarContents=new Hashtable<String, byte[]>();
    
    // a jar file
    private String jarFileName;
    
    
    /**
     * creates a JarResources. It extracts all resources from a Jar
     * into an internal hashtable, keyed by resource names.
     * @param jarFileName a jar or zip file
     */
    public JarResources(String jarFileName)
    {
        this.jarFileName=jarFileName;
        init();
    }
    
    /**
     * Extracts a jar resource as a blob.
     * @param name a resource name.
     */
    public byte[] getResource(String name)
    {
        return (byte[])htJarContents.get(name);
    }
    
    /** initializes internal hash tables with Jar file resources.  */
    private void init()
    {
        try
        {
            // extracts just sizes only.
            ZipFile zf=new ZipFile(jarFileName);
            Enumeration<? extends ZipEntry> e=zf.entries();
            while (e.hasMoreElements())
            {
                ZipEntry ze=(ZipEntry)e.nextElement();
                
                logger.debug(dumpZipEntry(ze));
                
                htSizes.put(ze.getName(),new Integer((int)ze.getSize()));
            }
            zf.close();
            
            // extract resources and put them into the hashtable.
            FileInputStream fis=new FileInputStream(jarFileName);
            BufferedInputStream bis=new BufferedInputStream(fis);
            ZipInputStream zis=new ZipInputStream(bis);
            ZipEntry ze=null;
            while ((ze=zis.getNextEntry())!=null)
            {
                if (ze.isDirectory())
                {
                    continue;
                }
                
                logger.debug("ze.getName()="+ze.getName()+
                        ","+"getSize()="+ze.getSize() );
                
                int size=(int)ze.getSize();
                // -1 means unknown size.
                if (size==-1)
                {
                    size=((Integer)htSizes.get(ze.getName())).intValue();
                }
                
                byte[] b=new byte[(int)size];
                int rb=0;
                int chunk=0;
                while (((int)size - rb) > 0)
                {
                    chunk=zis.read(b,rb,(int)size - rb);
                    if (chunk==-1)
                    {
                        break;
                    }
                    rb+=chunk;
                }
                
                // add to internal resource hashtable
                htJarContents.put(ze.getName(),b);
                
                logger.debug( ze.getName()+"  rb="+rb+
                        ",size="+size+
                        ",csize="+ze.getCompressedSize() );
            }
            zis.close();
        }
        catch (NullPointerException e)
        {
            logger.debug("done.");
        }
        catch (FileNotFoundException e)
        {
            logger.debug("File not found", e);
        }
        catch (IOException e)
        {
            logger.debug("IOException", e);
        }
    }
    
    /**
     * Dumps a zip entry into a string.
     * @param ze a ZipEntry
     */
    private String dumpZipEntry(ZipEntry ze)
    {
        StringBuffer sb=new StringBuffer();
        if (ze.isDirectory())
        {
            sb.append("d ");
        }
        else
        {
            sb.append("f ");
        }
        
        if (ze.getMethod()==ZipEntry.STORED)
        {
            sb.append("stored   ");
        }
        else
        {
            sb.append("defalted ");
        }
        
        sb.append(ze.getName());
        sb.append("\t");
        sb.append(""+ze.getSize());
        if (ze.getMethod()==ZipEntry.DEFLATED)
        {
            sb.append("/"+ze.getCompressedSize());
        }
        
        return (sb.toString());
    }        
}	// End of JarResources class.
