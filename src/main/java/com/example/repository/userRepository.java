package com.example.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.entity.*;
public interface userRepository extends CrudRepository< Users, Integer> {
	public Users findById(int id);
	
	@Query(value = "SELECT * FROM Users order by cdate desc limit :limit", nativeQuery = true)
	public List<Users> findTop(@Param("limit") int limit);
}
