package ru.netology.cloudStorage.entity;

import lombok.*;
import ru.netology.cloudStorage.entity.User;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "files", schema = "public")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Long size;

    @Lob
    @Column(nullable = false)
    private byte[] fileByte;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private boolean isDelete = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;
}
