package facebook.bot.app;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebook4j.FacebookException;
import facebook4j.Group;
import facebook4j.Post;
import facebook4j.ResponseList;

public class TSearch extends Thread {

	final static Logger logger = LoggerFactory.getLogger(TSearch.class);
	private static LinkedList<String> words = new LinkedList<String>();
	private long time = 0;
	public static String type = "post-search";
	
	public TSearch(){}
	public TSearch(long time){
		this.time = time;
	}
	
	@Override
    public void run() {
        while (true) {
            try {
            	if (words.size() > 0) {
            		String word = getWord();
            		searchGroups(word);
            		//searchPosts(word);// TODO: check problem (#11 - Post search has been deprecated)
            	} else {
            		logger.info("there no are words for verification");
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
			ResponseList<Group> groups = Facebook.getFacebook().searchGroups(word);
			for (Group group : groups) {
				TGroup.addGroupId(group.getId());
			}
		} catch (FacebookException e) {
			logger.error("[Error Code: " + e.getErrorCode() + "] - [Error: " + e.getErrorMessage() + "]", e);
		}
	}
	
	public void searchPosts(String word) {
		try {
			ResponseList<Post> posts = Facebook.getFacebook().searchPosts(word);
			for (Post post : posts) {
				Cassandra.insertPost(post, type);
				/*
				String postId = post.getId();
				TUser.addUserId(post.getFrom().getId());// Mais um usuario para minerar informacao
				PagableList<Comment> comments = post.getComments();
				for (Comment comment : comments) {
					TUser.addUserId(comment.getFrom().getId());// Mais um usuario para minerar informacao
					//FacebookComments.postLikeCount(postId, comment);
				}
				*/
			}
		} catch (FacebookException e) {
			logger.error("[Error Code: " + e.getErrorCode() + "] - [Error: " + e.getErrorMessage() + "]", e);
		}
	}
}