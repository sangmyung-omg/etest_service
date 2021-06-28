package com.tmax.eTest.Report.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.tmax.eTest.Contents.model.ProblemUKRelation;
import com.tmax.eTest.Report.dto.lrs.StatementDTO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Path;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class ProbUKRelSpecs {

    private final Logger logger = LoggerFactory.getLogger("StatementSpecs");

    public Specification<ProblemUKRelation> searchProbUKRelFromStatementList(List<StatementDTO> statementList)
    {
    	return (Specification<ProblemUKRelation>) ((root, query, builder) -> {
    		List<Predicate> results = new ArrayList<>();
    		
    		for(StatementDTO dto : statementList)
    		{
    			if(dto.getSourceId() != null)
    				results.add(builder.equal(root.get("ukId"), dto.getSourceId()));
    		}

            return builder.or(results.toArray(new Predicate[0]));
        });
    }
    
    /*
        return (Specification<StatementDAO>) ((root, query, builder) -> {
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
            
            if(checkIsNotDeleted)
            	results.add(builder.equal(root.get("isDeleted"), 0));
            
            if(isAsc)
            	query.orderBy(builder.asc(root.get("timestamp")));
            else
            	query.orderBy(builder.desc(root.get("timestamp")));

            return builder.and(results.toArray(new Predicate[0]));
        });
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
        Root<StatementDAO> root, 
        CriteriaBuilder builder)
    {
    	Path<String> path = root.get("timestamp");
    	
        if(dateFrom != null && dateTo != null)
            return builder.between(path, dateFrom, dateTo);
        else if(dateFrom != null)
            return builder.greaterThan(path, dateFrom);
        else // dateTo != null
            return builder.lessThan(path, dateTo);
    }
    */
    
}