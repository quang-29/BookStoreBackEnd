package org.example.bookstore.payload.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.bookstore.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdate {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String address;
    private String phoneNumber;

}
