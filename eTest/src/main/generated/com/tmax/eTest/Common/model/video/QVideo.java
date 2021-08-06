package com.tmax.eTest.Common.model.video;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVideo is a Querydsl query type for Video
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVideo extends EntityPathBase<Video> {

    private static final long serialVersionUID = -2093275256L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVideo video = new QVideo("video");

    public final DateTimePath<java.sql.Timestamp> createDate = createDateTime("createDate", java.sql.Timestamp.class);

    public final StringPath creatorId = createString("creatorId");

    public final NumberPath<Long> curriculumId = createNumber("curriculumId", Long.class);

    public final StringPath imgSrc = createString("imgSrc");

    public final StringPath originalFileSrc = createString("originalFileSrc");

    public final StringPath title = createString("title");

    public final NumberPath<Float> totalTime = createNumber("totalTime", Float.class);

    public final SetPath<VideoBookmark, QVideoBookmark> videoBookmarks = this.<VideoBookmark, QVideoBookmark>createSet("videoBookmarks", VideoBookmark.class, QVideoBookmark.class, PathInits.DIRECT2);

    public final QVideoCurriculum videoCurriculum;

    public final QVideoHit videoHit;

    public final NumberPath<Long> videoId = createNumber("videoId", Long.class);

    public final StringPath videoSrc = createString("videoSrc");

    public final SetPath<VideoUkRel, QVideoUkRel> videoUks = this.<VideoUkRel, QVideoUkRel>createSet("videoUks", VideoUkRel.class, QVideoUkRel.class, PathInits.DIRECT2);

    public QVideo(String variable) {
        this(Video.class, forVariable(variable), INITS);
    }

    public QVideo(Path<? extends Video> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVideo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVideo(PathMetadata metadata, PathInits inits) {
        this(Video.class, metadata, inits);
    }

    public QVideo(Class<? extends Video> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.videoCurriculum = inits.isInitialized("videoCurriculum") ? new QVideoCurriculum(forProperty("videoCurriculum")) : null;
        this.videoHit = inits.isInitialized("videoHit") ? new QVideoHit(forProperty("videoHit")) : null;
    }

}

