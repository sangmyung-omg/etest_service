package com.tmax.eTest.Common.model.report;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDiagnosisReport is a Querydsl query type for DiagnosisReport
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDiagnosisReport extends EntityPathBase<DiagnosisReport> {

    private static final long serialVersionUID = 2079354955L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDiagnosisReport diagnosisReport = new QDiagnosisReport("diagnosisReport");

    public final NumberPath<Float> avgUkMastery = createNumber("avgUkMastery", Float.class);

    public final DateTimePath<java.sql.Timestamp> diagnosisDate = createDateTime("diagnosisDate", java.sql.Timestamp.class);

    public final NumberPath<Float> giScore = createNumber("giScore", Float.class);

    public final NumberPath<Integer> investItemNum = createNumber("investItemNum", Integer.class);

    public final NumberPath<Integer> investScore = createNumber("investScore", Integer.class);

    public final NumberPath<Integer> knowledgeScore = createNumber("knowledgeScore", Integer.class);

    public final NumberPath<Integer> riskScore = createNumber("riskScore", Integer.class);

    public final NumberPath<Integer> stockRatio = createNumber("stockRatio", Integer.class);

    public final com.tmax.eTest.Common.model.user.QUserMaster user;

    public final StringPath userMbti = createString("userMbti");

    public final StringPath userUuid = createString("userUuid");

    public QDiagnosisReport(String variable) {
        this(DiagnosisReport.class, forVariable(variable), INITS);
    }

    public QDiagnosisReport(Path<? extends DiagnosisReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDiagnosisReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDiagnosisReport(PathMetadata metadata, PathInits inits) {
        this(DiagnosisReport.class, metadata, inits);
    }

    public QDiagnosisReport(Class<? extends DiagnosisReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.tmax.eTest.Common.model.user.QUserMaster(forProperty("user")) : null;
    }

}

