package com.resumeradar.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRegModel {

	@NotBlank(message = "Title can not be blank")
	private String title;
	
	@NotBlank(message = "Description can not be blank")
	private String description;
	
	@NotBlank(message="Skill cannot be blanked")
	private String skill;
	
	@NotBlank(message="Location can not be blanked")
	private String location;
	
	@NotBlank(message="Salary ranged can not be blanked")
	private String salaryRange;
	
	@NotBlank(message="Company Name can not be blanked")
	private String company;
	
}
