package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

// see https://www.baeldung.com/java-reflection for more on how reflection can be used
public class ReflectionBasics {

	public static <T> T createInstance(Class<T> clazz, List<Object> parameters) {
		Class<?>[] parameterTypes = parameters.stream().map(Object::getClass).toArray(Class<?>[]::new);
		try {
			Constructor<T> constructor = clazz.getConstructor(parameterTypes);
			return constructor.newInstance(parameters.toArray());
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> getNamesOfPrivateFields(Object object) {
		return Stream.of(object.getClass().getDeclaredFields())
				.map(Field::getName)
				.collect(toList());
	}

	public static List<Object> getValuesOfPrivateFields(Object object) {
		return Stream.of(object.getClass().getDeclaredFields())
				.peek(field -> field.setAccessible(true))
				.map(field -> getFieldValue(field, object))
				.collect(toList());
	}

	private static Object getFieldValue(Field field, Object object) {
		try {
			return field.get(object);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static void changeValuesOfPrivateFinalFields(Object object, Map<String, Object> valuePerField) {
		for (Map.Entry<String, Object> entry : valuePerField.entrySet()) {
			try {
				Field field = object.getClass().getDeclaredField(entry.getKey());
				field.setAccessible(true);
				field.set(object, entry.getValue());
			} catch (NoSuchFieldException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void changeTypesOfPrivateFinalFields(Object object, Map<String, Object> valuePerField) {
		for (Map.Entry<String, Object> entry : valuePerField.entrySet()) {
			try {
				Field field = object.getClass().getDeclaredField(entry.getKey());

				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

				Field fieldType = Field.class.getDeclaredField("type");
				fieldType.setAccessible(true);
				fieldType.set(field, entry.getValue().getClass());

				field.setAccessible(true);
				field.set(object, entry.getValue());
			} catch (NoSuchFieldException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

	}

	public static List<String> getMethodNames(Object object) {
		return Stream.of(object.getClass().getDeclaredMethods()).map(Method::getName).collect(toList());
	}

	public static List<Class<?>> getAnnotationClasses(Object object) {
		return Stream.of(object.getClass().getDeclaredAnnotations()).map(Annotation::annotationType).collect(toList());
	}

}

