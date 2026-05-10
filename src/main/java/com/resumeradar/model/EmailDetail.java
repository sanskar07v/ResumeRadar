package com.resumeradar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetail {

	private String msgBody;
	private String subject;
	private String attachment;
	
}
