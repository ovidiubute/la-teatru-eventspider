package com.ovi.events.spider.model.theatre;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author ovidiubute
 *
 */
public class City {

	private Long id = new Long(-1);
	private String name;
	private float latitude;
	private float longitude;
	private String countryCode;
	private int hidden = 1;
	private Set<Venue> venues = new HashSet<Venue>();
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
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
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
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
	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}
	/**
	 * @param venues the venues to set
	 */
	public void setVenues(Set<Venue> venues) {
		this.venues = venues;
	}
	/**
	 * @return the venues
	 */
	public Set<Venue> getVenues() {
		return venues;
	}
}
