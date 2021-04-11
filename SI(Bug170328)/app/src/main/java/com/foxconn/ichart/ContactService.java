package com.foxconn.ichart;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;

/**
 *
 * @author user
 * 
 */
public class ContactService {

	public List<Contact> getContacts() {

		List<Contact> contacts = new ArrayList<Contact>();

		contacts.add(new Contact("Chrome", 33.59, "#cbab4f"));
		contacts.add(new Contact("Firefox", 22.85, "#76a871"));
		contacts.add(new Contact("Safari", 7.39, "#9f7961"));
		contacts.add(new Contact("Opera", 1.63, "#a56f8f"));
		contacts.add(new Contact("Other", 1.69, "#6f83a5"));
		return contacts;
	}

}
