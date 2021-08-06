package com.tmax.eTest.Common.model.problem;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTestProblem is a Querydsl query type for TestProblem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTestProblem extends EntityPathBase<TestProblem> {

    private static final long serialVersionUID = -1179286754L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTestProblem testProblem = new QTestProblem("testProblem");

    public final QPart part;

    public final NumberPath<Integer> probID = createNumber("probID", Integer.class);

    public final QProblem problem;

    public final StringPath status = createString("status");

    public final StringPath subject = createString("subject");

    public QTestProblem(String variable) {
        this(TestProblem.class, forVariable(variable), INITS);
    }

    public QTestProblem(Path<? extends TestProblem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTestProblem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTestProblem(PathMetadata metadata, PathInits inits) {
        this(TestProblem.class, metadata, inits);
    }

    public QTestProblem(Class<? extends TestProblem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.part = inits.isInitialized("part") ? new QPart(forProperty("part")) : null;
        this.problem = inits.isInitialized("problem") ? new QProblem(forProperty("problem"), inits.get("problem")) : null;
    }

}

