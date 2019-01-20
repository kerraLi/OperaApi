package com.ywxt.Controller;

import org.springframework.http.HttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;




@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class RoleController extends CommonController {
    public void full(HttpRequest request){}
}