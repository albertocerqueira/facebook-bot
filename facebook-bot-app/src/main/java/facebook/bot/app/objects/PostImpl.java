package facebook.bot.app.objects;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import facebook4j.Application;
import facebook4j.Comment;
import facebook4j.IdNameEntity;
import facebook4j.PagableList;
import facebook4j.Place;
import facebook4j.Post;
import facebook4j.Privacy;
import facebook4j.Tag;

public class PostImpl implements Post {

	private String id;
	private String message;
	private String type;
	private IdNameEntity from;
	private PagableList<IdNameEntity> likes;
	private PagableList<Comment> comments;
	private Integer rankingPosition;// create for bot
	private Integer popularity;// create for bot
	
	@Override
	public Metadata getMetadata() {
		return null;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public IdNameEntity getFrom() {
		return this.from;
	}

	public void setFrom(IdNameEntity from) {
		this.from = from;
	}

	@Override
	public List<IdNameEntity> getTo() {
		return null;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public List<Tag> getMessageTags() {
		return null;
	}

	@Override
	public URL getPicture() {
		return null;
	}

	@Override
	public URL getLink() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getCaption() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public URL getSource() {
		return null;
	}

	@Override
	public List<Property> getProperties() {
		return null;
	}

	@Override
	public String getIcon() {
		return null;
	}

	@Override
	public List<Action> getActions() {
		return null;
	}

	@Override
	public Privacy getPrivacy() {
		return null;
	}

	@Override
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public PagableList<IdNameEntity> getLikes() {
		return this.likes;
	}

	public void setLikes(PagableList<IdNameEntity> likes) {
		this.likes = likes;
	}

	@Override
	public Place getPlace() {
		return null;
	}

	@Override
	public String getStory() {
		return null;
	}

	@Override
	public Map<String, Tag[]> getStoryTags() {
		return null;
	}

	@Override
	public List<IdNameEntity> getWithTags() {
		return null;
	}

	@Override
	public PagableList<Comment> getComments() {
		return this.comments;
	}

	public void setComments(PagableList<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public Long getObjectId() {
		return null;
	}

	@Override
	public Application getApplication() {
		return null;
	}

	@Override
	public Date getCreatedTime() {
		return null;
	}

	@Override
	public Date getUpdatedTime() {
		return null;
	}

	public Integer getRankingPosition() {
		return rankingPosition;
	}

	public void setRankingPosition(Integer rankingPosition) {
		this.rankingPosition = rankingPosition;
	}

	public Integer getPopularity() {
		return popularity;
	}

	public void setPopularity(Integer popularity) {
		this.popularity = popularity;
	}

	@Override
	public String toString() {
		return "PostImpl [id=" + id + ", message=" + message + ", type=" + type + ", from=" + (from == null ? "null" : from.toString()) + ", likes=" + likes + ", comments=" + comments + ", rankingPosition=" + rankingPosition + ", popularity=" + popularity + "]";
	}
}