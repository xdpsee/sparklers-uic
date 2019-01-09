namespace java com.zhenhui.demo.uic.api.domain

struct UserDto {
    1:i64 id

    2:string name

    3:string avatar

    4:set<string> authorities = {}

}
