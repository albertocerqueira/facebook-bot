package facebook.bot.app.objects;

import facebook4j.IdNameEntity;

public class IdNameEntityImpl implements IdNameEntity {

	private String id;
	private String name;
	
	@Override
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}