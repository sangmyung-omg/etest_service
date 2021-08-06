package com.tmax.eTest.Common.model.problem;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDiagnosisCurriculum is a Querydsl query type for DiagnosisCurriculum
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDiagnosisCurriculum extends EntityPathBase<DiagnosisCurriculum> {

    private static final long serialVersionUID = 1327119997L;

    public static final QDiagnosisCurriculum diagnosisCurriculum = new QDiagnosisCurriculum("diagnosisCurriculum");

    public final StringPath chapter = createString("chapter");

    public final NumberPath<Integer> curriculumId = createNumber("curriculumId", Integer.class);

    public final StringPath section = createString("section");

    public final StringPath setType = createString("setType");

    public final StringPath status = createString("status");

    public final StringPath subject = createString("subject");

    public final StringPath subSection = createString("subSection");

    public QDiagnosisCurriculum(String variable) {
        super(DiagnosisCurriculum.class, forVariable(variable));
    }

    public QDiagnosisCurriculum(Path<? extends DiagnosisCurriculum> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDiagnosisCurriculum(PathMetadata metadata) {
        super(DiagnosisCurriculum.class, metadata);
    }

}

