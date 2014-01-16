package generator.randomisers;

import generator.engine.file.Generator;
import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import generator.masterbuild.MasterBuilder;
import generator.misc.EntityEventNodeInterface;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;


public class GenericMemoryAccessRandomiser implements IRandomiserFunctionality{
	Logger logger = Logger.getLogger(IRandomiserFunctionality.class);
		
		Boolean UseParentRef = false; // keep these 
		String columnRef = "";
		String stageReference = "";
		
		
		public void setRandomiserInstance(RandomiserInstance ri) {
			
			LinkedHashMap<String,String> hashMap;
	        hashMap = ri.getProperties();
	        
	        try{
	        		stageReference  =(String) hashMap.get("stageReference");
	        	 if (hashMap.get("columnRef") != null){
	        		 columnRef = hashMap.get("columnRef");
	        	 }
	        	 if (hashMap.get("UseParentRef") != null){
	        		 UseParentRef = Boolean.parseBoolean(hashMap.get("UseParentRef"));
	        	 }
	        }
	        catch(Exception e)
	        {
	            logger.warn(ri.getName() +": Error setting values (2 - ParseInt)", e);
	        }

			
		}

		public Object generate() {
			EntityEventNodeInterface tempNode;
			int parentRef ;
			if(UseParentRef){
				return MasterBuilder.currentNode.getColumnValue(Generator.rowRefId, columnRef, stageReference); //use the current id for the first call in case the value is inside the same entity
			}
			else{
				//choose a random value from the parent
				tempNode = MasterBuilder.headNode.getNode(stageReference); //parent of current stage
				parentRef = new Random().nextInt(tempNode.getStageInstancesCount().intValue());
				return MasterBuilder.currentNode.getColumnValue(parentRef, columnRef, stageReference); // pick a random value between 0 and the total number of entries in the stage
			}
		}

		public void destroy() {

		}

		public boolean isListCompatible() {
			return false;
		}

		public Object generatefromlist(int pos, long numrecs, List<String> dslist) {
			return null;
		}
	}

