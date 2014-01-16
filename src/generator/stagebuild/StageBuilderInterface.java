package generator.stagebuild;



import generator.misc.EntityEventNode;
import generator.misc.EntityEventNodeInterface;
import generator.stagebuild.impl.StageBuilderConfigurationException;

public interface StageBuilderInterface {
	
	//StageBuilderContext ctx ;
	public void setCtx(StageBuilderContext ctx) ;

	public StageBuilderContext getCtx();
		
	public String getname () ;
	
	/* Destroys the stage builder instance  */ 
	public void cleanup() ;
	
	
	/* Build the stage */
	public int build() throws StageBuilderConfigurationException ;

	public void setConfig(StageBuilderConfig stageconfig);
	
	public void setEntityEventModel(EntityEventNodeInterface headNode);

}
