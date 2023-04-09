package com.xuyao.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuyao.chat.bean.po.UserRelation;

public interface IUserRelationService extends IService<UserRelation> {

    boolean add(Long friendId);

}
