package com.ovi.events.spider.model.theatre;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author ovidiubute
 *
 */
public class Event {
	
	private Long id = new Long(-1);
	private Venue venue;
	private String title;
	private String detailsStory;
	private String detailsTechnical;
	private String detailsCast;
	private String author;
	private String ticketPrice;
	private String hall;
	private Set<EventSchedule> eventSchedules = new HashSet<EventSchedule>();

	public String toString() {
		return this.getTitle()+" ("+this.getVenue().getName()+")";
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @param venue the venue to set
	 */
	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	/**
	 * @return the venue
	 */
	public Venue getVenue() {
		return venue;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param detailsStory the detailsStory to set
	 */
	public void setDetailsStory(String detailsStory) {
		this.detailsStory = detailsStory;
	}
	/**
	 * @return the detailsStory
	 */
	public String getDetailsStory() {
		return detailsStory;
	}
	/**
	 * @param detailsTechnical the detailsTechnical to set
	 */
	public void setDetailsTechnical(String detailsTechnical) {
		this.detailsTechnical = detailsTechnical;
	}
	/**
	 * @return the detailsTechnical
	 */
	public String getDetailsTechnical() {
		return detailsTechnical;
	}
	/**
	 * @param detailsCast the detailsCast to set
	 */
	public void setDetailsCast(String detailsCast) {
		this.detailsCast = detailsCast;
	}
	/**
	 * @return the detailsCast
	 */
	public String getDetailsCast() {
		return detailsCast;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param ticketPrice the ticketPrice to set
	 */
	public void setTicketPrice(String ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	/**
	 * @return the ticketPrice
	 */
	public String getTicketPrice() {
		return ticketPrice;
	}
	/**
	 * @param hall the hall to set
	 */
	public void setHall(String hall) {
		this.hall = hall;
	}
	/**
	 * @return the hall
	 */
	public String getHall() {
		return hall;
	}
	public void setEventSchedules(Set<EventSchedule> eventSchedules) {
		this.eventSchedules = eventSchedules;
	}
	public Set<EventSchedule> getEventSchedules() {
		return eventSchedules;
	}

}
