package com.mainmicroservice.mainmicroservice.Repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.mainmicroservice.mainmicroservice.Entities.UserDocument;

@Repository
@Transactional
public interface UserDocumentRepository extends JpaRepository<UserDocument,Long> {

	@Query(value = "Select * from userdocuments where userid=?1", 
			  nativeQuery = true)
	public List<UserDocument> findByUserId(Long id);
}
