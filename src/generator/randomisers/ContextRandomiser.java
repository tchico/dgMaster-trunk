package generator.randomisers;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ContextRandomiser implements IRandomiserFunctionality{

	Logger logger = Logger.getLogger(ContextRandomiser.class);

	 String varname, vartype ;

	public void setRandomiserInstance(RandomiserInstance ri) {

        LinkedHashMap<String,String> hashMap;

        hashMap = ri.getProperties();
        varname  = (String) hashMap.get("varName");
        vartype  = (String) hashMap.get("varType");

	}

	public Object generate() {
		//TODO: implement generator
		return null;
	}

	public void destroy() {
		//  Auto-generated method stub

	}

	public boolean isListCompatible() {
		return false;
	}

	public Object generatefromlist(int pos, long numrecs, List<String> dslist) {
		//  Auto-generated method stub
		return null;
	}

}
