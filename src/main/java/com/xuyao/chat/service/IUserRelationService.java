package com.xuyao.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuyao.chat.bean.po.UserRelation;
import com.xuyao.chat.bean.vo.UserRelationVO;

import java.util.List;

public interface IUserRelationService extends IService<UserRelation> {

    boolean add(Long friendId);

    List<UserRelationVO> list(Long lastId, Integer size);

    boolean delete(Long friendId);

}
