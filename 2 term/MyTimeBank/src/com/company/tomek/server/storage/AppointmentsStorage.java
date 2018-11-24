package com.company.tomek.server.storage;



import java.util.ArrayList;
import java.util.List;

public class AppointmentsStorage {

    private List<String> appointments = new ArrayList<>(8);

    public AppointmentsStorage() {
        for(int i = 0 ; i<8 ; i++) {
            String str = String.format("%d : 1%d:00 - appointment number %d - free | reserved by -", i, i+1, i+1);
            appointments.add(i, str);
        }
    }

    //TODO: extract this two methods to separate ones
    public boolean reserveOne(int index,String clientId) {
        String appointment = appointments.get(index);
        if(isReserved(appointment)) return false;
        String appointmentReplacement = appointment.replace("free", "booked").
                                            replace("reserved by -", "reserved by " + clientId);
        appointments.remove(index);
        appointments.add(index, appointmentReplacement);
        return true;
    }

    public boolean cancel(int index, String clientId) {
        String appointment = appointments.get(index);
        if(!isOwner(appointment, clientId)) return false;
        String appointmentReplacement = appointment.replace("booked", "free").
                        replaceAll("reserved by .++","reserved by -");
        appointments.remove(index);
        appointments.add(index, appointmentReplacement);
        return true;
    }


    public List<String> getAll() {
        return appointments;
    }

    private boolean isOwner(String appointment, String clientId) {
        return  appointment.contains(clientId);
    }

    public String getAppointment(int index) {
        return appointments.get(index);
    }

    // checks if given appointments is already booked
    private boolean isReserved(String appointment) {
        return appointment.contains("booked");
    }
}
