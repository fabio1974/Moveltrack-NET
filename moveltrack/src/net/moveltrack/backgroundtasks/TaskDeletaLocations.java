package net.moveltrack.backgroundtasks;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.moveltrack.dao.LocationDao;
import net.moveltrack.util.Task;

@ApplicationScoped
public class TaskDeletaLocations extends Task  {
	
	@PostConstruct
	public void init() {
	}
	
	@Inject
	LocationDao locationDao;
	
	@Override
    public void run() {
		
		System.out.println("DELATANDO LOCATIONS!");
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		c.add(Calendar.DAY_OF_YEAR,-1);
		locationDao.deleteUntilDateFromLocation2(c.getTime());

		
		Calendar c2 = Calendar.getInstance();
		c2.add(Calendar.DAY_OF_YEAR,-120);
		locationDao.deleteUntilDateFromLocation(c2.getTime());
		
    }
}