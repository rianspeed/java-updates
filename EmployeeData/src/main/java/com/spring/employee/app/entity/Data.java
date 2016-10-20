package com.spring.employee.app.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the data database table.
 * 
 */
@Entity
@Table(name = "data")
public class Data implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="age")
	private int age;
	
	@Column(name="dept")
	private String department;

	@Column(name="empid")
	private String employeeId;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="name")
	private String name;

	@Column(name="pic")
	private String pic;

	public Data() {
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String dept) {
		this.department = dept;
	}

	public String getEmpid() {
		return this.employeeId;
	}

	public void setEmpid(String empid) {
		this.employeeId = empid;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return this.pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

}