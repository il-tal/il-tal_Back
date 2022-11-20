package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

}
