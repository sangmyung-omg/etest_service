package com.tmax.eTest.Common.model.comment;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="COMMENT_VERSION_INFO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentVersionInfo {
	@Id
	private String versionName;
	private Integer isSelected;
}
