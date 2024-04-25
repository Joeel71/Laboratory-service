import flogo.boot.utils.api.ApiConnection;
import flogo.boot.utils.api.FlogoApiConnection;
import org.junit.Test;

import java.util.HashMap;

public class ApiConnectionTest {

    private static final ApiConnection API_CONNECTION = new FlogoApiConnection();

    @Test
    public void should_test_the_stat_method() {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("laboratory", "TestLaboratory");
        queryParams.put("experiment", "TestExperiment");
        API_CONNECTION.getStat("best-experiment", queryParams);
    }
}

