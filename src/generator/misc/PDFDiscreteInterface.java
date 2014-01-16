package generator.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import generator.misc.PDFDiscrete;


public class PDFDiscreteInterface {
		
	Logger logger = Logger.getLogger(PDFDiscreteInterface.class);
	
	private HashMap<String, List<String>> valmap = new HashMap<String , List<String>>();
	private HashMap<String, List<String>> scountmap = new HashMap<String , List<String>>();
	private HashMap<String, double []> countmap = new HashMap<String , double []>();
		
	public PDFDiscreteInterface (List <List<String>> vallist) {
		
		String name ;
				
		for (int x=0; x<vallist.get(0).size() ; x++) {
			name = vallist.get(0).get(x).replace(" ", "_") ;
			List<String> tmplist = new ArrayList<String>(); ;
			valmap.put(name, tmplist);			
		}
		
		Set<String> valkeys = valmap.keySet() ;
		Iterator<String> it = valkeys.iterator() ;
		
		while (it.hasNext()) {
			
			 List<String> targetlist = new ArrayList<String>(); ;
			 List<String> scountlist = new ArrayList<String>();;
			 
			String matchKey = it.next(); 
			for (int x=0; x<vallist.get(0).size() ; x++) {
				if (vallist.get(0).get(x).equals(matchKey)) {
					targetlist.add(vallist.get(1).get(x)) ;
					scountlist.add(vallist.get(2).get(x)) ;
				}
			}
			valmap.put(matchKey, targetlist) ;
			scountmap.put(matchKey, scountlist) ;
			
			double [] countlist = new double[scountlist.size()];
			double total = 0;
			for (int i = 0; i < countlist.length; i++) {
				total +=  Double.parseDouble(scountlist.get(i)) ;
			}
			for (int i = 0; i < countlist.length; i++) {
				countlist[i] = (Double.parseDouble(scountlist.get(i)) / total) ;  
			}
			countmap.put(matchKey, countlist);
		}
		
	}
	
	public String getPDF(String match) {
		
		if (countmap.containsKey(match)&& valmap.containsKey(match)) {
			
			double [] countlist = countmap.get(match) ;
			PDFDiscrete pdf = new PDFDiscrete(countlist);
			int pointer = pdf.next() ;
		
			return valmap.get(match).get(pointer) ;
		}
		else {
			return "NULL" ;
		}			
	}	
}

