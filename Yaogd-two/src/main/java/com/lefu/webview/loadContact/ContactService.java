package com.lefu.webview.loadContact;

import java.util.ArrayList;
import java.util.List;

public class ContactService {

	public List<Contact> getContacts(){
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(new Contact(78, "张飞", "1384949494"));
		contacts.add(new Contact(12, "李静", "194505555"));
		contacts.add(new Contact(89, "赵薇", "1785959595"));
		return contacts;
	}
}
