package generator.randomisers;



import generator.engine.file.Generator;
import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import generator.masterbuild.MasterBuilder;
import generator.misc.EntityEventNodeInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class StageAmountRandomiser  implements IRandomiserFunctionality{

    Logger logger = Logger.getLogger(DoubleListRandomiser.class);
    ArrayList<String> stages = new ArrayList<String>();
    HashMap stageColumns = new HashMap();
    HashMap stageValues = new HashMap();
    ArrayList<String> stageRandomisers = new ArrayList<String>();
    String keyStage = "";
    
    ArrayList<IRandomiserFunctionality> randomisers = new ArrayList<IRandomiserFunctionality>(); // targeted randomisers
    Boolean listCompatiable;
    /**
     * Creates a new instance of DoubleListRandomiser
     */
    public StageAmountRandomiser()
    {

    }
    
    public void setRandomiserInstance(RandomiserInstance ri)
    {        
        LinkedHashMap<String,String> hashMap;
        
        hashMap = ri.getProperties();
        
        keyStage = hashMap.get("keyStage"); // the stage youre building againist
        
        int exit = 0; int ext =0;
        if(hashMap.containsKey("stage0")){
    		exit++;
    	}
        
        for(int i = 0; i < exit; i++ ){
        	stages.add(hashMap.get("stage"+i));
        	
        	if(hashMap.containsKey("stage"+i+"column0")){
        		ext++;
        		for(int k = 0; k < ext;k++){
        			stageColumns.put("stage"+i+"column"+k, hashMap.get("stage"+i+"column"+k));
        			stageValues.put("stage"+i+"value"+k, hashMap.get("stage"+i+"value"+k));
        			if(hashMap.containsKey("stage"+i+"column"+ext)){
                		ext++;
        			}
        		}
        	}
        	if(hashMap.containsKey("stage"+exit)){
        		exit++;
        	}
        }
        if(exit > 0){
        	
        }
       
    }
    
    public Object generate() // will be returning an integer
    {
    	int instanceId = Generator.rowRefId;
    	int exit;
    	for(int i = 0; i < stages.size(); i++){
    		exit = 0;
    		if(stageColumns.containsKey("stage"+i+"column0")){
        		exit++;
    		}
    		for(int k = 0; k < exit;k++){
    			if(stages.get(i).equalsIgnoreCase(keyStage)){
    				if(  MasterBuilder.headNode.getNode(keyStage).getColumnValue(instanceId, (String) stageColumns.get("stage"+i+"column"+k), stages.get(i)).equalsIgnoreCase((String) stageValues.get("stage"+i+"value"+k)) ){
    					return ""+randomisers.get(i).generate();
    				}
    			}
    			else{ //otherwise we need to find how these nodes are connected, so cannot just use instanceId
    				 logger.debug("the stage name " + stages.get(i) +" and key stage "+keyStage +" do not match!!");
    				 boolean foundConnection = false;
    				 EntityEventNodeInterface temp = MasterBuilder.headNode.getNode(keyStage); 
    				 while(foundConnection == false){
    					 if(temp.getParent().getName().equalsIgnoreCase(stages.get(i))){ // found the parent
    						instanceId = temp.getStageInstance(instanceId).getParentRefId();
    						foundConnection = true;
    					 }
    					 else{ // not the parent, so go up a step and repeat
    						 instanceId = temp.getStageInstance(instanceId).getParentRefId();
    						 temp = temp.getParent();
    					 }
    				 }
    				 // after the above loop I should now have the instanceId thats connected to the keystage.
    				 
    				 if(  MasterBuilder.headNode.getNode(stages.get(i)).getColumnValue(instanceId, (String) stageColumns.get("stage"+i+"column"+k), stages.get(i)).equalsIgnoreCase((String) stageValues.get("stage"+i+"value"+k)) ){
     					return ""+randomisers.get(i).generate();
     				}
    			}
    			if(stageColumns.containsKey("stage"+i+"column"+k+1)){
            		exit++;
        		}
    		}
    		
    	}
    	return null;
    }
    
    public void addTargetRandomiser(IRandomiserFunctionality targetRandomiser){
    	this.randomisers.add(targetRandomiser);
    }
    
    public String getKeyStageName(){
    	return keyStage;
    }
    
    public void destroy()
    {
    }

	public boolean isListCompatible() {
		return false;
	}

	public Object generatefromlist(int pos, long numrecs, List<String> dslist) {
		return null;
    
	}
}




