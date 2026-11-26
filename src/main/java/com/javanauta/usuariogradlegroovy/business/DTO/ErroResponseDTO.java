package com.javanauta.usuariogradlegroovy.business.DTO;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErroResponseDTO {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
}
