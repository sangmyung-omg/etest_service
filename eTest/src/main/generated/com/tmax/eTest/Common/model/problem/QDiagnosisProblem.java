package com.tmax.eTest.Common.model.problem;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDiagnosisProblem is a Querydsl query type for DiagnosisProblem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDiagnosisProblem extends EntityPathBase<DiagnosisProblem> {

    private static final long serialVersionUID = -1278863459L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDiagnosisProblem diagnosisProblem = new QDiagnosisProblem("diagnosisProblem");

    public final QDiagnosisCurriculum curriculum;

    public final NumberPath<Integer> curriculumId = createNumber("curriculumId", Integer.class);

    public final NumberPath<Integer> orderNum = createNumber("orderNum", Integer.class);

    public final NumberPath<Integer> probId = createNumber("probId", Integer.class);

    public final QProblem problem;

    public QDiagnosisProblem(String variable) {
        this(DiagnosisProblem.class, forVariable(variable), INITS);
    }

    public QDiagnosisProblem(Path<? extends DiagnosisProblem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDiagnosisProblem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDiagnosisProblem(PathMetadata metadata, PathInits inits) {
        this(DiagnosisProblem.class, metadata, inits);
    }

    public QDiagnosisProblem(Class<? extends DiagnosisProblem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.curriculum = inits.isInitialized("curriculum") ? new QDiagnosisCurriculum(forProperty("curriculum")) : null;
        this.problem = inits.isInitialized("problem") ? new QProblem(forProperty("problem"), inits.get("problem")) : null;
    }

}

