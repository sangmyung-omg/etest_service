package com.tmax.eTest.Common.model.uk;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProblemUKRelation is a Querydsl query type for ProblemUKRelation
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProblemUKRelation extends EntityPathBase<ProblemUKRelation> {

    private static final long serialVersionUID = -1896246599L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProblemUKRelation problemUKRelation = new QProblemUKRelation("problemUKRelation");

    public final com.tmax.eTest.Common.model.problem.QProblem probID;

    public final QUkMaster ukId;

    public QProblemUKRelation(String variable) {
        this(ProblemUKRelation.class, forVariable(variable), INITS);
    }

    public QProblemUKRelation(Path<? extends ProblemUKRelation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProblemUKRelation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProblemUKRelation(PathMetadata metadata, PathInits inits) {
        this(ProblemUKRelation.class, metadata, inits);
    }

    public QProblemUKRelation(Class<? extends ProblemUKRelation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.probID = inits.isInitialized("probID") ? new com.tmax.eTest.Common.model.problem.QProblem(forProperty("probID"), inits.get("probID")) : null;
        this.ukId = inits.isInitialized("ukId") ? new QUkMaster(forProperty("ukId")) : null;
    }

}

