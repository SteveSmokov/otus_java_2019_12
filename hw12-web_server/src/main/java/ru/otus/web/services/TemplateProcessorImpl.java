package ru.otus.web.services;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.util.Map;

public class TemplateProcessorImpl implements TemplateProcessor {
    private final ClassLoaderTemplateResolver templateResolver;

    public TemplateProcessorImpl(String templatesDir) {
        this.templateResolver = new ClassLoaderTemplateResolver();
        this.templateResolver.setPrefix("/templates/");
        this.templateResolver.setSuffix(".html");
        this.templateResolver.setCharacterEncoding("UTF-8");
        this.templateResolver.setTemplateMode(TemplateMode.HTML);
    }

    @Override
    public String getPage(String fileName, Map<String, Object> data) throws IOException {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context ct = new Context();
        ct.setVariables(data);
        return templateEngine.process(fileName, ct);
    }
}
