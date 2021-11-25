package com.tmax.eTest.Common.model.comment;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@IdClass(CommentKey.class)
@Table(name="COMMENT_INFO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class CommentInfo {
	@Id
	private String versionName;
	@Id
	private Integer seqNum;
	private String commentType;
	private String commentName;
	private String commentText;
	private String ruleText;
}
