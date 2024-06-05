package ca.sheridancollege.machmark.beans;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class User {

	//This is fields for a user
	@NonNull
	private String firstName;
	@NonNull
	private String lastName;
	private Long userId;
	@NonNull
	private String email;
	@NonNull
	private String encryptedPassword;
	private Boolean enabled;
}
