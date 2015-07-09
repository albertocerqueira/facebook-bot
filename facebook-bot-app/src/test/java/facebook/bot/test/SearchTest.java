package facebook.bot.test;

import org.junit.Test;

import facebook.bot.app.AppFacebook;
import facebook4j.FacebookException;
import facebook4j.Group;
import facebook4j.ResponseList;

public class SearchTest {

	@Test
	public void search_groups_by_key_word() throws FacebookException {
		ResponseList<Group> groups = AppFacebook.getFacebook().searchGroups("palavra chave");
		for (Group group : groups) {
			System.out.println("Group: " + group.getName());
		}
	}
}