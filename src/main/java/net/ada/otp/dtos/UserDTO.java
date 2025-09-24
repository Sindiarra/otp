package net.ada.otp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import net.ada.otp.entities.User;
import net.ada.otp.enums.Role;

public class UserDTO {

    private Long id;

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @Size(min = 8, max = 20, message = "Le numéro de téléphone doit contenir entre 8 et 20 caractères")
    private String phoneNumber;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    private boolean enabled;

    // Rôle en String avec validation
    @NotBlank(message = "Le rôle est obligatoire")
    @Pattern(regexp = "USER|ADMIN", message = "Rôle invalide")
    private String role;

    // --- Constructeurs, getters et setters ---
    public UserDTO() {}

    public UserDTO(Long id, String firstName, String lastName, String phoneNumber,
                   String email, boolean enabled, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.enabled = enabled;
        this.role = role;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // --- Conversion DTO -> entité ---
    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setPhoneNumber(this.phoneNumber);
        user.setEmail(this.email);
        user.setEnabled(this.enabled);
        user.setRole(Role.valueOf(this.role)); // conversion sûre
        return user;
    }

    // --- Conversion entité -> DTO ---
    public static UserDTO fromEntity(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.isEnabled(),
                user.getRole().name() // stocké en String pour le front
        );
    }
}
