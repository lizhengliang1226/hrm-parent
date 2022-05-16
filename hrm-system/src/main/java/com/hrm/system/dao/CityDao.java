package com.hrm.system.dao;


import com.hrm.domain.system.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CityDao extends JpaRepository<City, String>, JpaSpecificationExecutor<City> {
}
