package com.cmci.user.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto extends UserVo{
    List<UserDto> userList;

    int addCnt;
    int modifyCnt;
    int removeCnt;
}
