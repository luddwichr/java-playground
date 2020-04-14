package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DependencyManager {

    private final Map<Class<?>, Object> singletonInstancePerClass = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> classToInstantiate) {
        if (!singletonInstancePerClass.containsKey(classToInstantiate)) {
            singletonInstancePerClass.put(classToInstantiate, instantiate(classToInstantiate));
        }
        return (T) singletonInstancePerClass.get(classToInstantiate);
    }

    private <T> T instantiate(Class<T> classToInstantiate) {
        try {
            Constructor<T> constructor = getConstructor(classToInstantiate);
            Object[] parametersForConstructor = getInstances(constructor.getParameterTypes());
            return constructor.newInstance(parametersForConstructor);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to instantiate " + classToInstantiate.getName() + "!", e);
        }
    }

    private <T> Constructor<T> getConstructor(Class<T> classToInstantiate) {
        Constructor<?>[] availableConstructors = classToInstantiate.getDeclaredConstructors();
        if (availableConstructors.length != 1) {
            throw new IllegalStateException(classToInstantiate.getName() + " must have exactly one constructor!");
        }
        Class<?>[] parameterTypesForConstructor = availableConstructors[0].getParameterTypes();
        try {
            return classToInstantiate.getDeclaredConstructor(parameterTypesForConstructor);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Failed to instantiate " + classToInstantiate.getName() + "!", e);
        }
    }

    private Object[] getInstances(Class<?>[] dependencies) {
        return Stream.of(dependencies).map(this::getInstance).toArray(Object[]::new);
    }

}
