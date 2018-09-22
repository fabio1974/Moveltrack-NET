package com.moveltrack.cartaoprograma.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.moveltrack.cartaoprograma.model.EquipamentoDeApoio;

public class Util {

	public boolean hasDuplicateItem(List<EquipamentoDeApoio> items) {
		for (int i = 0; i < items.size(); i++) { 
			for (int j = i + 1 ; j < items.size(); j++){ 
				if (items.get(i).getDescricao().equals(items.get(j).getDescricao())) { 
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isHistorico(Date t1){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR,-1);
		Date d = setTime(c.getTime(),0,0,0,0);
		return !t1.after(d);
	}
	
	public static Date setTime( final Date date, final int hourOfDay, final int minute, final int second, final int ms )
	{
	    final GregorianCalendar gc = new GregorianCalendar();
	    gc.setTime( date );
	    gc.set( Calendar.HOUR_OF_DAY, hourOfDay );
	    gc.set( Calendar.MINUTE, minute );
	    gc.set( Calendar.SECOND, second );
	    gc.set( Calendar.MILLISECOND, ms );
	    return gc.getTime();
	}

}
