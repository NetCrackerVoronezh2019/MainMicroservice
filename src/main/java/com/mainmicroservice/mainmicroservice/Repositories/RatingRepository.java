package com.mainmicroservice.mainmicroservice.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mainmicroservice.mainmicroservice.Entities.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long>{

}
