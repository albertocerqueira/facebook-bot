package facebook.bot.app;

import java.util.LinkedList;
import java.util.List;

import facebook4j.Comment;
import facebook4j.FacebookException;
import facebook4j.Group;
import facebook4j.PagableList;
import facebook4j.Post;
import facebook4j.ResponseList;

public class TSearch extends Thread {

	private static LinkedList<String> words = new LinkedList<String>();
	private long time = 0;
	
	public TSearch(){}
	public TSearch(long time){
		this.time = time;
	}
	
	@Override
    public void run() {
        while (true) {
            try {
            	String word = getWord();
            	
            	if (words.size() > 0) {
            		searchGroups(word);
            		searchPosts(word);
            	} else {
            		// TODO: Inserir log...
            	}
                TGroup.sleep(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	public static void addWord(String word){
		words.add(word);
	}
	
	public static void removeWord(String word){
		words.remove(word);
	}
	
	public static void addWordAll(List<String> wordsList){
		words.addAll(wordsList);
	}
	
	public static LinkedList<String> getWords() {
		return words;
	}
	
	public static void setPostIds(LinkedList<String> words) {
		TSearch.words = words;
	}
	
	private String getWord() {
		int p = words.size() - 1;
		String word = words.get(p);
		words.remove(p);
		return word;
	}
	
	public void searchGroups(String word) {
		try {
			ResponseList<Group> groups = AppFacebook.getFacebook().searchGroups(word);
			for (Group group : groups) {
				TGroup.addGroupId(group.getId());
			}
		} catch (FacebookException e) {
			// TODO: Inserir log...
			e.printStackTrace();
		}
	}
	
	public void searchPosts(String word) {
		try {
			ResponseList<Post> posts = AppFacebook.getFacebook().searchPosts(word);
			for (Post post : posts) {
				String postId = post.getId();
				TUser.addUserId(post.getFrom().getId());// Mais um usuario para minerar informacao
				PagableList<Comment> comments = post.getComments();
				for (Comment comment : comments) {
					TUser.addUserId(comment.getFrom().getId());// Mais um usuario para minerar informacao
					FacebookComments.postLikeCount(postId, comment);
				}
			}
		} catch (FacebookException e) {
			// TODO: Inserir log...
			e.printStackTrace();
		}
	}
}