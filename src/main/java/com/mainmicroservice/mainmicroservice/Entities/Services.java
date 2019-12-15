package com.mainmicroservice.mainmicroservice.Entities;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name="Services")
public class Services {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long serviceId;

    @Size(min=4, max=30)
	private String serviceName;

	@Size(min=4, max=30)
	private String serviceWebSite;
	@NotNull
	@OneToMany(cascade = CascadeType.ALL,
    fetch = FetchType.LAZY,
    mappedBy = "service")
	Set<Users> userss=new HashSet<>();
	
	public Long getServiceId() {
		return this.serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return this.serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceWebSite() {
		return serviceWebSite;
	}
	public void setServiceWebSite(String serviceWebSite) {
		this.serviceWebSite = serviceWebSite;
	}	
}
