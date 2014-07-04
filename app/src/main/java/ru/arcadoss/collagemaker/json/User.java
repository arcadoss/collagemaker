package ru.arcadoss.collagemaker.json;

import java.io.Serializable;

/**
 * Created by arcturus at 02.07.14
 *
 "username": "jack",
 "first_name": "Jack",
 "profile_picture": "http://distillery.s3.amazonaws.com/profiles/profile_66_75sq.jpg",
 "id": "66",
 "last_name": "Dorsey"
 */
public class User implements Serializable {
	public String username;
	public String id;
	public String profile_picture;

	@Override
	public String toString() {
		return String.format("User{username='%s', id='%s', pic='%s'}", username, id, profile_picture);
	}
}
