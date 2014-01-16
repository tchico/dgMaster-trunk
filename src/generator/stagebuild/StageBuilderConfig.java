package generator.stagebuild;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class StageBuilderConfig {
	public static final String PROPERTY_REPOSITORY_SUFFIX = "dgmaster.stagebuild.repository.suffix";
	public static final String PROPERTY_DEFINITION_SUFFIX = "dgmaster.stagebuild.definition.suffix";
	
	private String stagedir ;
	private String shareddir ;
	private Properties prop = new Properties();
	
	
	
	public void setStagedir(String stagedir) {
		this.stagedir = stagedir;
	}
	
	public String getStagedir() {
		return stagedir;
	}
	
	public void setShareddir(String shareddir) {
		this.shareddir = shareddir;
	}
	
	public String getShareddir() {
		return shareddir;
	}
	
	public void setPropertiesfile(String propertiesfile) {
		try {
			prop.load(new FileInputStream(propertiesfile));	
		} catch (FileNotFoundException e) {
			//do nothing, in this case there's no file to be read
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Properties getProp() {
		return prop;
	}

}
