package com.xuyao.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyao.chat.bean.po.UserRelation;
import com.xuyao.chat.bean.vo.UserVO;
import com.xuyao.chat.dao.UserRelationMapper;
import com.xuyao.chat.service.IUserRelationService;
import com.xuyao.chat.service.IUserService;
import com.xuyao.chat.util.ContextUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserRelationServiceImpl extends ServiceImpl<UserRelationMapper, UserRelation> implements IUserRelationService {

    @Resource
    private IUserService userService;

    @Override
    public boolean add(Long friendId) {
        if (!userService.exist(friendId)) {
            throw new RuntimeException("好友不存在");
        }
        UserVO user = ContextUtil.getUser();
        UserRelation relation = new UserRelation();
        relation.setUserId(user.getUserId());
        relation.setFriendId(friendId);
        return super.save(relation);
    }
}
