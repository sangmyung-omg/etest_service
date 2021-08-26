package com.tmax.eTest.LRS.model;

import org.springframework.data.jpa.domain.Specification;

import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Path;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatementSpecs {

    private static final Logger logger = LoggerFactory.getLogger("StatementSpecs");

    /*
        input params
        {
            userIdList : (List<String>)
            actionTypeList : (List<String>)
            dateFrom : (String - ISO 8601 Format)
            dateTo : (String - ISO 8601 Format)
            sourceTypeList : (List<String>)
        }
    */
    public static Specification<Statement> searchStatement(
    		GetStatementInfoDTO searchInfo, 
    		boolean isAsc, 
    		boolean checkIsNotDeleted)
    {
        return (Specification<Statement>) ((root, query, builder) -> {
            List<Predicate> results = new ArrayList<>();

            if(searchInfo.getUserIdList() != null)
                results.add(getPredByStrList(
                    searchInfo.getUserIdList(),
                    root.get("userId"),
                    builder));
            
            if(searchInfo.getActionTypeList() != null)
                results.add(getPredByStrList(
                    searchInfo.getActionTypeList(),
                    root.get("actionType"),
                    builder));
            
            if(searchInfo.getDateFrom() != null 
            || searchInfo.getDateTo() != null)
                results.add(getPredByDate(
                    searchInfo.getDateFrom(),
                    searchInfo.getDateTo(),
                    root,
                    builder));
            
            if(searchInfo.getSourceTypeList() != null)
                results.add(getPredByStrList(
                    searchInfo.getSourceTypeList(),
                    root.get("sourceType"),
                    builder));
            
            if(searchInfo.getContainExtension() != null)
            	results.add(getContainPredByStrList(
        			searchInfo.getContainExtension(),
                    root.get("extension"),
                    builder));
            
            if(checkIsNotDeleted)
            	results.add(builder.equal(root.get("isDeleted"), 0));
            
            if(isAsc)
            	query.orderBy(builder.asc(root.get("timestamp")));
            else
            	query.orderBy(builder.desc(root.get("timestamp")));

            return builder.and(results.toArray(new Predicate[0]));
        });
    }
    
    private static Predicate getContainPredByStrList(
            List<String> strList,
            Path<String> path,
            CriteriaBuilder builder)
    {
    	List<Predicate> results = new ArrayList<>();
        for(String str : strList)
        {
            results.add(builder.like(path, "%"+str+"%"));
        }
        return builder.or(results.toArray(new Predicate[0]));
    }

    // equal command
    private static Predicate getPredByStrList(
        List<String> strList,
        Path<String> path,
        CriteriaBuilder builder)
    {
        List<Predicate> results = new ArrayList<>();
        for(String str : strList)
        {
            results.add(builder.equal(path, str));
        }
        return builder.or(results.toArray(new Predicate[0]));
    }

    // between or greaterThan or lessThan command
    private static Predicate getPredByDate(
        String dateFrom,
        String dateTo, 
        Root<Statement> root, 
        CriteriaBuilder builder)
    {
    	Path<String> path = root.get("timestamp");
    	
        if(dateFrom != null && dateTo != null)
            return builder.between(path, dateFrom, dateTo);
        else if(dateFrom != null)
            return builder.greaterThanOrEqualTo(path, dateFrom);
        else // dateTo != null
            return builder.lessThanOrEqualTo(path, dateTo);
    }
    
}