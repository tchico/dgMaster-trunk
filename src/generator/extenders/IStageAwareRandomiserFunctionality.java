package generator.extenders;

import java.util.Date;


/**
 * 
 * @author joaoesteves
 *
 */
public interface IStageAwareRandomiserFunctionality extends
		IRandomiserFunctionality {
	public Date getRunDate();

	public void setRunDate(Date runDate);
}
