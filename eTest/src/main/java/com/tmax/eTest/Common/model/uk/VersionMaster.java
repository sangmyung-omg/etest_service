package com.tmax.eTest.Common.model.uk;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "VERSION_MASTER")
public class VersionMaster {
    @Id
    private Long versionId;
    private String versionName;
    private String versionNum;
    private Timestamp createDate;
    private Timestamp editDate;
    private String isDefault;
    private String isDeleted;

    @OneToMany(mappedBy = "versionId")
    private List<UkDescriptionVersion> ukVersion;
}
