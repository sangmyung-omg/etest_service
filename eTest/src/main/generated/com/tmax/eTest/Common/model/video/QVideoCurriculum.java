package com.tmax.eTest.Common.model.video;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QVideoCurriculum is a Querydsl query type for VideoCurriculum
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVideoCurriculum extends EntityPathBase<VideoCurriculum> {

    private static final long serialVersionUID = 576181731L;

    public static final QVideoCurriculum videoCurriculum = new QVideoCurriculum("videoCurriculum");

    public final NumberPath<Long> curriculumId = createNumber("curriculumId", Long.class);

    public final StringPath subject = createString("subject");

    public QVideoCurriculum(String variable) {
        super(VideoCurriculum.class, forVariable(variable));
    }

    public QVideoCurriculum(Path<? extends VideoCurriculum> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVideoCurriculum(PathMetadata metadata) {
        super(VideoCurriculum.class, metadata);
    }

}

