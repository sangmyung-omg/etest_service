package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Common.model.uk.UkMaster;

public interface UkRepository extends CrudRepository<UkMaster, String>{

}
