import java.io.Serializable;


@SuppressWarnings("serial")
public class ChatMessage implements Serializable {

	int ttl;
	String source;
	String destination;
	private String content;
	ChatMessageType type;
	
	public ChatMessage(String source,String destination,String content){
		this.source = source;
		this.destination = destination;
		this.content = content;
		this.ttl = 2;
		type =ChatMessageType.NORMAL;
	}
	
	public ChatMessage(String source,String destination,ChatMessageType type){
		this.source = source;
		this.destination = destination;
		this.ttl = 2;
		this.type = type;
	}
	
	
	public ChatMessage(String source,String destination,String content,ChatMessageType type){
		this.source = source;
		this.destination = destination;
		this.ttl = 2;
		this.type = type;
		this.content = content;
	}
	
	public String getContent(){
		return content;
	}
	
	public String toString(){
		return source + " " + destination + " " + content;
	}
}
