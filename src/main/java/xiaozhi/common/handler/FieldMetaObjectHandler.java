package xiaozhi.common.handler;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import xiaozhi.common.constant.Constant;
import xiaozhi.common.user.UserDetail;

/**
 * 公共字段，自动填充值
 * Copyright (c) 人人开源 All rights reserved.
 * Website: https://www.renren.io
 */
@Component
public class FieldMetaObjectHandler implements MetaObjectHandler {
    private final static String CREATE_DATE = "createDate";
    private final static String CREATOR = "creator";
    private final static String UPDATE_DATE = "updateDate";
    private final static String UPDATER = "updater";

    private final static String DATA_OPERATION = "dataOperation";

    @Override
    public void insertFill(MetaObject metaObject) {
        UserDetail user = null; // TODO: Replace with proper authentication
        Date date = new Date();

        // 创建者 (認証無効のため一時的にデフォルト値)
        strictInsertFill(metaObject, CREATOR, Long.class, 1L);
        // 创建时间
        strictInsertFill(metaObject, CREATE_DATE, Date.class, date);

        // 更新者 (認証無効のため一時的にデフォルト値)
        strictInsertFill(metaObject, UPDATER, Long.class, 1L);
        // 更新时间
        strictInsertFill(metaObject, UPDATE_DATE, Date.class, date);

        // 数据标识
        strictInsertFill(metaObject, DATA_OPERATION, String.class, Constant.DataOperation.INSERT.getValue());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新者 (認証無効のため一時的にデフォルト値)
        strictUpdateFill(metaObject, UPDATER, Long.class, 1L);
        // 更新时间
        strictUpdateFill(metaObject, UPDATE_DATE, Date.class, new Date());

        // 数据标识
        strictInsertFill(metaObject, DATA_OPERATION, String.class, Constant.DataOperation.UPDATE.getValue());
    }
}