package assignment1.ridengo;

import junit.framework.TestCase;

import java.security.spec.ECField;
import java.util.ArrayList;

/**
 * Created by Rui on 2016-11-12.
 */
public class UserListTest extends TestCase {

    public void testContainsByUser() throws Exception {
        UserList userList = new UserList();
        User newUser = new User("test", "", "");

        assertFalse(userList.contains(newUser));

        userList.addUser(newUser);
        assertTrue(userList.contains(newUser));
    }


    public void testContainByUserName() throws Exception {
        UserList userList = new UserList();
        User newUser = new User("test", "", "");

        assertFalse(userList.contains(newUser.getUsername()));

        userList.addUser(newUser);
        assertTrue(userList.contains(newUser.getUsername()));
    }


    public void testGetUsers() throws Exception {
        UserList oldUserList = new UserList();
        oldUserList.addUser(new User("test1", "",""));
        oldUserList.addUser(new User("test2", "",""));
        oldUserList.addUser(new User("test3", "",""));

        ArrayList<User> newList = oldUserList.getUsers();

        User newUser1 = new User("test1", "", "");
        User newUser2 = new User("test2", "", "");
        User newUser3 = new User("test3", "", "");

        assertEquals(newList.get(0).getUsername(), newUser1.getUsername());
        assertEquals(newList.get(1).getUsername(), newUser2.getUsername());
        assertEquals(newList.get(2).getUsername(), newUser3.getUsername());
    }


    public void testAddUsers() throws Exception {
        UserList userList= new UserList();

        User newUser = new User("test1", "", "");
        assertFalse(userList.contains(newUser));

        userList.addUser(newUser);
        assertTrue(userList.contains(newUser));
        try {
            userList.addUser(newUser);
            fail("Should not reach here");
        }catch (Exception e){
            assertTrue(true);
        }
    }


    public void testGetUserByUsername() throws Exception {
        UserList userList = new UserList();

        userList.addUser(new User("test1", "test1@example.com", "1234567891"));
        userList.addUser(new User("test2", "test2@example.com", "1234567892"));
        userList.addUser(new User("test3", "test3@example.com", "1234567893"));

        User user = userList.getUserByUsername("test1");

        assertEquals(user.getUsername(), "test1");
        assertEquals(user.getEmail(), "test1@example.com");
        assertEquals(user.getPhoneNum(), "1234567891");

    }


    public void testClear() throws Exception {
        UserList userList = new UserList();

        userList.addUser(new User("test1", "test1@example.com", "1234567891"));
        userList.addUser(new User("test2", "test2@example.com", "1234567892"));
        userList.addUser(new User("test3", "test3@example.com", "1234567893"));

        assertFalse(userList.getUsers().size() == 0);

        userList.clear();

        assertTrue(userList.getUsers().size() == 0);

    }
}