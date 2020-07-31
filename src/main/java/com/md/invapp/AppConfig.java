/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author Martin.Degiorgio
*/
@Configuration
@PropertySource("classpath:conf/invapp.properties")
public class AppConfig {
    
    @Value("${COMPANY_NAME}")
    private String compName;

    @Value("${COMPANY_ADDRESS_LINE_1}")
    private String addressLine1;

    @Value("${COMPANY_ADDRESS_LINE_2}")
    private String addressLine2;

    @Value("${COMPANY_ADDRESS_LINE_3}")
    private String addressLine3;

    @Value("${COMPANY_VAT_NUMBER}")
    private String vatNumber;

    @Value("${COMPANY_EMAIL}")
    private String emailAddress;
    
    @Value("${COMPANY_MOBILE")
    private String mobileNumber;
    
    @Value("${SELLING_VAT_RATE}")
    private double vatRate;
    
    @Value("${REPORTS_PATH}")
    private String reportsPath;

    @Value("${REPORTS_EMAIL}")
    private String reportsEmail;
    
    @Bean
    public InvAppConfig getInvAppConfig(){
        return new InvAppConfig(compName, addressLine1, addressLine2, addressLine3, vatNumber, emailAddress, mobileNumber, reportsPath, reportsEmail, vatRate);
    }
    
}
