package com.mainmicroservice.mainmicroservice.Entities;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name="SERVICES")
public class Service {

    
    @Id
	@Size(min=4, max=30)
    @Column(name="SERVICEWEBSITE")
	private String serviceWebSite;
    
	@Size(min=4, max=30)
	@Column(name="SERVICENAME")
	private String serviceName;
	@NotNull
	@OneToMany(cascade = CascadeType.ALL,
    fetch = FetchType.EAGER,
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
