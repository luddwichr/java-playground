package reflection;

@AnnotationForRuntime
public class Person {

	private String name;
	private Integer age;
	private Boolean married;

	public Person(String name, Integer age, Boolean married) {
		this.name = name;
		this.age = age;
		this.married = married;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Boolean getMarried() {
		return married;
	}

	public void setMarried(Boolean married) {
		this.married = married;
	}

	protected void protectedMethod() {

	}

	void packagePrivateMethod() {

	}

	private void privateMethod() {

	}
}
