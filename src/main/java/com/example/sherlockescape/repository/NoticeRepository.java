package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {

	Optional<Notice> findById(Long noticeId);
}
