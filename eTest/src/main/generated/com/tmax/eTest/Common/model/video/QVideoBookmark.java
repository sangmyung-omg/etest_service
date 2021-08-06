package com.tmax.eTest.Common.model.video;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVideoBookmark is a Querydsl query type for VideoBookmark
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVideoBookmark extends EntityPathBase<VideoBookmark> {

    private static final long serialVersionUID = -991876898L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVideoBookmark videoBookmark = new QVideoBookmark("videoBookmark");

    public final StringPath userUuid = createString("userUuid");

    public final QVideo video;

    public final NumberPath<Long> videoId = createNumber("videoId", Long.class);

    public QVideoBookmark(String variable) {
        this(VideoBookmark.class, forVariable(variable), INITS);
    }

    public QVideoBookmark(Path<? extends VideoBookmark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVideoBookmark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVideoBookmark(PathMetadata metadata, PathInits inits) {
        this(VideoBookmark.class, metadata, inits);
    }

    public QVideoBookmark(Class<? extends VideoBookmark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.video = inits.isInitialized("video") ? new QVideo(forProperty("video"), inits.get("video")) : null;
    }

}

