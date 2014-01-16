package generator.stagebuild.impl;

import generator.engine.file.StageFileGenerator;
import generator.misc.ApplicationContext;
import generator.misc.DataFileDefinition;
import generator.misc.DataFileItem;
import generator.stagebuild.StageBuilderContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Event Stage builder, to be used for Behaviour or transaction patterning
 * 
 * @author joaoesteves
 * 
 */
public class EventStageBuilder extends AbstractBuilder {

	public EventStageBuilder() {
		super("EventStageBuilder", Logger.getLogger(EventStageBuilder.class));
	}

	public int build() throws StageBuilderConfigurationException {
		// perform generic initializations
		super.build();

		Date runDate = (Date) getCtx().getValObj(StageBuilderContext.RUNDATE);
		ApplicationContext context = ApplicationContext.getInstance();

		vRandomiserTypes = context.getRandomiserTypes();
		vRandomiserInstances = context.getRandomiserInstances();

		Vector<DataFileDefinition> vDFDs = context.getDFD();

		StageFileGenerator generator = new StageFileGenerator(getCtx());

		processStageAwareRandomisers(vRandomiserInstances, runDate);
		generator.setEngineData(vRandomiserTypes, vRandomiserInstances, vDFDs,this.entityEventModel);

		for (int i = 0; i < vDFDs.size(); i++) {
			DataFileDefinition dfd = vDFDs.get(i);
			logger.debug("getName : " + i + " : " + dfd.getName());
			logger.debug("getNumOfRecs : " + i + " : " + dfd.getNumOfRecs());

			for (int ii = 0; ii < dfd.getOutDataItems().size(); ii++) {
				DataFileItem ditem = (DataFileItem) dfd.getOutDataItems().get(
						ii);
				logger.debug("getRandomiserInstanceName : " + ii + " : "
						+ ditem.getRandomiserInstanceName());
			}

			// check if the definitions file loads properly, if not then abort
			// execution
			if (!generator.setFileDefinitionOutput(dfd.getName())) {
				throw new StageBuilderConfigurationException(
						"File definition name " + dfd.getName() + " not found, will do nothing.");
			}

			File outputFile = new File(dfd.getOutFilename());
			String formattedDate = new SimpleDateFormat("yyyyMMdd").format(runDate);
			String outdir = ""+getCtx().getValObj(StageBuilderContext.BASE_FOLDER);
			String outputFileName = outdir + "/" +outputFile+"_"+formattedDate+".txt";
			dfd.setOutFilename(outputFileName);

			logger.info("Begin generation for day "+formattedDate+". Using file definition " + dfd.getName() + ": "+outputFileName);
			generator.generate("event");
		}
		return 0;
	}

}
