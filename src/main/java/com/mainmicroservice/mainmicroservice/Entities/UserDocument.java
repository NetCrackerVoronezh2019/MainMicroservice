package com.mainmicroservice.mainmicroservice.Entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import Jacson.Views;

@Entity
@Table(name = "USERDOCUMENTS")
public class UserDocument {

		@Column(name="DOCUMENTID")
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long documentId;
		
		@Column(name="DOCUMENTKEY")
		private String documentKey;
		
		@Column(name="ISVALID")
		private Boolean isValid;
		
		
		@ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "USERID", nullable = false)
	    private User user;
	

		public Long getDocumentId() {
			return documentId;
		}


		public void setDocumentId(Long documentId) {
			this.documentId = documentId;
		}


		public String getDocumentKey() {
			return documentKey;
		}


		public void setDocumentKey(String documentKey) {
			this.documentKey = documentKey;
		}


		public Boolean getIsValid() {
			return isValid;
		}


		public void setIsValid(Boolean isValid) {
			this.isValid = isValid;
		}

	
		public User getUser() {
			return user;
		}


		public void setUser(User user) {
			this.user = user;
		}
			
}

