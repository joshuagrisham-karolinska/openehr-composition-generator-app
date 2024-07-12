package com.github.joshuagrisham.openehr;

import java.time.OffsetDateTime;

import org.ehrbase.openehr.sdk.examplegenerator.ExampleGeneratorConfig;
import org.ehrbase.openehr.sdk.examplegenerator.ExampleGeneratorToCompositionWalker;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Language;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Setting;
import org.ehrbase.openehr.sdk.generator.commons.shareddefinition.Territory;
import org.ehrbase.openehr.sdk.serialisation.walker.FlatHelper;
import org.ehrbase.openehr.sdk.serialisation.walker.defaultvalues.DefaultValuePath;
import org.ehrbase.openehr.sdk.serialisation.walker.defaultvalues.DefaultValues;
import org.ehrbase.openehr.sdk.webtemplate.model.WebTemplate;
import org.ehrbase.openehr.sdk.webtemplate.webtemplateskeletonbuilder.WebTemplateSkeletonBuilder;

import com.nedap.archie.rm.composition.Composition;

public class CompositionGenerator {

    public static Composition generateExample(WebTemplate webTemplate) {

        Composition composition = WebTemplateSkeletonBuilder.build(webTemplate, false);

        ExampleGeneratorConfig object = new ExampleGeneratorConfig();

        DefaultValues defaultValues = new DefaultValues();
        defaultValues.addDefaultValue(DefaultValuePath.TIME, OffsetDateTime.now());
        defaultValues.addDefaultValue(
                DefaultValuePath.LANGUAGE,
                FlatHelper.findEnumValueOrThrow(webTemplate.getDefaultLanguage(), Language.class));
        defaultValues.addDefaultValue(DefaultValuePath.TERRITORY, Territory.DE);
        defaultValues.addDefaultValue(DefaultValuePath.SETTING, Setting.OTHER_CARE);
        defaultValues.addDefaultValue(DefaultValuePath.COMPOSER_NAME, "Max Mustermann");

        ExampleGeneratorToCompositionWalker walker = new ExampleGeneratorToCompositionWalker();
        walker.walk(composition, object, webTemplate, defaultValues, webTemplate.getTemplateId());

        composition.setTerritory(Territory.DE.toCodePhrase());
        return composition;        

    }

}
