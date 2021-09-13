package com.tmax.eTest.Common.model.uk;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UkRelCompositeKey implements Serializable{
    private Integer baseUkId;
    private Integer preUkId;
}
