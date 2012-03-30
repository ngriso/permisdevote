package p2v.web.signin;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Reference;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SignIn {
	
	@Inject
	private Facebook facebook;
	
//	@Path("facebook")
//    @GET
	public List<Reference> facebook() {
		List<Reference> result = facebook.friendOperations().getFriends();
		return result;
	}
	
}
