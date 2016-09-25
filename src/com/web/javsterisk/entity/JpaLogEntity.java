package com.web.javsterisk.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.db.jpa.BasicLogEventEntity;

@Entity
public class JpaLogEntity extends BasicLogEventEntity  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private long id;
	
	public JpaLogEntity() {
        super(null);
    }
	
	public JpaLogEntity(LogEvent wrappedEvent) {
        super(wrappedEvent);
    }
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
