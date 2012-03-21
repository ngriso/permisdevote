package p2v;

import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Handler;
import java.util.logging.LogManager;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;

public class MainTest {

    @Test
    public void expect_list_themes_is_not_empty() {
        expect().body("size()", greaterThan(0)).get(URLS.BASE + "/themes");
    }

    @Test
    public void expect_candidicies_are_correct() {
        expect().body(
                // check list of candidacies is not empty
                "size()", greaterThan(0),
                // check each candidacies has only 1 candidates
                "findAll { it.nbCandidates != 1}", empty()
        ).get(URLS.BASE + "/candidacies");
    }

    @Test
    public void expect404_when_ask_next_questions_with_missing_args() {
        expect().statusCode(404).get(URLS.BASE + "/questions/next");
    }

    @Test
    public void expect_404_when_ask_next_questions_with_unknow_candiday() {
        expect().statusCode(404).get(URLS.BASE + "/questions/next?candidacyId=whatever");
    }

    @Test
    public void expect_404_when_ask_next_questions_with_unknow_theme() {
        expect().statusCode(404).get(URLS.BASE + "/questions/next?tagId=whatever");
    }

    @Test
    public void test_next_question_for_theme() {
        String themeId = get(URLS.BASE + "/themes").jsonPath().get("[0].id");
        expect().body("text", notNullValue()).get(URLS.BASE + "/questions/next?tagId=" + themeId);
    }

    @Test
    public void test_next_question_for_candidacy() {
        String candidacyId = get(URLS.BASE + "/candidacies").jsonPath().get("[0].id");
        expect().body("text", notNullValue()).get(URLS.BASE + "/questions/next?candidacyId=" + candidacyId);
    }

    @Test
    public void answer() {
        String candidacyId = get(URLS.BASE + "/candidacies").jsonPath().get("[0].id");
        Integer questionId = get(URLS.BASE + "/questions/next?candidacyId=" + candidacyId).jsonPath().getInt("id");
        String response = expect().statusCode(200).get(URLS.BASE + "/questions/{questionId}/answer?answer={answer}&username={username}", questionId, "true", "nicolas").asString();
        assertTrue(response.equalsIgnoreCase(Boolean.TRUE.toString()) || response.equalsIgnoreCase(Boolean.FALSE.toString()));

        get(URLS.BASE + "/voters/nicolas/stats");
        get(URLS.BASE + "/stats");
    }

    @Test
    public void checkVoterOperations() {
/*
        given().body("{\"username\":\"nicolas\"}").contentType(ContentType.JSON)
                .expect()
                    .statusCode(200)
                .body(
                        "id", notNullValue(),
                        "username", equalTo("nicolas")
                )
                .post(URLS.BASE + "/voters");
*/
        expect().get(URLS.BASE + "/voters/nicolas/stats");
    }

    @ClassRule
    public static final JettyServerRule jetty = new JettyServerRule(18080, "src/main/webapp");

    static {
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        SLF4JBridgeHandler.install();
    }

    public static interface URLS {
        final String BASE = "http://localhost:" + jetty.getPort() + "/api";
    }
}
