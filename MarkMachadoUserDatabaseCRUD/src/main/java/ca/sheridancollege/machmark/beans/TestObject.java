package ca.sheridancollege.machmark.beans;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestObject {

	
	//Fields for Test Object
	private Long id;
	private String name;
	private int age;
	//When entering Date it will show in this format
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;
}
