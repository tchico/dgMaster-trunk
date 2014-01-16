package generator.engine.file;

import generator.stagebuild.StageBuilderContext;


/**
 * Generator extension to include context awareness 
 * @author joaoesteves
 *
 */
public class StageFileGenerator extends Generator {
	StageBuilderContext stageCtx;

	public StageFileGenerator(StageBuilderContext stageCtx) {
		super();
		this.stageCtx = stageCtx;
	}

}
