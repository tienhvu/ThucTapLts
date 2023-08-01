package com.example.demo.Repository;

import com.example.demo.Entity.Chua;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ChuaRepository extends JpaRepository<Chua, Integer> {

    Chua findByTenChua(String tenChua);
    Page<Chua> findByTenChuaContainingIgnoreCase(String keyword, Pageable pageable);
}
