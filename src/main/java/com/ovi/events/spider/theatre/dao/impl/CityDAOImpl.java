package com.ovi.events.spider.theatre.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ovi.events.spider.model.theatre.City;
import com.ovi.events.spider.theatre.dao.CityDAO;

/**
 * 
 * @author ovidiubute
 *
 */
public class CityDAOImpl extends HibernateDaoSupport implements CityDAO {

	@Override
	public void addCity(City city) {
		getHibernateTemplate().save(city);
	}

	@Override
	public City findCity(String name) {
		String query = "FROM City c WHERE c.name=?";
		@SuppressWarnings("unchecked")
		List<City> resultList = getHibernateTemplate().find(query, name);
		
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

	@Override
	public City getCity(Long id) {
		return (City) getHibernateTemplate().get(City.class, id);
	}

}
