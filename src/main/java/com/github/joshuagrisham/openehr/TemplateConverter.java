package com.github.joshuagrisham.openehr;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
//import org.ehrbase.openehr.sdk.serialisation.flatencoding.FlatFormat;
//import org.ehrbase.openehr.sdk.serialisation.flatencoding.FlatJasonProvider;
import org.ehrbase.openehr.sdk.webtemplate.model.WebTemplate;
import org.ehrbase.openehr.sdk.webtemplate.parser.OPTParser;
import org.openehr.schemas.v1.OPERATIONALTEMPLATE;
import org.openehr.schemas.v1.TemplateDocument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TemplateConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    //private static final StringTemplateProvider stringTemplateProvider = new StringTemplateProvider();
    //private static final FlatJasonProvider flatJasonProvider = new FlatJasonProvider(stringTemplateProvider);

    private TemplateConverter() {}

    public static OPERATIONALTEMPLATE getOperationalTemplate(String operationalTemplateString) throws XmlException {
        return TemplateDocument.Factory.parse(operationalTemplateString.trim()).getTemplate();
    }

    /*
    public static String toString(OPERATIONALTEMPLATE operationalTemplate) throws JsonProcessingException {
        return operationalTemplate.xmlText(new XmlOptions().setSavePrettyPrint());
    }
    */

    public static WebTemplate getWebTemplate(String operationalTemplateString) throws XmlException {
        return getWebTemplate(getOperationalTemplate(operationalTemplateString));
    }

    public static WebTemplate getWebTemplate(OPERATIONALTEMPLATE operationalTemplate) {
        return new OPTParser(operationalTemplate).parse();
    }

    public static String toString(WebTemplate webTemplate) throws JsonProcessingException {
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(webTemplate);
    }

}
