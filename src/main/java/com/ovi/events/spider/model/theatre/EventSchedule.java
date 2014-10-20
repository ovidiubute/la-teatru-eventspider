package com.ovi.events.spider.model.theatre;

import java.util.Date;

/**
 * 
 * @author ovidiubute
 *
 */
public class EventSchedule {
	private Event event;
	private Long id = new Long(-1);
	private Date timestamp;
	
	@Override
	public String toString() {
		return this.getTimestamp().toString();
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getTimestamp() {
		return timestamp;
	}
}
