package com.mainmicroservice.mainmicroservice.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mainmicroservice.mainmicroservice.Entities.Rating;
import com.mainmicroservice.mainmicroservice.Repositories.RatingRepository;

@Service
public class RatingService {

	@Autowired
	private RatingRepository ratingRepository;
	
	public Rating findById(Long id)
	{
		return this.ratingRepository.findById(id).get();
	}
	
	public void save(Rating rating)
	{
		this.ratingRepository.save(rating);
	}
	
}
