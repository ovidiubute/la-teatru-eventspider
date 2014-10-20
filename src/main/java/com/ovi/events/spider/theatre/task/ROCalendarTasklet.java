package com.ovi.events.spider.theatre.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftConditionalCommentTagTypes;
import net.htmlparser.jericho.PHPTagTypes;
import net.htmlparser.jericho.Source;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ovi.events.spider.model.theatre.City;
import com.ovi.events.spider.model.theatre.Event;
import com.ovi.events.spider.model.theatre.EventSchedule;
import com.ovi.events.spider.model.theatre.Venue;
import com.ovi.events.spider.theatre.dao.impl.CityDAOImpl;
import com.ovi.events.spider.theatre.dao.impl.EventDAOImpl;
import com.ovi.events.spider.theatre.dao.impl.EventScheduleDAOImpl;
import com.ovi.events.spider.theatre.dao.impl.VenueDAOImpl;

/**
 * Tasklet to retrieve calendar of events for given time period.
 * It also retrieves new Cities and Venues if they are not already in the db.
 * 
 * @author ovidiubute
 *
 */
public class ROCalendarTasklet implements Tasklet {

	private String urlParamPattern;
	private String dateDayPattern;
	private String dateTimePattern;
 
	private static final Logger LOGGER = LoggerFactory.getLogger(ROCalendarTasklet.class);
	
	private ClassPathXmlApplicationContext appContext;
	
	private String url;
	private URL preparedUrl;
	private Source source;
	
	Map<String, Object> params = new HashMap<String, Object>();
	
	public void setDateDayPattern(String dateDayPattern) {
		this.dateDayPattern = dateDayPattern;
	}

	public String getDateDayPattern() {
		return dateDayPattern;
	}

	public void setDateTimePattern(String dateTimePattern) {
		this.dateTimePattern = dateTimePattern;
	}

	public String getDateTimePattern() {
		return dateTimePattern;
	}

	public void setUrlParamPattern(String urlParamPattern) {
		this.urlParamPattern = urlParamPattern;
	}

	public String getUrlParamPattern() {
		return urlParamPattern;
	}

	public ROCalendarTasklet() {
		appContext = new ClassPathXmlApplicationContext("ApplicationContext.xml");
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		long start = System.currentTimeMillis();
		LOGGER.info("Running step {}", chunkContext.getStepContext().getStepName());
		
		Map<String, Object> paramsOriginal = chunkContext.getStepContext().getJobParameters();
		
	    params.putAll(paramsOriginal);
		
		try {
			this.prepareUrl(params);
			LOGGER.info("Creating source tree from URL {}", this.preparedUrl.toExternalForm());
			this.source = this.getSource();
			this.saveEvents();
		} catch (Exception e) {
			throw e;
		}
		
		long stop = System.currentTimeMillis();
	    LOGGER.info("Step '{}' has finished successfully in {} seconds.", chunkContext.getStepContext().getStepName(), Long.valueOf((stop - start) / 1000L));

		return RepeatStatus.FINISHED;
	}
	
	/**
	 * Parses the HTML, retrieves events and saves them to the database.
	 * @throws Exception
	 */
	private void saveEvents() throws Exception {
		// Safety checks for tags
		MicrosoftConditionalCommentTagTypes.register();
		PHPTagTypes.register();
		MasonTagTypes.register();
		
		// DAO
		CityDAOImpl cityDAOImpl = (CityDAOImpl) appContext.getBean("cityDAOTarget");
		EventDAOImpl eventDAOImpl = (EventDAOImpl) appContext.getBean("eventDAOTarget");
		EventScheduleDAOImpl eventScDAOImpl = (EventScheduleDAOImpl) appContext.getBean("eventScheduleDAOTarget");
		VenueDAOImpl venueDAOImpl = (VenueDAOImpl) appContext.getBean("venueDAOTarget");
		
		// Traverse all table rows in search for venues and events
		Venue venue = null;
		List<Element> elementList=this.source.getAllElements("tr");
		// element is a table row (tr)
		for (Element mainTRElement : elementList) {
			// We search for the venue, then the events
			List<Attribute> attributeList = mainTRElement.getAttributes();
			List<Element> childList = null;
			// Table rows must have onmouseout="this.className=''" attribute!
			if (attributeList.size() > 0 &&
				mainTRElement.getAttributeValue("onmouseout") != null &&
				mainTRElement.getAttributeValue("onmouseout").equals("this.className=''")) {
				
				// children of the table row (tr) element
				childList = mainTRElement.getChildElements();
				if (childList.size() > 0) {
					// Attributes of the first child (td)
					List<Attribute> childAttributeList = childList.get(0).getAttributes();
					// Rows with just venues!
					if (childAttributeList.get(0).getName().equals("colspan") && 
						childAttributeList.get(0).getValue().equals("2")) {
						// Event!
						LOGGER.debug("Found Venue/City! {}", mainTRElement.getTextExtractor().toString());
						
						// Traverse the HTML tree to find the href element (venue name) and the city (not in HTML element at all)
						Element td = (Element) childList.get(0);
						List<Element> tdChildList = td.getChildElements();
						Element div = tdChildList.get(0);
						List<Element> divChildList = div.getChildElements();
						Element span = divChildList.get(0);
						List<Element> spanChildList = span.getChildElements();
						Element href = spanChildList.get(0);
						
						// Get venue name!
						String venueName = href.getTextExtractor().toString();
						// Get city name!... and remove first 3 characters ( - )
						String cityName = div.getTextExtractor().toString().substring(venueName.length()+3);
						
						City foundCity = cityDAOImpl.findCity(cityName);
						City city = null;
						if (foundCity == null) {
							City newCity = new City();
							newCity.setName(cityName);
							newCity.setCountryCode("RO");
							
							try {
								cityDAOImpl.addCity(newCity);
								cityDAOImpl.getHibernateTemplate().flush();
								LOGGER.info("Added city {}", newCity);
							} catch (Exception ex) {
								LOGGER.error("Failed to add city {}!",newCity);
							}
							
							city = newCity;
						} else {
							LOGGER.debug("City {} found in database", cityName);
							city = foundCity;
						}
						
						Venue foundVenue = venueDAOImpl.findVenue(venueName, city);
						if (foundVenue == null) {
							Venue newVenue = new Venue();
							newVenue.setName(venueName);
							newVenue.setCity(city);
							
							// Add to db...
							venueDAOImpl.addVenue(newVenue);
							venueDAOImpl.getHibernateTemplate().flush();
							LOGGER.info("Venue {} added to database",newVenue);
							// Use it to add events...
							venue = newVenue;
							
						} else {
							venue = foundVenue;
						}
					} else {
						// Only add the event schedule if the venue is NOT HIDDEN
						// The venues will be manually enabled later by an admin
						if (venue.getHidden() == 0) {
							// Table row with event!
							String eventName = null;
							childList = mainTRElement.getChildElements();
							if (childList.size() == 2) {
								// First td contains event name
								Element eventNameTableData = childList.get(0);
								if (eventNameTableData.getAttributeValue("class") != null &&
									eventNameTableData.getAttributeValue("class").equals("e_title_box3")) {
									/* Contents can be:
										1) Anchor element with name
										2) Anchor element with name + other elements with suplimentary info
										3) Just text with name (no elements at all)
										4) Text + other elements with suplimentary info
									*/
									List<Element> eventNameTableDataChildren = eventNameTableData.getChildElements();
									boolean found = false;
									// Case 3
									if (eventNameTableDataChildren.size() == 0) {
										eventName = eventNameTableData.getTextExtractor().toString();
									} else {
										int index = 0;
										int extraElementsLength = 0;
										for (Element child : eventNameTableDataChildren) {
											if (!found) {
												// Case 1 && 2
												if (child.getStartTag().getName().equals("a") && index == 0) {
													eventName = child.getTextExtractor().toString();
													LOGGER.debug("Found event title: {}", eventName);
													found = true;
												} else {
													// We have not found the element on the first position, this means it is pure text
													// Get the info using substring: but calculate the length of every other element
													extraElementsLength += child.getTextExtractor().toString().length();
												}
												index++;
											}
										}
										// If we haven't found it yet ... it could be case 4
										if (!found && extraElementsLength >= 0 && index >= 1) {
											String childText = eventNameTableData.getTextExtractor().toString(); 
											eventName = childText.substring(0, childText.length() - extraElementsLength);
										}
									}
								}
								
								// Do we have this event already in db?
								Event event = eventDAOImpl.findEvent(eventName, venue);
								if (event == null) {
									event = new Event();
									event.setTitle(eventName);
									event.setVenue(venue);
									
									eventDAOImpl.addEvent(event);
									eventDAOImpl.getHibernateTemplate().flush();
									LOGGER.info("Added event {}", event);
								} else {
									LOGGER.debug("Event {} already exists in db.", event);
								}
								
								// Second td contains datetime (at least one!)
								HashSet<EventSchedule> fullSchedule = new HashSet<EventSchedule>();
								Element eventTimeTableData = childList.get(1);
								if (eventTimeTableData.getAttributeValue("class") != null &&
									eventTimeTableData.getAttributeValue("class").equals("e_date3")) {
									
									List<Element> eventTimeTableDataChildren = eventTimeTableData.getChildElements();
									for (Element child : eventTimeTableDataChildren) {
										if (child.getStartTag().getName().equals("span")) {
											if (child.getAttributeValue("class").startsWith("event_")) {
												String rawTime = child.getTextExtractor().toString().trim();
												
												// Parse day
												String day = getMatchFromPattern(dateDayPattern, rawTime);
												// Parse hour
												String hour = getMatchFromPattern(dateTimePattern, rawTime).substring(0,2);
												// Parse minute
												String minute = getMatchFromPattern(dateTimePattern, rawTime).substring(3,5);
												
												Calendar cal = Calendar.getInstance();
												cal.set(Integer.parseInt((String)params.get("year")), 
														Integer.parseInt((String)params.get("month")), 
														Integer.parseInt(day), 
														Integer.parseInt(hour), 
														Integer.parseInt(minute),
														0);
												// Calendar starts with 0!
												cal.add(Calendar.MONTH, -1);
												LOGGER.debug("Found event date: {}", cal.getTime());
												
												EventSchedule evSched = new EventSchedule();
												evSched.setTimestamp(cal.getTime());
												evSched.setEvent(event);
												fullSchedule.add(evSched);
											}
										}
									}
								}
								
								// Add schedule after event (dependency)
								for (EventSchedule evSched : fullSchedule) {
									evSched.setEvent(event);
									if (eventScDAOImpl.find(event, evSched.getTimestamp()) != null) {
										LOGGER.debug("Schedule {} for event {} already exists.", evSched, event);
										// Schedule already exists! Do nothing...
									} else {
										eventScDAOImpl.addEventSchedule(evSched);
										eventScDAOImpl.getHibernateTemplate().flush();
										LOGGER.info("Added schedule {} for event {} ", evSched, event);
									}
								}
							}
						}
					}
				} 
			} 
		}
	}
	
	/**
	 * Parses a regexp and returns the first result.
	 * 
	 * @param pattern
	 * @param source
	 * @return Result
	 */
	private String getMatchFromPattern(String pattern, String source) {
		Pattern regexp = Pattern.compile(pattern);
		Matcher matcher = regexp.matcher(source);
		if (matcher.find()) {
			return matcher.group();
		} else {
			return null;
		}
	}
	
	/**
	 * Gets source tree or creates it from URL.
	 * @return Source
	 * @throws IOException
	 */
	private Source getSource() throws Exception {
		if (this.source == null) {
			if (this.preparedUrl != null) {
				// Create a jericho source tree from URL
				URLConnection conn = this.preparedUrl.openConnection();
				
				try {
					String userAgent = this.getRandomUserAgent();
					conn.setRequestProperty("User-Agent", userAgent);
					LOGGER.info("Using user-agent: {}",userAgent);
					this.source = new Source(conn.getInputStream());
					return this.source;
				} catch (Exception e) {
					throw e;
				}
			} else {
				throw new IllegalArgumentException("Prepared URL is null!");
			}
		} else {
			return this.source;
		}
	}

	/**
	 * Parses resource file and retrieves a random User-Agent
	 * @return String
	 * @throws Exception
	 */
	private String getRandomUserAgent() throws Exception {
		try {
			// Set a random user agent by reading one line from useragents.txt resource file
			InputStream inputStream = ROCalendarTasklet.class.getResourceAsStream("/useragents.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	
			String strLine;
			int lineCount = 0;
			while ((strLine = bufferedReader.readLine()) != null) {
				lineCount++;
			}
			inputStream.close();
			Random rand = new Random();
			int randomLine = rand.nextInt(lineCount);
			
			InputStream iStream = ROCalendarTasklet.class.getResourceAsStream("/useragents.txt");
			InputStreamReader iStreamReader = new InputStreamReader(iStream);
			BufferedReader bReader = new BufferedReader(iStreamReader);
			String userAgent = null;
			int currentLine = 0;
			while ((strLine = bReader.readLine()) != null) {
				if (currentLine == randomLine) {
					userAgent = strLine; 
				}
				currentLine++;
			}
			
			return userAgent;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Parses the root URL with date parameters and creates an URL object.
	 * @param params
	 * @throws Exception
	 */
	private void prepareUrl(Map<String, Object> params) throws Exception {
		if (this.preparedUrl == null) {
			// Replace tokens in url
			DateTime firstDate = new DateTime(
					Integer.parseInt((String)params.get("year")), 
					Integer.parseInt((String)params.get("month")), 
					1, 0, 0, 0, 0
				);
			DateTime secondDate = firstDate.plusMonths(1).minusDays(1);
			DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd-HH");
			String[] secondDateComponents = dtf.print(secondDate).split("-");
			
			String workingURL = this.url;
			Pattern pattern = Pattern.compile(getUrlParamPattern());
			Matcher matcher = pattern.matcher(workingURL);
			while (matcher.find()) {
				String token = matcher.group().substring(1, matcher.group().length() - 1);
				if (token.equals("year1")) {
					workingURL = workingURL.replaceAll(new StringBuilder().append("@").append("year1").append("@").toString(), (String)params.get("year"));
				} else if (token.equals("month1")) {
					workingURL = workingURL.replaceAll(new StringBuilder().append("@").append("month1").append("@").toString(), (String)params.get("month"));
				} else if (token.equals("day1")) {
					workingURL = workingURL.replaceAll(new StringBuilder().append("@").append("day1").append("@").toString(), "01");
				} else if (token.equals("year2")) {
					workingURL = workingURL.replaceAll(new StringBuilder().append("@").append("year2").append("@").toString(), secondDateComponents[0]);
				} else if (token.equals("month2")) {
					workingURL = workingURL.replaceAll(new StringBuilder().append("@").append("month2").append("@").toString(), secondDateComponents[1]);
				} else if (token.equals("day2")) {
					workingURL = workingURL.replaceAll(new StringBuilder().append("@").append("day2").append("@").toString(), secondDateComponents[2]);
				}
			}

			this.preparedUrl = new URL(workingURL);
		}
	}
}
