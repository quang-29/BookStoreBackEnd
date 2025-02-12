package org.example.bookstore.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntrospectTokenResponse {
    boolean valid;
}