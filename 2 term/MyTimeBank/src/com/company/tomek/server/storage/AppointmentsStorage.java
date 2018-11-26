package com.company.tomek.server.storage;



import java.io.PrintWriter;
import java.util.*;

public class AppointmentsStorage {

    private List<String> appointments = new ArrayList<>(8);
    private Map<String, String> secretAppointment = new HashMap<>();

    public AppointmentsStorage() {
        for(int i = 0 ; i<8 ; i++) {
            String str = String.format("%d : 1%d:00 - appointment number %d - free | reserved by -", i, i+1, i+1);
            appointments.add(i, str);
        }
    }


    public boolean reserveOne(int index, String nick, String secretKey) {
        String appointment = appointments.get(index);
        if(isReserved(appointment)) return false;
        String appointmentReplacement = appointment.replace("free", "booked").
                                        replace("reserved by -", "reserved by " + nick);
        secretAppointment.put(secretKey, appointmentReplacement);
        appointments.remove(index);
        appointments.add(index, appointmentReplacement);
        return true;
    }

    public boolean cancel(int index, String secretKey) {
        String appointment = appointments.get(index);
        if(!isOwner(appointment, secretKey)) return false;
        String appointmentReplacement = appointment.replace("booked", "free").
                        replaceAll("reserved by .++","reserved by -");
        appointments.remove(index);
        secretAppointment.remove(secretKey);
        appointments.add(index, appointmentReplacement);
        return true;
    }

    public void printAllAppointments(PrintWriter printWriter) {
        for(String appointment : appointments) {
            printWriter.println(appointment);
            printWriter.flush();
        }
    }

    private boolean isOwner(String appointment, String secretKey) {
       Optional<String> key = Optional.ofNullable(secretAppointment.get(secretKey));
       if(key.orElse("").equals(appointment)) return true;
       return false;
    }

    public String getAppointment(int index) {
        return appointments.get(index);
    }

    // checks if given appointments is already booked
    private boolean isReserved(String appointment) {
        return appointment.contains("booked");
    }
}
