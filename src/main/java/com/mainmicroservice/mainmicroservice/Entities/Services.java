package com.mainmicroservice.mainmicroservice.Entities;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name="Services")
public class Services {

    
    @Id
	@Size(min=4, max=30)
    @Column(name="service_web_site")
	private String serviceWebSite;
	
	@Size(min=4, max=30)
	private String serviceName;
	@NotNull
	@OneToMany(cascade = CascadeType.ALL,
    fetch = FetchType.LAZY,
    mappedBy = "service")
	Set<User> allusers=new HashSet<>();

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
