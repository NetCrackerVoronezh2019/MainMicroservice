package com.mainmicroservice.mainmicroservice.Repositories;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mainmicroservice.mainmicroservice.Entities.Rating;

@Repository
@Transactional
public interface RatingRepository extends JpaRepository<Rating,Long>{

}
