package com.iugu.utils;

import com.iugu.model.Event;
import com.iugu.model.Trigger;

public abstract class Utils {
	public static String booleanToString(boolean Value) {
		return Boolean.toString(Value);
	}
	public static Trigger stringToTrigger(String gatilho){
		System.out.println(gatilho);
		Trigger trigger = new Trigger();
    	String[] s = gatilho.split("&");
    	for (String linha : s) {
    		if(linha.startsWith("event")){
    			String e = linha.substring(linha.indexOf("event=")+6);
    			e = e.replace(".","_");
    			trigger.setEvent(Event.valueOf(e));
    			System.out.println(trigger.getEvent().getValue());
    		}	
    		if(linha.startsWith("data")){
    			String key = linha.substring(linha.indexOf("data%5B")+7,linha.indexOf("%5D"));
    			String value = linha.substring(linha.indexOf("=")+1);
    			trigger.getData().put(key, value);
    			System.out.println("data="+key +"=>" + value + ";");
    		}
		}
    	return trigger;
	}
	
	public static String formatString(String string, String mask) throws java.text.ParseException {
		javax.swing.text.MaskFormatter mf = new javax.swing.text.MaskFormatter(mask);
		mf.setValueContainsLiteralCharacters(false);
		return mf.valueToString(string);
	}
}
