package generator.stagebuild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StageBuilderContext {
	public static final String RUNDATE = "rundate";
	public static final String DAYNUM = "daynum";
	public static final String NUMDAYS = "numdays";
	public static final String BASE_FOLDER = "baseFolder";
	public static final String SHARE_FOLDER = "sharedFolder";

	private List<Map<String, Object>> ctxList = new ArrayList<Map<String, Object>>();

	public List<Map<String, Object>> addToCtx(String name, String type,Object obj) {

		Map<String, Object> tmpMap = new HashMap<String, Object>();

		tmpMap.clear();

		tmpMap.put("name", name);
		tmpMap.put("type", type);
		tmpMap.put("value", obj);

		ctxList.add(tmpMap);

		return ctxList;

	}

	public String getValString(String name) {

		String value = "";

		for (int i = 0; i < ctxList.size(); i++) {
			Map<String, Object> mp = ctxList.get(i);
			if (mp.get("name") == name) {
				value = mp.get("value").toString();
			}
		}

		return value;

	}

	public Object getValObj(String name) {

		Object value = "";

		for (int i = 0; i < ctxList.size(); i++) {
			Map<String, Object> mp = ctxList.get(i);
			if (mp.get("name") == name) {
				value = mp.get("value");
			}

		}

		return value;
	}

	
	public void setCtxList(List<Map<String, Object>> ctxList) {
		this.ctxList = ctxList;
	}

	public List<Map<String, Object>> getCtxList() {
		return ctxList;
	}

}
