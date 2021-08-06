package com.tmax.eTest.Common.model.problem;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPart is a Querydsl query type for Part
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPart extends EntityPathBase<Part> {

    private static final long serialVersionUID = -30483550L;

    public static final QPart part = new QPart("part");

    public final NumberPath<Integer> orderNum = createNumber("orderNum", Integer.class);

    public final NumberPath<Integer> partID = createNumber("partID", Integer.class);

    public final StringPath partName = createString("partName");

    public final NumberPath<Integer> problemCount = createNumber("problemCount", Integer.class);

    public QPart(String variable) {
        super(Part.class, forVariable(variable));
    }

    public QPart(Path<? extends Part> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPart(PathMetadata metadata) {
        super(Part.class, metadata);
    }

}

