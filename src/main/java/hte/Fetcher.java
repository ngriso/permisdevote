package hte;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("fetch")
@Produces(MediaType.APPLICATION_JSON)
public class Fetcher {

    @GET
    @Path("2012")
    public Election fetchElection() {
        String idElection2012 = "4f16fe2299c7a10001000012";
        String election2012URL = "http://voxe.org/api/v1/elections/4f16fe2299c7a10001000012";

        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(ContextResolverForObjectMapper.class);
        cc.getClasses().add(JacksonJsonProvider.class);
        cc.getFeatures().put("com.sun.jersey.api.json.POJOMappingFeature", true);
        Election election = Client.create(cc).resource(election2012URL).accept(MediaType.APPLICATION_JSON_TYPE).get(ResponseElection.class).response.election;

        

        return election;
    }
}
