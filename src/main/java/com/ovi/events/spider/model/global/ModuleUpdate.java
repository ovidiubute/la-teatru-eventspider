package com.ovi.events.spider.model.global;

import java.util.Date;

/**
 * Currently not in use.
 * 
 * @author ovidiubute
 *
 */
public class ModuleUpdate {
	
	private Long id = new Long(-1);
	private String module;
	private String controller;
	private Date lastUpdate;
	private String countryCode;
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
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}
	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}
	/**
	 * @param controller the controller to set
	 */
	public void setController(String controller) {
		this.controller = controller;
	}
	/**
	 * @return the controller
	 */
	public String getController() {
		return controller;
	}
	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
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
}
