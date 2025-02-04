package ru.netology.cloudStorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.netology.cloudStorage.entity.File;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findFileByUserIdAndFileName(Long userId, String filename);

    @Query(value = "SELECT * FROM files f WHERE f.user_id = :userId ORDER BY f.file_name LIMIT :limit", nativeQuery = true)
    List<File> findFilesByUserIdWithLimit(Long userId, int limit);
}
