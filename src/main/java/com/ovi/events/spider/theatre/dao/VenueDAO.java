package com.ovi.events.spider.theatre.dao;

import com.ovi.events.spider.model.theatre.City;
import com.ovi.events.spider.model.theatre.Venue;

/**
 * 
 * @author ovidiubute
 *
 */
public interface VenueDAO {
	public abstract void addVenue(Venue venue);
	public abstract Venue getVenue(Long id);
	public abstract Venue findVenue(String name, City city);
}