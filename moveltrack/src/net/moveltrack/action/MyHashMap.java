package net.moveltrack.action;

import java.util.HashMap;

public class MyHashMap extends HashMap<String, Object> {

	@Override
	public Object put(String key,Object value){
		if(value!=null)
			return super.put(key, value);
		return null;
	}

}
