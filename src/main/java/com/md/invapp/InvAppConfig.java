/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Martin.Degiorgio
 */

@Getter
@Setter
@AllArgsConstructor
public class InvAppConfig {

    private String companyName, addressLine1, addressLine2, addressLine3, vatNumber, emailAddress, mobileNumber, reportsPath, reportsEmail;
    private double vatRate;

}
