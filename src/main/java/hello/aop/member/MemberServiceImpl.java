package hello.aop.member;

import hello.aop.member.annotation.ClassAop;
import org.springframework.stereotype.Component;

@ClassAop
@Component
public class MemberServiceImpl implements MemberService {
    // AOP 포인트컷 테스트를 위한 소스

    @Override
    public String hello(String param) {
        return "ok";
    }

    public String internal(String param) {
        return "ok";
    }

}
