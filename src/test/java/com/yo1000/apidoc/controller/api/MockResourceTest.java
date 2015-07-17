package com.yo1000.apidoc.controller.api;

import com.yo1000.apidoc.TestContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Created by yoichi.kikuchi on 2015/07/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestContext.class})
@WebIntegrationTest({"server.port=55555"})
public class MockResourceTest {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void getItemTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("header1", "true");
        headers.set("header2", "false");

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        this.getRestTemplate().exchange(
                "http://localhost:55555/mocks/{var1}/items/{var2}?param1={param1}&param2={param2}",
                HttpMethod.GET, entity, String.class,
                new HashMap<String, Object>() {
                    {
                        this.put("var1", "aaa");
                        this.put("var2", "bbb");
                        this.put("param1", 100);
                        this.put("param2", 200);
                    }
                }
        );
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
