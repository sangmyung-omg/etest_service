package com.tmax.eTest.Common.model.video;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVideoUkRel is a Querydsl query type for VideoUkRel
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVideoUkRel extends EntityPathBase<VideoUkRel> {

    private static final long serialVersionUID = -1013448837L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVideoUkRel videoUkRel = new QVideoUkRel("videoUkRel");

    public final NumberPath<Long> ukId = createNumber("ukId", Long.class);

    public final com.tmax.eTest.Common.model.uk.QUkMaster ukMaster;

    public final NumberPath<Long> videoId = createNumber("videoId", Long.class);

    public QVideoUkRel(String variable) {
        this(VideoUkRel.class, forVariable(variable), INITS);
    }

    public QVideoUkRel(Path<? extends VideoUkRel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVideoUkRel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVideoUkRel(PathMetadata metadata, PathInits inits) {
        this(VideoUkRel.class, metadata, inits);
    }

    public QVideoUkRel(Class<? extends VideoUkRel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ukMaster = inits.isInitialized("ukMaster") ? new com.tmax.eTest.Common.model.uk.QUkMaster(forProperty("ukMaster")) : null;
    }

}

