package ru.itis.services.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ru.itis.services.interfaces.TemplateDrawer;

import java.io.IOException;
import java.util.Map;

@Service
public class TemplateDrawerImpl implements TemplateDrawer {
    private final
    Configuration ftlConfiguration;

    public TemplateDrawerImpl(Configuration ftlConfiguration) {
        this.ftlConfiguration = ftlConfiguration;
    }

    @Override
    public String getPageAsString(String viewName, Map<String, Object> model) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            stringBuilder.append(FreeMarkerTemplateUtils
                    .processTemplateIntoString(ftlConfiguration.getTemplate(viewName + ".ftl"), model));
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
        return stringBuilder.toString();
    }
}
