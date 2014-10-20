package com.ovi.events.spider.model.theatre;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author ovidiubute
 *
 */
public class Venue {

	private Long id = new Long(-1);
	private Set<Event> events = new HashSet<Event>();
	private City city;
	private String name;
	private float latitude;
	private float longitude;
	private String website;
	private String address;
	private int hidden = 1;
	
	@Override
	public String toString() {
		return this.getName()+" ("+this.getCity().getName()+")";
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(City city) {
		this.city = city;
	}
	/**
	 * @return the city
	 */
	public City getCity() {
		return city;
	}
	/**
	 * @param events the events to set
	 */
	public void setEvents(Set<Event> events) {
		this.events = events;
	}
	/**
	 * @return the events
	 */
	public Set<Event> getEvents() {
		return events;
	}
	public void addEvent(Event event) {
		event.setVenue(this);
		this.events.add(event);
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(int hidden) {
		this.hidden = hidden;
	}
	/**
	 * @return the hidden
	 */
	public int getHidden() {
		return hidden;
	}
}
