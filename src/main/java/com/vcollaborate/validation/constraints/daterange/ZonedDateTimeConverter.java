package com.vcollaborate.validation.constraints.daterange;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;

public class ZonedDateTimeConverter implements InstantConverter {
	
	@Override
	public Class<?> getSupportedType() {
		return ZonedDateTime.class;
	}

	@Override
	public Chronology getChronology(Object object, DateTimeZone zone) {
        if (object.getClass().getName().endsWith(".BuddhistCalendar")) {
            return BuddhistChronology.getInstance(zone);
        } else if (object instanceof GregorianCalendar) {
            GregorianCalendar gc = (GregorianCalendar) object;
            long cutover = gc.getGregorianChange().getTime();
            if (cutover == Long.MIN_VALUE) {
                return GregorianChronology.getInstance(zone);
            } else if (cutover == Long.MAX_VALUE) {
                return JulianChronology.getInstance(zone);
            } else {
                return GJChronology.getInstance(zone, cutover, 4);
            }
        } else {
            return ISOChronology.getInstance(zone);
        }
	}

	@Override
	public Chronology getChronology(Object object, Chronology chrono) {
		if (chrono != null) {
            return chrono;
        }
		ZonedDateTime zdt = (ZonedDateTime) object;
        DateTimeZone zone = null;
        try {
            zone = DateTimeZone.forTimeZone(TimeZone.getTimeZone(zdt.getZone()));
            
        } catch (IllegalArgumentException ex) {
            zone = DateTimeZone.getDefault();
        }
        return getChronology(zdt, zone);
	}

	@Override
	public long getInstantMillis(Object object, Chronology chrono) {
		ZonedDateTime zdt = (ZonedDateTime) object;
        return zdt.toOffsetDateTime().toInstant().toEpochMilli();
	}

}
