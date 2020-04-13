package Models;
import java.util.*;

import Models.Enums.AdvertisementType;

public class AdvFilters {

	private List<SubjectModel> subjects;
	private Integer minPrice;
	private Integer maxPrice;
	private String searchRow;
	private AdvertisementType type;
	private Tag[] tags;
	
	public Tag[] getTags() {
		return tags;
	}

	public void setTags(Tag[] tags) {
		this.tags = tags;
	}

	public AdvertisementType getType() {
		return type;
	}

	public void setType(AdvertisementType type) {
		this.type = type;
	}

	public String getSearchRow() {
		return searchRow;
	}

	public void setSearchRow(String searchRow) {
		this.searchRow = searchRow;
	}

	public Integer getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Integer minPrice) {
		this.minPrice = minPrice;
	}

	public Integer getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Integer maxPrice) {
		this.maxPrice = maxPrice;
	}

	public List<SubjectModel> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SubjectModel> subjects) {
		this.subjects = subjects;
	}
	
	
}
