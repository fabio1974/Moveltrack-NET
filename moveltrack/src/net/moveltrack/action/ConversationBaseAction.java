package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.security.LoginBean;

@Named(value="conversation")
@ConversationScoped
public class ConversationBaseAction extends BaseAction implements Serializable{

	private static final long serialVersionUID = -2443158376437524108L;
	
	@Inject
	private Conversation conversation;
	
	@Inject 
	protected LoginBean loginBean;

	
	public ConversationBaseAction() {
	}
	
	public String novaConversa(String name){
		endConversation();
		initConversation((new Date()).getTime()+"");
		return name;
	}
	
	public void initConversation(String name){
		try{
			if (conversation != null && conversation.isTransient()) 
					conversation.begin(name);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
	}


	public void endConversation(){
		if(conversation != null && !conversation.isTransient()){
			String id = conversation.getId();
			System.out.println("Conversation "+id+" finishing...");
			conversation.end();
			System.out.println("Conversation "+id+" finished...");
		}
	}
	
	@Override
	public void operacaoSucesso() {
		super.operacaoSucesso();
	}
	

	@Override
	public void operacaoSucesso(Boolean readonly) {
		super.operacaoSucesso(readonly);
	}
	
	

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

}
