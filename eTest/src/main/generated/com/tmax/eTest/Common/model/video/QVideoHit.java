package com.tmax.eTest.Common.model.video;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QVideoHit is a Querydsl query type for VideoHit
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVideoHit extends EntityPathBase<VideoHit> {

    private static final long serialVersionUID = -2132908309L;

    public static final QVideoHit videoHit = new QVideoHit("videoHit");

    public final NumberPath<Integer> hit = createNumber("hit", Integer.class);

    public final NumberPath<Long> videoId = createNumber("videoId", Long.class);

    public QVideoHit(String variable) {
        super(VideoHit.class, forVariable(variable));
    }

    public QVideoHit(Path<? extends VideoHit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVideoHit(PathMetadata metadata) {
        super(VideoHit.class, metadata);
    }

}

