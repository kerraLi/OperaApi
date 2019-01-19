package com.ywxt.Controller;

import org.springframework.security.access.prepost.PreAuthorize;



@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class RoleController {}