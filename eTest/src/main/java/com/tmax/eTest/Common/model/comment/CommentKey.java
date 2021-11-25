package com.tmax.eTest.Common.model.comment;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentKey implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String versionName;
	private Integer seqNum;

}
