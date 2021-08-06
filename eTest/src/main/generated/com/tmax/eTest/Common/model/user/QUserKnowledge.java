package com.tmax.eTest.Common.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserKnowledge is a Querydsl query type for UserKnowledge
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserKnowledge extends EntityPathBase<UserKnowledge> {

    private static final long serialVersionUID = -200666736L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserKnowledge userKnowledge = new QUserKnowledge("userKnowledge");

    public final com.tmax.eTest.Common.model.uk.QUkMaster ukDao;

    public final NumberPath<Integer> ukId = createNumber("ukId", Integer.class);

    public final NumberPath<Float> ukMastery = createNumber("ukMastery", Float.class);

    public final DateTimePath<java.sql.Timestamp> updateDate = createDateTime("updateDate", java.sql.Timestamp.class);

    public final StringPath userUuid = createString("userUuid");

    public QUserKnowledge(String variable) {
        this(UserKnowledge.class, forVariable(variable), INITS);
    }

    public QUserKnowledge(Path<? extends UserKnowledge> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserKnowledge(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserKnowledge(PathMetadata metadata, PathInits inits) {
        this(UserKnowledge.class, metadata, inits);
    }

    public QUserKnowledge(Class<? extends UserKnowledge> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ukDao = inits.isInitialized("ukDao") ? new com.tmax.eTest.Common.model.uk.QUkMaster(forProperty("ukDao")) : null;
    }

}

