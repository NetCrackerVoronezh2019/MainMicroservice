package com.mainmicroservice.mainmicroservice.Services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainmicroservice.mainmicroservice.Entities.Rating;
import com.mainmicroservice.mainmicroservice.Repositories.RatingRepository;

@Service
@Transactional
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
