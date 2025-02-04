package org.insurance.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class UpdateClientRequest {
    @NotNull(message = "PESEL cannot be null")
    private Long pesel;
    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 characters")
    private String newFirstName;
    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String newLastName;

    // Konstruktor bezargumentowy wymagany przez Spring
    public UpdateClientRequest() {}

    // Konstruktor z argumentami
    public UpdateClientRequest(Long pesel, String newFirstName, String newLastName) {
        this.pesel = pesel;
        this.newFirstName = newFirstName;
        this.newLastName = newLastName;
    }

    // Gettery i Settery (Spring ich używa do mapowania JSON → obiekt)
    public Long getPesel() {
        return pesel;
    }

    public void setPesel(Long pesel) {
        this.pesel = pesel;
    }

    public String getNewFirstName() {
        return newFirstName;
    }

    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }
}

