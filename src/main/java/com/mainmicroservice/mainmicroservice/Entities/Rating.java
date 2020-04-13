package com.mainmicroservice.mainmicroservice.Entities;

import javax.persistence.*;

@Entity
@Table(name = "Ratings")
public class Rating {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="RATINGID")
	private Long ratingId;
	
	@Column(name="CUSTOMERID")
	private Long customerId;
	
	@Column(name="FREELANCERID")
	private Long freelancerId;
	
	@Column(name="STARS")
	private double stars;

	public Long getRatingId() {
		return ratingId;
	}

	public void setRatingId(Long ratingId) {
		this.ratingId = ratingId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getFreelancerId() {
		return freelancerId;
	}

	public void setFreelancerId(Long freelancerId) {
		this.freelancerId = freelancerId;
	}

	public double getStars() {
		return stars;
	}

	public void setStars(double stars) {
		this.stars = stars;
	}
	
	
	
	
	
}
