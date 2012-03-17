package hte;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("fetch")
public class Fetcher {

    @GET
    @Path("2012")
    public Election fetchElection(){
        String idElection2012 = "4f16fe2299c7a10001000012";
        String election2012URL = "http://voxe.org/api/v1/elections/4f16fe2299c7a10001000012";

        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(JacksonJsonProvider.class);
        cc.getFeatures().put("com.sun.jersey.api.json.POJOMappingFeature", true);
        return Client.create(cc).resource(election2012URL).get(ResponseElection.class).response;

        
    }
}
