package org.fasttrackit.Transfer;

public class MarkAgendaRequest {
    private String firstName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "MarkAgendaRequest{" +
                "firstName='" + firstName + '\'' +
                '}';
    }
}
