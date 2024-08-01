package hello.aop.proxy.cglib;

import hello.aop.proxy.cglib.code.TimeMethodInterceptor;
import hello.aop.proxy.common.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {
    /**
     * CGLIB는 바이트코드를 조작해서 동적으로 클래스를 생성하는 기술을 제공하는 라이브러리이다.
     * CGLIB를 사용하면 인터페이스가 없어도 구체 클래스만 가지고 동적 프록시를 만들어낼 수 있다.
     * CGLIB는 원래는 외부 라이브러리인데, 스프링 프레임워크가 스프링 내부 소스 코드에 포함했다. 따라서 스프링을 사용한다면 별도의 외부 라이브러리를 추가하지 않아도 사용할 수 있다.
     * 참고로 우리가 CGLIB를 직접 사용하는 경우는 거의 없다.
     * 이후에 설명할 스프링의 ProxyFactory 라는 것이 이 기술을 편리하게 사용하게 도와주기 때문에, 너무 깊이있게 파기 보다는 CGLIB가 무엇인지 대략 개념만 잡으면 된다.
     * 예제 코드로 CGLIB를 간단히 이해해보자
     */

    @Test
    void cglib(){
        ConcreteService target = new ConcreteService();

        Enhancer enhancer = new Enhancer();     // CGLIB는 Enhancer 를 사용해서 프록시를 생성한다
        enhancer.setSuperclass(ConcreteService.class);  // CGLIB는 구체 클래스를 상속 받아서 프록시를 생성할 수 있다. 어떤 구체 클래스를 상속 받을지 지정한다.
        enhancer.setCallback(new TimeMethodInterceptor(target));    // 프록시에 적용할 실행 로직을 할당한다.

        ConcreteService proxy = (ConcreteService) enhancer.create();    // 프록시를 생성한다. 앞서 설정한 enhancer.setSuperclass(ConcreteService.class) 에서 지정한 클래스를 상속 받아서 프록시가 만들어진다
        log.info("tartClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();
    }
    // Q: 인터페이스가 있는 경우에는 JDK 동적 프록시를 적용하고, 그렇지 않은 경우에는 CGLIB를 적용하려면 어떻게 해야할까
}
