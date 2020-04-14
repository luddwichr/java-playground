package reflection;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DependencyManagerTest {

    private final DependencyManager dependencyManager = new DependencyManager();

    @Test
    void getInstance_classWithNoDependencies() {
        A a = dependencyManager.getInstance(A.class);
        assertThat(a).isNotNull();
        assertThat(dependencyManager.getInstance(A.class)).isSameAs(a);
    }

    @Test
    void getInstance_isSingleton() {
        A a1 = dependencyManager.getInstance(A.class);
        A a2 = dependencyManager.getInstance(A.class);
        assertThat(a2).isSameAs(a1);
    }

    @Test
    void getInstance_classWithOneDependency() {
        B b = dependencyManager.getInstance(B.class);
        assertThat(b).isNotNull();
        assertThat(b.a).isSameAs(dependencyManager.getInstance(A.class));
    }

    @Test
    void getInstance_classWithRecursiveDependencies() {
        C c = dependencyManager.getInstance(C.class);
        assertThat(c).isNotNull();
        assertThat(c.b).isSameAs(dependencyManager.getInstance(B.class));
        assertThat(c.b.a).isSameAs(dependencyManager.getInstance(A.class));
    }

    @Test
    void getInstance_classWithTwoDependencies() {
        D d = dependencyManager.getInstance(D.class);
        assertThat(d).isNotNull();
        assertThat(d.a).isSameAs(dependencyManager.getInstance(A.class));
        assertThat(d.b).isSameAs(dependencyManager.getInstance(B.class));
    }

}

class A {

}

class B {
    public final A a;

    B(A a) {
        this.a = a;
    }
}

class C {
    public final B b;

    C(B b) {
        this.b = b;
    }
}

class D {
    public final A a;
    public final B b;

    D(A a, B b) {
        this.a = a;
        this.b = b;
    }
}