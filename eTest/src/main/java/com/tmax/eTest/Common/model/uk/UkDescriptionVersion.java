package com.tmax.eTest.Common.model.uk;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@IdClass(UkDescriptionVersionCompositeKey.class)
@Table(name = "UK_DESCRIPTION_VERSION")
public class UkDescriptionVersion {
    @Id
    private Long ukId;
    @Id
    private Long versionId;
    private String ukName;
    private String ukDescription;
    private String externalLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ukId", insertable = false, updatable = false, nullable = true)
    private UkMaster ukMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "versionId", insertable = false, updatable = false, nullable = true)
    private VersionMaster versionMaster;
}
