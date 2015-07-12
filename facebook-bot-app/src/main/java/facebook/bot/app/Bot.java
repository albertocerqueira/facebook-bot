package facebook.bot.app;

import java.util.List;

import org.apache.cassandra.thrift.ColumnOrSuperColumn;

import facebook.bot.app.objects.PostImpl;

public class Bot {

	private void test() {
		List<ColumnOrSuperColumn> cscs = Cassandra.getPostsPages("post-gorup");
		for (ColumnOrSuperColumn csc : cscs) {
			PostImpl post = new PostImpl();
			
			String[] superColumn = Cassandra.createString(csc.super_column.getName()).split("-");
			String postId = superColumn[0];
			String userId = superColumn[1];
			String userName = superColumn[2];
			
			String[] column = Cassandra.createString(csc.column.getName()).split("-");
			String commentsCount = column[0];
			String likeCount = column[1];
			
			String message = Cassandra.createString(csc.column.getValue());
		}
		
		String rowKey = "post-gorup";
	}
}