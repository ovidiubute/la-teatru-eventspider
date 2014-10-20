package com.ovi.events.spider.theatre.dao;

import com.ovi.events.spider.model.theatre.City;

/**
 * 
 * @author ovidiubute
 *
 */
public interface CityDAO {
	public abstract void addCity(City city);
	public abstract City findCity(String name);
	public abstract City getCity(Long id);
}
