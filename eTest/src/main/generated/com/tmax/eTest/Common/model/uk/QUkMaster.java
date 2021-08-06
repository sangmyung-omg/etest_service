package com.tmax.eTest.Common.model.uk;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUkMaster is a Querydsl query type for UkMaster
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUkMaster extends EntityPathBase<UkMaster> {

    private static final long serialVersionUID = 1951676272L;

    public static final QUkMaster ukMaster = new QUkMaster("ukMaster");

    public final StringPath part = createString("part");

    public final ListPath<com.tmax.eTest.Common.model.problem.ProblemChoice, com.tmax.eTest.Common.model.problem.QProblemChoice> problemChoices = this.<com.tmax.eTest.Common.model.problem.ProblemChoice, com.tmax.eTest.Common.model.problem.QProblemChoice>createList("problemChoices", com.tmax.eTest.Common.model.problem.ProblemChoice.class, com.tmax.eTest.Common.model.problem.QProblemChoice.class, PathInits.DIRECT2);

    public final ListPath<ProblemUKRelation, QProblemUKRelation> problemUkRels = this.<ProblemUKRelation, QProblemUKRelation>createList("problemUkRels", ProblemUKRelation.class, QProblemUKRelation.class, PathInits.DIRECT2);

    public final StringPath trainUnseen = createString("trainUnseen");

    public final StringPath ukDescription = createString("ukDescription");

    public final NumberPath<Integer> ukId = createNumber("ukId", Integer.class);

    public final StringPath ukName = createString("ukName");

    public QUkMaster(String variable) {
        super(UkMaster.class, forVariable(variable));
    }

    public QUkMaster(Path<? extends UkMaster> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUkMaster(PathMetadata metadata) {
        super(UkMaster.class, metadata);
    }

}

