package net.ada.otp.dtos;

import net.ada.otp.entities.User;

public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private boolean enabled;
    private String role; // String pour simplicité

    // --- Constructeurs ---
    public UserDTO() {}

    public UserDTO(Long id, String firstName, String lastName, String phoneNumber, String email, boolean enabled, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.enabled = enabled;
        this.role = role;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public boolean isEnabled() { return enabled; }
    public String getRole() { return role; }

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setRole(String role) { this.role = role; }

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
                user.getRole() != null ? user.getRole().name() : null
        );
    }

    // --- Conversion DTO -> entité ---
    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setEmail(dto.getEmail());
        user.setEnabled(dto.isEnabled());
        // role doit être mappé par le service (enum parsing si nécessaire)
        return user;
    }
}
