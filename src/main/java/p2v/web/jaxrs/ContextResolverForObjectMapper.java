package p2v.web.jaxrs;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.introspect.VisibilityChecker;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.text.SimpleDateFormat;

import static org.codehaus.jackson.annotate.JsonAutoDetect.Visibility.ANY;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.ALWAYS;

@Provider
public class ContextResolverForObjectMapper implements ContextResolver<ObjectMapper> {

    final ObjectMapper objectMapper;

    public ContextResolverForObjectMapper() {
        objectMapper = createCustomMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }

    ObjectMapper createCustomMapper() {
        ObjectMapper mapper = new ObjectMapper();

        SerializationConfig serializationConfig = mapper.getSerializationConfig()
                .with(Feature.INDENT_OUTPUT)
                .without(Feature.AUTO_DETECT_GETTERS)
//                .without(Feature.USE_ANNOTATIONS)
                .without(Feature.WRITE_DATES_AS_TIMESTAMPS)
                .without(Feature.FAIL_ON_EMPTY_BEANS)
                .withDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .withSerializationInclusion(ALWAYS)
                .withVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(ANY));
        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig()
                .without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES)
                .withVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(ANY));

        mapper.setSerializationConfig(serializationConfig);
        mapper.setDeserializationConfig(deserializationConfig);

        return mapper;
    }
}
