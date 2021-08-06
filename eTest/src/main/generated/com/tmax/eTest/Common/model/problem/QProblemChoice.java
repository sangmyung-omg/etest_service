package com.tmax.eTest.Common.model.problem;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProblemChoice is a Querydsl query type for ProblemChoice
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProblemChoice extends EntityPathBase<ProblemChoice> {

    private static final long serialVersionUID = -1648093615L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProblemChoice problemChoice = new QProblemChoice("problemChoice");

    public final NumberPath<Long> choiceNum = createNumber("choiceNum", Long.class);

    public final NumberPath<Integer> choiceScore = createNumber("choiceScore", Integer.class);

    public final QProblem probID;

    public final com.tmax.eTest.Common.model.uk.QUkMaster ukId;

    public QProblemChoice(String variable) {
        this(ProblemChoice.class, forVariable(variable), INITS);
    }

    public QProblemChoice(Path<? extends ProblemChoice> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProblemChoice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProblemChoice(PathMetadata metadata, PathInits inits) {
        this(ProblemChoice.class, metadata, inits);
    }

    public QProblemChoice(Class<? extends ProblemChoice> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.probID = inits.isInitialized("probID") ? new QProblem(forProperty("probID"), inits.get("probID")) : null;
        this.ukId = inits.isInitialized("ukId") ? new com.tmax.eTest.Common.model.uk.QUkMaster(forProperty("ukId")) : null;
    }

}

