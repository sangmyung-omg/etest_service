package com.tmax.eTest.Common.model.report;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMinitestReport is a Querydsl query type for MinitestReport
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMinitestReport extends EntityPathBase<MinitestReport> {

    private static final long serialVersionUID = 489899639L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMinitestReport minitestReport = new QMinitestReport("minitestReport");

    public final NumberPath<Float> avgUkMastery = createNumber("avgUkMastery", Float.class);

    public final NumberPath<Integer> correctNum = createNumber("correctNum", Integer.class);

    public final NumberPath<Integer> dunnoNum = createNumber("dunnoNum", Integer.class);

    public final DateTimePath<java.sql.Timestamp> minitestDate = createDateTime("minitestDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> setNum = createNumber("setNum", Integer.class);

    public final com.tmax.eTest.Common.model.user.QUserMaster user;

    public final StringPath userUuid = createString("userUuid");

    public final NumberPath<Integer> wrongNum = createNumber("wrongNum", Integer.class);

    public QMinitestReport(String variable) {
        this(MinitestReport.class, forVariable(variable), INITS);
    }

    public QMinitestReport(Path<? extends MinitestReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMinitestReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMinitestReport(PathMetadata metadata, PathInits inits) {
        this(MinitestReport.class, metadata, inits);
    }

    public QMinitestReport(Class<? extends MinitestReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.tmax.eTest.Common.model.user.QUserMaster(forProperty("user")) : null;
    }

}

