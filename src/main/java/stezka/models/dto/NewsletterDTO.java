package stezka.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class NewsletterDTO {

    @NotBlank(message = "Vyplňte email")
    @Email
    private String email;


    public @NotBlank(message = "Vyplňte email") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Vyplňte email") String email) {
        this.email = email;
    }
}
