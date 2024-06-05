package ca.sheridancollege.machmark.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.machmark.beans.TestObject;
import ca.sheridancollege.machmark.beans.User;

@Repository
public class DatabaseAccess {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	protected NamedParameterJdbcTemplate jdbc;

//Method to get the testObject list
	public List<TestObject> getList() {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "SELECT * FROM TestObject";

		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<>(TestObject.class));
	}

//Method to get a specific testObject query using the testObject object
	public List<TestObject> getObject(TestObject testObject) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		namedParameters.addValue("id", testObject.getId());
		String query = "SELECT * FROM TestObject WHERE id = :id";

		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<>(TestObject.class));
	}

	// method to receive a testObject by the id
	public TestObject getObjectById(Long Id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", Id);
		String query = "SELECT * FROM TestObject WHERE id = :id";

		List<TestObject> testObjectList = jdbc.query(query, namedParameters, new BeanPropertyRowMapper<>(TestObject.class));

		if (!testObjectList.isEmpty()) {
			return testObjectList.get(0); // Return the first TestObject object
		} else {
			return null; // Return null if no TestObject objects were found
		}
	}

	// This gathers all the information in the form binded page and adds it to the
	// database
	public void add(TestObject testObject) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("name", testObject.getName());
		namedParameters.addValue("age", testObject.getAge());
		namedParameters.addValue("birthday", testObject.getBirthday());
		String query = "INSERT INTO TestObject(name,age,birthday) VALUES (:name, :age, :birthday)";
		jdbc.update(query, namedParameters);
	}

	// This finds the testObject at a specified id and deletes it
	public void deleteByObject(TestObject testObject) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", testObject.getId());
		String query = "DELETE FROM TestObject WHERE id = :id";
		jdbc.update(query, namedParameters);
	}

	//This finds a testObject by id and deletes it
	public void deleteByID(Long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", id);
		String query = "DELETE FROM TestObject WHERE id = :id";
		jdbc.update(query, namedParameters);
	}

	//This saves the object using the REST API
	public Long save(TestObject testObject) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		String query = "INSERT INTO TestObject(name,age,birthday) VALUES (:name, :age, :birthday)";
		namedParameters.addValue("name", testObject.getName());
		namedParameters.addValue("age", testObject.getAge());
		namedParameters.addValue("birthday", testObject.getBirthday());
		jdbc.update(query, namedParameters, generatedKeyHolder);
		return (Long) generatedKeyHolder.getKey();
	}

	//This deletes the whole collection
	public void deleteAll() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "DELETE FROM TestObject";
		jdbc.update(query, namedParameters);
	}

	//This returns a count on how many entries are in the collection
	public Long count() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT count(*) FROM TestObject";
		return jdbc.queryForObject(query, namedParameters, Long.TYPE);
	}

	//This saves all items into a collection
	public void saveAll(List<TestObject> TestObjectList) {
		for (TestObject s : TestObjectList) {
			save(s);
		}
	}

	//This updates an entry by using the current items id and puts in the new item
	public int updateById(Long id, TestObject testObject) {
	    String query = "UPDATE TestObject SET name = :name, age = :age, birthday = :birthday WHERE id = :id";
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("id", id);
	    namedParameters.addValue("name", testObject.getName());
	    namedParameters.addValue("age", testObject.getAge());
	    namedParameters.addValue("birthday", testObject.getBirthday());
	    return jdbc.update(query, namedParameters);
	}
//--------------------------------------------------------------------------------------------
	// Anything below is security related with users

	// This adds the user to the database using the form in register
	public void addUser(String email, String password, String firstName, String lastName) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "INSERT INTO sec_user " + "(firstName, lastName, email, encryptedPassword, enabled)"
				+ " VALUES (:firstName, :lastName, :email, :encryptedPassword, 1)";
		parameters.addValue("email", email);
		parameters.addValue("firstName", firstName);
		parameters.addValue("lastName", lastName);
		// Here, it takes the password and encrypts it so it can be hidden in the
		// database
		parameters.addValue("encryptedPassword", passwordEncoder.encode(password));
		jdbc.update(query, parameters);

	}

	// This adds a role, this is used when a new user is added into the database
	public void addRole(Long userId, Long roleId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "INSERT INTO user_role (userId, roleId)" + " VALUES (:userId, :roleId)";
		parameters.addValue("userId", userId);
		parameters.addValue("roleId", roleId);
		jdbc.update(query, parameters);
	}

	// This searches the database for an email that matches, if not found it returns
	// null
	public User findUserAccount(String email) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user where email = :email";
		namedParameters.addValue("email", email);
		try {
			return jdbc.queryForObject(query, namedParameters, new BeanPropertyRowMapper<>(User.class));
		} catch (EmptyResultDataAccessException erdae) {
			return null;
		}
	}

	// This will find the users role by using the userid and cross referencing it
	public List<String> getRolesById(Long userId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT sec_role.roleName " + "FROM user_role, sec_role "
				+ "WHERE user_role.roleId = sec_role.roleId " + "AND userId = :userId";
		namedParameters.addValue("userId", userId);
		return jdbc.queryForList(query, namedParameters, String.class);
	}
}