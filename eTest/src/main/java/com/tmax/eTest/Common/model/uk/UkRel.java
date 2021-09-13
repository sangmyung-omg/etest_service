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
@IdClass(UkRelCompositeKey.class)
@Table(name = "UK_REL")
public class UkRel {
    @Id
    private Integer baseUkId;
    @Id
    private Integer preUkId;
    private String relationReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "baseUkId", insertable = false, updatable = false, nullable = true)
    private UkMaster baseUk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preUkId", insertable = false, updatable = false, nullable = true)
    private UkMaster preUk;
}
