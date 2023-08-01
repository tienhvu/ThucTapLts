package com.example.demo.Repository;

import com.example.demo.Entity.Chua;
import com.example.demo.Entity.DaoTrang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DaoTrangRepository extends JpaRepository<DaoTrang, Integer> {
    DaoTrang findByNoiToChuc(String noiToChuc);
    Page<DaoTrang> findByNoiToChucContainingIgnoreCase(String keyword, Pageable pageable);
}
