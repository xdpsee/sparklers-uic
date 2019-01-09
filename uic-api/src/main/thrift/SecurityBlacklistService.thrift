namespace java com.zhenhui.demo.uic.api.service


service SecurityBlacklistService {

    bool isBlocked(1:string token)

    void block(1:string token)
}
