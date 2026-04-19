package org.restaurant.repository;

import entity.DonDatBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonDatBanRepository extends JpaRepository<DonDatBan, String> {
}
