package com.tmax.eTest.Common.repository.uk;

import com.tmax.eTest.Common.model.uk.UkRel;
import com.tmax.eTest.Common.model.uk.UkRelCompositeKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UkRelRepo extends JpaRepository<UkRel, UkRelCompositeKey> {
    
}
