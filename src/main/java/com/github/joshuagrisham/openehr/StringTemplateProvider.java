package com.github.joshuagrisham.openehr;

import java.util.Optional;

import org.ehrbase.openehr.sdk.webtemplate.templateprovider.TemplateProvider;
import org.openehr.schemas.v1.OPERATIONALTEMPLATE;
import org.openehr.schemas.v1.TemplateDocument;

public class StringTemplateProvider implements TemplateProvider {
    @Override
    public Optional<OPERATIONALTEMPLATE> find(String templateXmlString) {
        try {
            return Optional.ofNullable(TemplateDocument.Factory.parse(templateXmlString.trim()).getTemplate());
        } catch (Exception e) {
            throw new RuntimeException("Exception when parsing the openEHR Template XML String", e);
        }
    }
}
