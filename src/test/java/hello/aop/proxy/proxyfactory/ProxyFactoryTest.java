package hello.aop.proxy.proxyfactory;

import hello.aop.proxy.advice.TimeAdvice;
import hello.aop.proxy.common.ConcreteService;
import hello.aop.proxy.common.ServiceImpl;
import hello.aop.proxy.common.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ProxyFactoryTest {
    // ProxyFactory를 이용한 프록시 테스트

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy(){
        ServiceInterface target = new ServiceImpl();

        /*
         * 프록시 팩토리를 생성할 때, 생성자에 프록시의 호출 대상을 함께 넘겨준다.
         * 프록시 팩토리는 이 인스턴스 정보를 기반으로 프록시를 만들어낸다.
         * 만약 이 인스턴스에 인터페이스가 있다면 JDK 동적 프록시를 기본으로 사용하고 인터페이스가 없고 구체 클래스만 있다면 CGLIB를 통해서 동적 프록시를 생성한다.
         * 여기서는 target 이 new ServiceImpl() 의 인스턴스이기 때문에 ServiceInterface 인터페이스가 있다.
         * 따라서 이 인터페이스를 기반으로 JDK 동적 프록시를 생성한다.
         */
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());   // 프록시 팩토리를 통해서 만든 프록시가 사용할 부가 기능 로직을 설정한다
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();    // 프록시 객체를 생성하고 그 결과를 받는다.
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy(){
        ConcreteService target = new ConcreteService();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("ProxyTargerClass 옵션을 사용하면 인터페이스가 있어도 CGLOB를 사용하요, 클래스 기반 프록시 사용")
    void proxyTargetClass(){
        ServiceInterface target = new ServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        // 프록시 팩토리는 proxyTargetClass 라는 옵션을 제공하는데, 이 옵션에 true 값을 넣으면 인터페이스가 있어도 강제로 CGLIB를 사용한다. 그리고 인터페이스가 아닌 클래스 기반의 프록시를 만들어준다
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvice(new TimeAdvice());

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    /*
     * 참고
     * 스프링 부트는 AOP를 적용할 때 기본적으로 proxyTargetClass=true 로 설정해서 사용한다.
     * 따라서 인터페이스가 있어도 항상 CGLIB를 사용해서 구체 클래스를 기반으로 프록시를 생성한다.
     */
}

