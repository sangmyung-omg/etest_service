package com.tmax.eTest.Common.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserEmbedding is a Querydsl query type for UserEmbedding
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserEmbedding extends EntityPathBase<UserEmbedding> {

    private static final long serialVersionUID = 1135931497L;

    public static final QUserEmbedding userEmbedding1 = new QUserEmbedding("userEmbedding1");

    public final DateTimePath<java.sql.Timestamp> updateDate = createDateTime("updateDate", java.sql.Timestamp.class);

    public final StringPath userEmbedding = createString("userEmbedding");

    public final StringPath userUuid = createString("userUuid");

    public QUserEmbedding(String variable) {
        super(UserEmbedding.class, forVariable(variable));
    }

    public QUserEmbedding(Path<? extends UserEmbedding> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEmbedding(PathMetadata metadata) {
        super(UserEmbedding.class, metadata);
    }

}

