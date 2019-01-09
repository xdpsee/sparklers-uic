namespace java com.zhenhui.demo.uic.api.service

include "User.thrift"

service UserQueryService {

    User.UserDto queryUser(1:i64 userId)

    map<i64, User.UserDto> queryUsers(1:set<i64> userIds)

}

