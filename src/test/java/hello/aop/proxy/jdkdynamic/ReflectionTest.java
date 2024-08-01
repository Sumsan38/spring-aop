package hello.aop.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {
    // 스프링 - 고급편 (Proxy 실습)

    @Test
    void reflection0(){
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA();
        log.info("result={}", result1);
        // 공통 로직1 종료

        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callB();
        log.info("result={}", result2);
        // 공통 로직2 종료

    }

    @Test
    void reflection1() throws Exception {
        // 클래스 정보
        Class<?> classHello = Class.forName("com.my.bob.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        // callA 메소드 정보
        Method methodCallA = classHello.getMethod("callA"); // getMethod로 해당 클래스의 메서드 메타 정보 휙득
        Object result1 = methodCallA.invoke(target);
        log.info("result={}", result1);

        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result={}", result2);
    }

    /**
     * 정리
     * 정적인 target.callA() , target.callB() 코드를 리플렉션을 사용해서 Method 라는 메타정보로 추상화했
     * 다. 덕분에 공통 로직을 만들 수 있게 되었다
     *
     * 주의
     * 리플렉션을 사용하면 클래스와 메서드의 메타정보를 사용해서 애플리케이션을 동적으로 유연하게 만들 수 있다. 하지만
     * 리플렉션 기술은 런타임에 동작하기 때문에, 컴파일 시점에 오류를 잡을 수 없다.
     * 따라서 리플렉션은 일반적으로 사용하면 안된다. 지금까지 프로그래밍 언어가 발달하면서 타입 정보를 기반으로 컴파일
     * 시점에 오류를 잡아준 덕분에 개발자가 편하게 살았는데, 리플렉션은 그것에 역행하는 방식이다.
     * 리플렉션은 프레임워크 개발이나 또는 매우 일반적인 공통 처리가 필요할 때 부분적으로 주의해서 사용해야 한다.
     * @throws Exception
     */
    @Test
    void reflection2() throws Exception{
        // 클래스 정보
        Class<?> classHello = Class.forName("com.my.bob.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    private void dynamicCall(Method method, Object target) throws Exception {
        Object result = method.invoke(target);
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello{
        public String callA(){
            log.info("callA");
            return "A";
        }
        public String callB(){
            log.info("callB");
            return "B";
        }
    }
}
