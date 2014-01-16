package generator.gui;

import generator.misc.ApplicationContext;
import generator.stagebuild.StageBuilderConfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;

/**
 * Toolbar menu stage selection dropdown Action Listener
 * @author joaoesteves
 *
 */
public class StageSelectionActionListener implements ActionListener {
	private List<StageBuilderConfig> stagesConfig;
	private MainForm mainFormRef;

	public StageSelectionActionListener(List<StageBuilderConfig> stagesConfig,  MainForm mainFormRef) {
		this.stagesConfig = stagesConfig; 
		this.mainFormRef =  mainFormRef;
	}

	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		String stageName = (String) cb.getSelectedItem();
		
		if(stageName != null){
			int stageNumber;
			try {
				stageNumber = Integer.parseInt(stageName.substring(stageName.length()-1));
				updateTreeBrowser(stageNumber);
			} catch (NumberFormatException e1) {
				//invalid request, so do nothing
			}
			
		}
	}

	private void updateTreeBrowser(int stageId) {
		ApplicationContext.setBaseConfigurationFolder(stagesConfig.get(stageId-1).getStagedir()+"/conf");
		mainFormRef.initFileConfiguration();
		mainFormRef.refreshTree();
		mainFormRef.refreshMainFormWindowTitle();
	}

}
