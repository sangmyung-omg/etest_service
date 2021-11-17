package com.tmax.eTest.Common.repository.uk;

import com.tmax.eTest.Common.model.uk.UkDescriptionVersion;
import com.tmax.eTest.Common.model.uk.UkDescriptionVersionCompositeKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UkDescriptionVersionRepo extends JpaRepository<UkDescriptionVersion, UkDescriptionVersionCompositeKey> {
    
}
