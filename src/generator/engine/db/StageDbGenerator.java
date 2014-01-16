package generator.engine.db;

import generator.stagebuild.StageBuilderContext;


/***
 * Stage aware DB generator 
 * @author joaoesteves
 *
 */
public class StageDbGenerator extends Generator {
	StageBuilderContext stageCtx;

	public StageDbGenerator(StageBuilderContext stageCtx) {
		super();
		this.stageCtx = stageCtx;
	}
}
