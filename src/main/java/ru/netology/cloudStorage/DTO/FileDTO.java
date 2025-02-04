package ru.netology.cloudStorage.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FileDTO {
    @JsonProperty("filename")
    private String fileName;
    private Long size;
    private String hash;
    private byte[] fileByte;
    private String type;
    private LocalDateTime date;
}
