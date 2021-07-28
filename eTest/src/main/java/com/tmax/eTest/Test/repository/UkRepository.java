package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Common.model.uk.UkMaster;
import org.springframework.stereotype.Repository;

@Repository("TE-UkRepository")

public interface UkRepository extends CrudRepository<UkMaster, String>{

}
