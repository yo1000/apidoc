package com.yo1000.apidoc.controller.api;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Created by yoichi.kikuchi on 2015/07/17.
 */
@RestController
@RequestMapping("mocks/{var1}")
public class MockResource {
    @RequestMapping("items/{var2}")
    public Object getItem(@PathVariable final String var1,
                          @PathVariable final String var2,
                          @RequestParam(required = true) final int param1,
                          @RequestParam(required = false) final int param2,
                          @RequestHeader(required = false) final boolean header1,
                          @RequestHeader(required = false) final boolean header2) {
        return new HashMap<String, Object>() {
            {
                this.put("var1", var1);
                this.put("var2", var2);
                this.put("param1", param1);
                this.put("param2", param2);
                this.put("header1", header1);
                this.put("header2", header2);
            }
        };
    }
}
