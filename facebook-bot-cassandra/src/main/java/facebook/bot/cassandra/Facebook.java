package facebook.bot.cassandra;

import static facebook.bot.cassandra.Constants.COLUMN_FAMILY_FACEBOOK_COMMENT_LIKE_COUNT;
import static facebook.bot.cassandra.Constants.UTF8;

import java.io.UnsupportedEncodingException;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

public class Facebook {

	public static void insertCommentLikeCount(String commentId, String postId, String userId, String likeCount) {
		try {
			Idao dao = new Dao();
			Clock clock = new Clock(System.nanoTime());
			Column column = new Column();
			column.setName(userId.getBytes(UTF8));
			column.setValue(likeCount.getBytes(UTF8));
			column.setTimestamp(clock.timestamp);
			dao.insertSuperColumn(COLUMN_FAMILY_FACEBOOK_COMMENT_LIKE_COUNT, commentId, postId, column);
		} catch (InvalidRequestException e) {
			e.printStackTrace();
			//Dificilmente ocorrera esse erro.
			//Descricao: Essa exceção e lancada quando um cliente JAXR tenta chamar um método de API que nao e valido, por algum motivo.
			//String message = GerenciadorMensagem.getMessage("system.db.cassandra.exception", PeopleDAO.class.getName(), "insertColumn", e.getClass().getName(), e.getMessage());
			//throw new CriticalUserException(PeopleFinder.class, message);
		} catch (UnavailableException e) {
			e.printStackTrace();
			//Dificilmente ocorrera esse erro.
			//Descricao: Lancada pelo servlet container quando o servlet nao esta ativo.
			//String message = GerenciadorMensagem.getMessage("system.db.cassandra.exception", PeopleDAO.class.getName(), "insertColumn", e.getClass().getName(), e.getMessage());
			//throw new CriticalUserException(PeopleFinder.class, message);
		} catch (TimedOutException e) {
			e.printStackTrace();
			//Dificilmente ocorrera esse erro.
			//Descricao: Excecao lancada quando o metodo excede o bloqueio de tempo da acao.
			//String message = GerenciadorMensagem.getMessage("system.db.cassandra.time.out.exception");
			//throw new CriticalUserException(PeopleFinder.class, message);
		} catch (TException e) {
			e.printStackTrace();
			//Descricao: Excecao generica do Thrift.
			//String message = GerenciadorMensagem.getMessage("system.db.cassandra.exception.thrift");
			//throw new CriticalUserException(PeopleFinder.class, message);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}