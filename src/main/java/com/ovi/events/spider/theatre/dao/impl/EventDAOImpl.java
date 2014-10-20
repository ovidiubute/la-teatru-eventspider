package com.ovi.events.spider.theatre.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ovi.events.spider.model.theatre.Event;
import com.ovi.events.spider.model.theatre.Venue;
import com.ovi.events.spider.theatre.dao.EventDAO;

/**
 * 
 * @author ovidiubute
 *
 */
public class EventDAOImpl extends HibernateDaoSupport implements EventDAO {

	@Override
	public void addEvent(Event event) {
		getHibernateTemplate().save(event);
	}

	@Override
	public Event getEvent(Long id) {
		return (Event) getHibernateTemplate().get(Event.class, id);
	}

	@Override
	public Event findEvent(String name, Venue venue) {
		String query = "FROM Event e WHERE e.title=? and e.venue=?";
		@SuppressWarnings("unchecked")
		List<Event> resultList = getHibernateTemplate().find(query,new Object[] {name,venue});
		
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
