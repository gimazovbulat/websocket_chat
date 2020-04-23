package ru.itis.services.interfaces;

import java.util.Map;

public interface TemplateDrawer {
    String getPageAsString(String viewName, Map<String, Object> map);
}
