package org.example.bookstore.payload;

import java.util.UUID;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserOrderDTO {
    private UUID userId;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;

}
