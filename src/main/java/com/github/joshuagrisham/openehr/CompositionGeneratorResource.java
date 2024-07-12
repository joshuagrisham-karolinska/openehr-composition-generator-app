package com.github.joshuagrisham.openehr;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.ehrbase.openehr.sdk.validation.CompositionValidator;
import org.ehrbase.openehr.sdk.validation.ConstraintViolation;
import org.ehrbase.openehr.sdk.webtemplate.model.WebTemplate;
import org.jboss.resteasy.reactive.RestForm;
import org.openehr.schemas.v1.OPERATIONALTEMPLATE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nedap.archie.rm.composition.Composition;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

@Path("/")
public class CompositionGeneratorResource {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index();
        public static native TemplateInstance template();
        public static native TemplateInstance webTemplate(String webTemplate);
        public static native TemplateInstance composition(CompositionConverter converter);
        public static native TemplateInstance compositionValidation();
        public static native TemplateInstance compositionValidationResults(List<ConstraintViolation> violations);
    }

    @GET
    public TemplateInstance getIndex() {
        return Templates.index();
    }

    @GET
    @Path("/template")
    public TemplateInstance getTemplate() {
        return Templates.template();
    }

    @POST
    @Path("/template")
    public TemplateInstance postTemplate(@RestForm String template) throws XmlException, JsonProcessingException {
        //OPERATIONALTEMPLATE opt = TemplateConverter.getOperationalTemplate(template);
        WebTemplate webTemplate = TemplateConverter.getWebTemplate(template);
        return Templates.webTemplate(TemplateConverter.toString(webTemplate));
    }

    @POST
    @Path("/composition/generate")
    public TemplateInstance postGenerateComposition(@RestForm String template) throws XmlException, JsonProcessingException {
        //OPERATIONALTEMPLATE opt = TemplateConverter.getOperationalTemplate(template);
        WebTemplate webTemplate = TemplateConverter.getWebTemplate(template);
        Composition composition = CompositionGenerator.generateExample(webTemplate);
        
        // TODO: clean up how all of this works (better setters/getters/maybe static instead?)
        CompositionConverter converter = new CompositionConverter(template);
        converter.setComposition(composition);

        return Templates.composition(converter);
    }

    @POST
    @Path("/composition")
    public TemplateInstance postComposition(@RestForm String template, @RestForm String composition, @RestForm String format) throws XmlException, JsonProcessingException {
        CompositionConverter converter = new CompositionConverter(template, composition, CompositionConverter.Format.valueOf(format));
        return Templates.composition(converter);
    }

    @GET
    @Path("/composition/validate")
    public TemplateInstance getCompositionValidation() {
        return Templates.compositionValidation();
    }

    @POST
    @Path("/composition/validate")
    public TemplateInstance postCompositionValidation(@RestForm String template, @RestForm String composition, @RestForm String format) throws XmlException {
        OPERATIONALTEMPLATE opt = TemplateConverter.getOperationalTemplate(template);
        CompositionConverter converter = new CompositionConverter(template, composition, CompositionConverter.Format.valueOf(format));
        CompositionValidator compositionValidator = new CompositionValidator();
        List<ConstraintViolation> violations = compositionValidator.validate(converter.getComposition(), opt);
        return Templates.compositionValidationResults(violations);
    }

}
