package com.github.joshuagrisham.openehr;


import org.ehrbase.openehr.sdk.serialisation.RMDataFormat;
import org.ehrbase.openehr.sdk.serialisation.flatencoding.FlatFormat;
import org.ehrbase.openehr.sdk.serialisation.flatencoding.FlatJasonProvider;
import org.ehrbase.openehr.sdk.serialisation.jsonencoding.CanonicalJson;
import org.ehrbase.openehr.sdk.serialisation.xmlencoding.CanonicalXML;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.nedap.archie.rm.composition.Composition;

public class CompositionConverter {

    public enum Format {
        JSON,
        FLAT_JSON,
        STRUCTURED_JSON,
        XML
    }

    private String templateXmlString;
    public String getTemplateXmlString() {
        return templateXmlString;
    }
    public void setTemplateXmlString(String templateXmlString) {
        this.templateXmlString = templateXmlString;
    }

    private Composition composition;
    public Composition getComposition() {
        return this.composition;
    }
    public void setComposition(Composition composition) {
        this.composition = composition;
    }

    private StringTemplateProvider stringTemplateProvider = new StringTemplateProvider();
    private FlatJasonProvider flatJasonProvider = new FlatJasonProvider(stringTemplateProvider);

    private CanonicalXML canonicalXML = new CanonicalXML();
    private CanonicalJson canonicalJson = new CanonicalJson();

    private RMDataFormat getSerializer(Format format) {
        switch (format) {
            case XML:
                return canonicalXML;
            case JSON:
                return canonicalJson;
            case FLAT_JSON:
                return flatJasonProvider.buildFlatJson(FlatFormat.SIM_SDT, templateXmlString);
            case STRUCTURED_JSON:
                return flatJasonProvider.buildFlatJson(FlatFormat.STRUCTURED, templateXmlString);
            default:
                throw new RuntimeException("Unsupported format type " + format.toString());
        }
    }

    public void setComposition(String compositionString, Format compositionStringFormat) {
        setComposition(getSerializer(compositionStringFormat).unmarshal(compositionString));
    }

    public CompositionConverter() {
    }

    public CompositionConverter(String templateXmlString) {
        setTemplateXmlString(templateXmlString);
    }

    public CompositionConverter(String templateXmlString, String compositionString, Format compositionStringFormat) {
        setTemplateXmlString(templateXmlString);
        setComposition(compositionString, compositionStringFormat);
    }

    public String getCompositionAsString(Format format) {
        return this.getSerializer(format).marshal(this.composition);
    }

    public String getCompositionAsString(String formatName) {
        return getCompositionAsString(Format.valueOf(formatName));
    }

    public String prettyPrintCompositionXml() throws JsonProcessingException {
        return PrettyPrintXml.convert(getCompositionAsString(Format.XML));
    }

}
