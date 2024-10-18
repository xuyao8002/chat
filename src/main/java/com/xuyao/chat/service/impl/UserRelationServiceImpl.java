package com.xuyao.chat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyao.chat.bean.po.User;
import com.xuyao.chat.bean.po.UserRelation;
import com.xuyao.chat.bean.vo.UserRelationVO;
import com.xuyao.chat.bean.vo.UserVO;
import com.xuyao.chat.dao.UserRelationMapper;
import com.xuyao.chat.service.IUserRelationService;
import com.xuyao.chat.service.IUserService;
import com.xuyao.chat.util.ContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if(user.getUserId().equals(friendId)){
            throw new RuntimeException("不能添加自己");
        }
        Long count = super.baseMapper.selectCount(Wrappers.lambdaQuery(UserRelation.class).eq(UserRelation::getUserId, user.getUserId())
                .eq(UserRelation::getFriendId, friendId));
        if(count > 0){
            throw new RuntimeException("好友关系已存在");
        }
        List<UserRelation> relations = new ArrayList<>();
        relations.add(UserRelation.builder().userId(user.getUserId()).friendId(friendId).build());
        relations.add(UserRelation.builder().userId(friendId).friendId(user.getUserId()).build());
        return super.saveBatch(relations);
    }

    @Override
    public List<UserRelationVO> list(Long lastId, Integer size) {
        UserVO user = ContextUtil.getUser();
        List<UserRelation> userRelations = baseMapper.selectList(Wrappers.lambdaQuery(UserRelation.class).eq(UserRelation::getUserId, user.getUserId())
                .eq(UserRelation::getDeleted, 0).gt(UserRelation::getFriendId, lastId)
                .orderByAsc(UserRelation::getFriendId).last("limit " + size));
        Map<Long, User> userMap = userService.userMap(userRelations.stream().map(UserRelation::getFriendId).collect(Collectors.toList()));
        return userRelations.stream().map(relation -> {
            UserRelationVO vo = new UserRelationVO();
            BeanUtils.copyProperties(relation, vo);
            vo.setFriendName(userMap.get(relation.getFriendId()).getUserName());
            return vo;
        }).collect(Collectors.toList());
    }
}
