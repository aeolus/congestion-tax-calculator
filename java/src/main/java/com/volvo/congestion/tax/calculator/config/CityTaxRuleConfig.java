package com.volvo.congestion.tax.calculator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvo.congestion.tax.calculator.domain.CityTaxRule;
import com.volvo.congestion.tax.calculator.dto.CityTaxRuleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CityTaxRuleConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CityTaxRuleConfig.class);
    private static final String RESOURCE_PATH = "classpath*:/taxRules/*.json";

    @Autowired
    public ObjectMapper mapper;

    @Bean
    public List<CityTaxRule> rules() {
        List<CityTaxRule> rules = new ArrayList<>();

        try {
            ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            Resource[] ruleFileLocations = patternResolver.getResources(RESOURCE_PATH);
            for (Resource ruleFileLocation : ruleFileLocations) {
                File file = ruleFileLocation.getFile();
                CityTaxRuleDto dto = mapper.readValue(file, CityTaxRuleDto.class);
                CityTaxRule rule = CityTaxRule.fromDto(dto);
                rules.add(rule);
            }
        } catch (IOException e) {
            String errorMessage = "Failed to load city tax rules";
            LOG.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }

        if (rules.size() == 0) {
            String errorMessage = "No city tax rules found, fail and exit";
            LOG.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }

        LOG.info("Successfully loaded (" + rules.size() + ") city tax rules.");

        return rules;
    }
}
