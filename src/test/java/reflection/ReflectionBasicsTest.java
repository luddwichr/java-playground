package reflection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionBasicsTest {

    @Test
    void createInstance() {
        List<Object> parameters = Arrays.asList("Hans", 99, true);
        Person person = ReflectionBasics.createInstance(Person.class, parameters);
        assertThat(person).isEqualToComparingFieldByField(new Person("Hans", 99, true));
    }

    @Test
    void getMethodNames() {
        Person person = new Person("Erna", 42, true);
        List<String> methodNames = ReflectionBasics.getMethodNames(person);
        assertThat(methodNames)
                .contains("getName", "setName", "getMarried", "setMarried", "getAge", "setAge")
                .contains("protectedMethod", "packagePrivateMethod", "privateMethod");
    }

    @Test
    void getAnnotationClasses() {
        Person person = new Person("Erna", 42, true);
        List<Class<?>> annotationClasses = ReflectionBasics.getAnnotationClasses(person);
        assertThat(annotationClasses).containsExactly(AnnotationForRuntime.class);
    }

    @Test
    void getNamesOfFields() {
        Person person = new Person("Erna", 42, true);
        List<String> namesOfFields = ReflectionBasics.getNamesOfFields(person);
        assertThat(namesOfFields).containsExactlyInAnyOrder("name", "age", "married");
    }

    @Test
    void getValuesOfFields() {
        Person person = new Person("Erna", 42, true);
        List<Object> valuesOfFields = ReflectionBasics.getValuesOfFields(person);
        assertThat(valuesOfFields).containsExactlyInAnyOrder("Erna", 42, true);
    }

    @Test
    void changeValuesOfPrivateFinalFields() {
        ClassForPrivateFinalFieldValueChanges instance = new ClassForPrivateFinalFieldValueChanges("A", 321, true);
        Map<String, Object> valuePerField = new HashMap<>();
        valuePerField.put("string", "B");
        valuePerField.put("integer", 123);
        valuePerField.put("aBoolean", false);

        ReflectionBasics.changeValuesOfPrivateFinalFields(instance, valuePerField);

        assertThat(instance).isEqualToComparingFieldByField(new ClassForPrivateFinalFieldValueChanges("B", 123, false));
    }

    @Test
    void changeTypeOfPrivateFinalFields() {
        ClassForPrivateFinalFieldTypeChanges instance = new ClassForPrivateFinalFieldTypeChanges("A", 321, true);

        Map<String, Object> valuePerField = new HashMap<>();
        valuePerField.put("string", 123);
        valuePerField.put("integer", false);
        valuePerField.put("aBoolean", "yes");

        ReflectionBasics.changeTypesOfPrivateFinalFields(instance, valuePerField);

        assertThat(ReflectionBasics.getValuesOfFields(instance))
                .containsExactlyInAnyOrder(123, false, "yes");
    }

}

class ClassForPrivateFinalFieldValueChanges {

    private final String string;
    private final Integer integer;
    private final Boolean aBoolean;

    public ClassForPrivateFinalFieldValueChanges(String string, Integer integer, Boolean aBoolean) {
        this.string = string;
        this.integer = integer;
        this.aBoolean = aBoolean;
    }
}

class ClassForPrivateFinalFieldTypeChanges {

    private final String string;
    private final Integer integer;
    private final Boolean aBoolean;

    public ClassForPrivateFinalFieldTypeChanges(String string, Integer integer, Boolean aBoolean) {
        this.string = string;
        this.integer = integer;
        this.aBoolean = aBoolean;
    }
}
