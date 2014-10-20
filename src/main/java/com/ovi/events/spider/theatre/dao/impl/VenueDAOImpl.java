package com.ovi.events.spider.theatre.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ovi.events.spider.model.theatre.City;
import com.ovi.events.spider.model.theatre.Venue;
import com.ovi.events.spider.theatre.dao.VenueDAO;

/**
 * 
 * @author ovidiubute
 *
 */
public class VenueDAOImpl extends HibernateDaoSupport implements VenueDAO {

	@Override
	public void addVenue(Venue venue) {
		getHibernateTemplate().save(venue);
	}

	@Override
	public Venue getVenue(Long id) {
		return (Venue) getHibernateTemplate().get(Venue.class, id);
	}

	@Override
	public Venue findVenue(String name, City city) {
		String query = "FROM Venue v WHERE v.name=? and v.city=?";
		@SuppressWarnings("unchecked")
		List<Venue> resultList = getHibernateTemplate().find(query,new Object[] {name,city});
		
		if (resultList != null) {
			if (resultList.size() > 0) {
				return resultList.get(0);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
