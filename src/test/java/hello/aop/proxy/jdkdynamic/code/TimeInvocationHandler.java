package hello.aop.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

    private final Object target; // 동적 프록시가 호출할 대상

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    /**
     * JDK 동적 프록시에 적용할 공통 로직
     * @param proxy 프록시 자신
     * @param method 호출한 메서드
     * @param args : 메서드를 호출할 때 전달한 인수
     * @return result
     * @throws Throwable 프록시 관련 exception
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        // 리플렉션을 사용해서 target 인스턴스의 메서드를 실행한다. args는 메서드 호출시 넘겨줄 인수이다
        Object result = method.invoke(target, args);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime={}", resultTime);

        return result;
    }
}
