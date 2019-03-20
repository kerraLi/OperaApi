package com.ywxt.Service.System;

import com.ywxt.Domain.System.Ignore;

import java.util.List;

public interface IgnoreService {

    public void setMarked(String status, Ignore ignore);

    public void setMarked(String status, List<Ignore> list);

    public String[] getMarkedValues(String domain);

}
