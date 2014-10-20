package com.ovi.events.spider.theatre.dao;

import com.ovi.events.spider.model.theatre.Event;
import com.ovi.events.spider.model.theatre.Venue;

/**
 * 
 * @author ovidiubute
 *
 */
public interface EventDAO {
	public abstract void addEvent(Event event);
	public abstract Event getEvent(Long id);
	public abstract Event findEvent(String name, Venue venue);
}
