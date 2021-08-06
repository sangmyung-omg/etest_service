package com.tmax.eTest.Common.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserMaster is a Querydsl query type for UserMaster
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserMaster extends EntityPathBase<UserMaster> {

    private static final long serialVersionUID = -1231983984L;

    public static final QUserMaster userMaster = new QUserMaster("userMaster");

    public final StringPath email = createString("email");

    public final ListPath<com.tmax.eTest.Common.model.error_report.ErrorReport, com.tmax.eTest.Common.model.error_report.QErrorReport> errors = this.<com.tmax.eTest.Common.model.error_report.ErrorReport, com.tmax.eTest.Common.model.error_report.QErrorReport>createList("errors", com.tmax.eTest.Common.model.error_report.ErrorReport.class, com.tmax.eTest.Common.model.error_report.QErrorReport.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath username = createString("username");

    public final StringPath userType = createString("userType");

    public final StringPath userUuid = createString("userUuid");

    public QUserMaster(String variable) {
        super(UserMaster.class, forVariable(variable));
    }

    public QUserMaster(Path<? extends UserMaster> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserMaster(PathMetadata metadata) {
        super(UserMaster.class, metadata);
    }

}

