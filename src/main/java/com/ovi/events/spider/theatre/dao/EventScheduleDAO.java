package com.ovi.events.spider.theatre.dao;

import com.ovi.events.spider.model.theatre.EventSchedule;

/**
 * 
 * @author ovidiubute
 *
 */
public interface EventScheduleDAO {
	public abstract void addEventSchedule(EventSchedule eventSchedule);
	public abstract EventSchedule getEventSchedule(Long id);
}