package org.thethingsnetwork.samples.mqtt;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class Evento {
    // classe para criar um evento


    private Event evento;
    private EventDateTime inicio;
    private EventDateTime fim;
    private String[] recurrence;
    private EventReminder[] reminderOverrides;                      // lembrar email
    private String calendarID = "smartag04@gmail.com";              // email em uso
    private Calendar service;

    public Evento(Event evento , EventReminder[] reminderOverrides, Calendar service) {
        if(reminderOverrides != null) this.reminderOverrides = reminderOverrides;
        this.service = service;
        this.evento = evento;
        this.inicio = new EventDateTime()                   //inicio do evento
                .setDateTime(new DateTime(new Date()))
                .setTimeZone("Portugal");
        this.fim = new EventDateTime()                      //fim do evento
                .setDateTime(new DateTime(new Date(System.currentTimeMillis() + 3600000)))      // cria uma reuniao por uma hora
                .setTimeZone("Portugal");
        this.evento.setStart(this.inicio);
        this.evento.setEnd(this.fim);
        this.recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=1"};
        this.evento.setRecurrence(Arrays.asList(recurrence));

    }



    public void list_events() throws IOException {
        // List the next 3 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = this.service.events().list(this.calendarID).setMaxResults(3).setTimeMin(now).setOrderBy("startTime")
                .setSingleEvents(true).execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("Sem eventos adicionados.");
        } else {
            System.out.println("Eventos adicionados:");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
    }


    public String time_stamp(long millis) {
        //passa long para um time stamp
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;
        String time = String.format("%02d:%02d", hour + 1, minute);
        return time;
    }





    public Calendar getService() {
        return service;
    }

    public void setService(Calendar service) {
        this.service = service;
    }

    public Event getEvento() {
        return evento;
    }

    public void setEvento(Event evento) {
        this.evento = evento;
    }

    public EventDateTime getInicio() {
        return inicio;
    }

    public void setInicio(EventDateTime inicio) {
        this.inicio = inicio;
    }

    public EventDateTime getFim() {
        return fim;
    }

    public void setFim(EventDateTime fim) {
        this.fim = fim;
    }

    public String[] getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String[] recurrence) {
        this.recurrence = recurrence;
    }

    public EventReminder[] getReminderOverrides() {
        return reminderOverrides;
    }

    public void setReminderOverrides(EventReminder[] reminderOverrides) {
        this.reminderOverrides = reminderOverrides;
    }

    public String getCalendarID() {
        return calendarID;
    }

    public void setCalendarID(String calendarID) {
        this.calendarID = calendarID;
    }
}
