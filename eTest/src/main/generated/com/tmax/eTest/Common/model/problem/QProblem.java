package com.tmax.eTest.Common.model.problem;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProblem is a Querydsl query type for Problem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProblem extends EntityPathBase<Problem> {

    private static final long serialVersionUID = -1413842800L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProblem problem = new QProblem("problem");

    public final StringPath answerType = createString("answerType");

    public final StringPath category = createString("category");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath creatorId = createString("creatorId");

    public final QDiagnosisProblem diagnosisInfo;

    public final StringPath difficulty = createString("difficulty");

    public final DateTimePath<java.util.Date> editDate = createDateTime("editDate", java.util.Date.class);

    public final StringPath editorID = createString("editorID");

    public final ListPath<com.tmax.eTest.Common.model.error_report.ErrorReport, com.tmax.eTest.Common.model.error_report.QErrorReport> errors = this.<com.tmax.eTest.Common.model.error_report.ErrorReport, com.tmax.eTest.Common.model.error_report.QErrorReport>createList("errors", com.tmax.eTest.Common.model.error_report.ErrorReport.class, com.tmax.eTest.Common.model.error_report.QErrorReport.class, PathInits.DIRECT2);

    public final StringPath imgSrc = createString("imgSrc");

    public final StringPath intention = createString("intention");

    public final NumberPath<Integer> probID = createNumber("probID", Integer.class);

    public final ListPath<ProblemChoice, QProblemChoice> problemChoices = this.<ProblemChoice, QProblemChoice>createList("problemChoices", ProblemChoice.class, QProblemChoice.class, PathInits.DIRECT2);

    public final ListPath<com.tmax.eTest.Common.model.uk.ProblemUKRelation, com.tmax.eTest.Common.model.uk.QProblemUKRelation> problemUKReleations = this.<com.tmax.eTest.Common.model.uk.ProblemUKRelation, com.tmax.eTest.Common.model.uk.QProblemUKRelation>createList("problemUKReleations", com.tmax.eTest.Common.model.uk.ProblemUKRelation.class, com.tmax.eTest.Common.model.uk.QProblemUKRelation.class, PathInits.DIRECT2);

    public final StringPath question = createString("question");

    public final StringPath questionInitial = createString("questionInitial");

    public final StringPath solution = createString("solution");

    public final StringPath solutionInitial = createString("solutionInitial");

    public final StringPath source = createString("source");

    public final QTestProblem testInfo;

    public final StringPath timeReco = createString("timeReco");

    public final DateTimePath<java.util.Date> valiateDate = createDateTime("valiateDate", java.util.Date.class);

    public final StringPath valiatorID = createString("valiatorID");

    public QProblem(String variable) {
        this(Problem.class, forVariable(variable), INITS);
    }

    public QProblem(Path<? extends Problem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProblem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProblem(PathMetadata metadata, PathInits inits) {
        this(Problem.class, metadata, inits);
    }

    public QProblem(Class<? extends Problem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.diagnosisInfo = inits.isInitialized("diagnosisInfo") ? new QDiagnosisProblem(forProperty("diagnosisInfo"), inits.get("diagnosisInfo")) : null;
        this.testInfo = inits.isInitialized("testInfo") ? new QTestProblem(forProperty("testInfo"), inits.get("testInfo")) : null;
    }

}

