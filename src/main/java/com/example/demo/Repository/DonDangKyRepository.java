package com.example.demo.Repository;

import com.example.demo.Entity.DaoTrang;
import com.example.demo.Entity.DonDangKy;
import com.example.demo.Entity.PhatTu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonDangKyRepository extends JpaRepository<DonDangKy, Integer> {
    List<DonDangKy> findByPhatTuAndDaoTrang(PhatTu phatTu, DaoTrang daoTrang);
}
