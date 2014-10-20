package com.ovi.events.spider.theatre.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ovi.events.spider.model.theatre.Event;
import com.ovi.events.spider.model.theatre.EventSchedule;
import com.ovi.events.spider.theatre.dao.EventScheduleDAO;

/**
 * 
 * @author ovidiubute
 *
 */
public class EventScheduleDAOImpl extends HibernateDaoSupport implements
		EventScheduleDAO {

	@Override
	public void addEventSchedule(EventSchedule eventSchedule) {
		getHibernateTemplate().save(eventSchedule);

	}

	@Override
	public EventSchedule getEventSchedule(Long id) {
		return (EventSchedule) getHibernateTemplate().get(EventSchedule.class, id);
	}

	public EventSchedule find(Event event, Date timestamp) {
		String query = "FROM EventSchedule es WHERE es.event=? and es.timestamp=?";
		@SuppressWarnings("unchecked")
		List<EventSchedule> resultList = getHibernateTemplate().find(query,new Object[] {event, timestamp});
		
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
