package org.example.bookstore.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogInResponse {
    private boolean authenticated;
    private String token;
}