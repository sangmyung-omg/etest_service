package com.tmax.eTest.Common.model.error_report;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QErrorReport is a Querydsl query type for ErrorReport
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QErrorReport extends EntityPathBase<ErrorReport> {

    private static final long serialVersionUID = -219861831L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QErrorReport errorReport = new QErrorReport("errorReport");

    public final NumberPath<Long> errorID = createNumber("errorID", Long.class);

    public final NumberPath<Long> probID = createNumber("probID", Long.class);

    public final com.tmax.eTest.Common.model.problem.QProblem problem;

    public final StringPath reportText = createString("reportText");

    public final StringPath reportType = createString("reportType");

    public final com.tmax.eTest.Common.model.user.QUserMaster user;

    public final StringPath userUUID = createString("userUUID");

    public QErrorReport(String variable) {
        this(ErrorReport.class, forVariable(variable), INITS);
    }

    public QErrorReport(Path<? extends ErrorReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QErrorReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QErrorReport(PathMetadata metadata, PathInits inits) {
        this(ErrorReport.class, metadata, inits);
    }

    public QErrorReport(Class<? extends ErrorReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.problem = inits.isInitialized("problem") ? new com.tmax.eTest.Common.model.problem.QProblem(forProperty("problem"), inits.get("problem")) : null;
        this.user = inits.isInitialized("user") ? new com.tmax.eTest.Common.model.user.QUserMaster(forProperty("user")) : null;
    }

}

