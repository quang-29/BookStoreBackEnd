package org.example.bookstore.payload.response;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponse {
    private int code = 200 ;
    private String message;
    private Object data;
    private HttpStatus status;
    private LocalDateTime timestamp;

}