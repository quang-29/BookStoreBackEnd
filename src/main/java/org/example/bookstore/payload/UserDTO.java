package org.example.bookstore.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.bookstore.model.Role;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private UUID userId;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private String address;
    private CartDTO cart;
    @JsonProperty("roles")
    public String getRolesAsString() {
        return roles != null ? roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.joining(", "))
                : "";
    }
    private Set<Role> roles;

}

