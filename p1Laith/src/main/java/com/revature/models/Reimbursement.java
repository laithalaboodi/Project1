package com.revature.models;

import java.sql.Timestamp;

import com.revature.enums.ReimbursementStatus;
import com.revature.enums.ReimbursementType;

public class Reimbursement {
	
	private int id;
	private double amount;
	private Timestamp submitted;
	private Timestamp resolved;
	private String description;
	private Integer authorID;
	private Integer resolverID;
	private ReimbursementStatus status;
	private ReimbursementType type;
	private String firstName;
	private String lastName;
	
	public Reimbursement() {
		this.id = -1;
		this.resolved = null;
		this.submitted = new Timestamp(System.currentTimeMillis());
		this.description = null;
		this.resolverID = null;
		this.status = ReimbursementStatus.PENDING;
	}
	
	public Reimbursement(int id, double amount, Timestamp submitted, Timestamp resolved, String description, Integer authorID,
			Integer resolverID, ReimbursementStatus status, ReimbursementType type, String firstName, String lastName) {
		super();
		this.id = id;
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.authorID = authorID;
		this.resolverID = resolverID;
		this.status = status;
		this.type = type;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getAuthorID() {
		return authorID;
	}
	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}

	public Integer getResolverID() {
		return resolverID;
	}
	public void setResolverID(int resolverID) {
		this.resolverID = resolverID;
	}
	public ReimbursementType getType() {
		return type;
	}
	public void setType(ReimbursementType type) {
		this.type = type;
	}
	public Timestamp getSubmitted() {
		return submitted;
	}
	public void setResolved(Timestamp time) {
		this.resolved = time;
	}
	public Timestamp getResolved() {
		return resolved;
	}
	public ReimbursementStatus getStatus() {
		return status;
	}
	public void approveReimbursement() {
		//TODO throw new error
		if(status != ReimbursementStatus.PENDING) {
			return;
		}
		status = ReimbursementStatus.APPROVED;
	}
	public void declineReimbursement() {
		//TODO throw new error
		if(status != ReimbursementStatus.PENDING) {
			return;
		}
		status = ReimbursementStatus.DENIED;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + authorID;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((resolved == null) ? 0 : resolved.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((submitted == null) ? 0 : submitted.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reimbursement other = (Reimbursement) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (authorID != other.authorID)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (resolved == null) {
			if (other.resolved != null)
				return false;
		} else if (!resolved.equals(other.resolved))
			return false;
		if (status != other.status)
			return false;
		if (submitted == null) {
			if (other.submitted != null)
				return false;
		} else if (!submitted.equals(other.submitted))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", amount=" + amount + ", submitted=" + submitted + ", resolved=" + resolved
				+ ", description=" + description + ", authorID=" + authorID + ", status=" + status + ", type=" + type
				+ "]";
	}
	
	
	
}
