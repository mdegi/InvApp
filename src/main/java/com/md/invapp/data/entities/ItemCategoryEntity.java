/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp.data.entities;

import com.md.invapp.MaintenanceTableRecord;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author user
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "itemCategory", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ID")        
})
public class ItemCategoryEntity extends MaintenanceTableRecord implements InvAppEntitiy, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private int id;
    
    @Column(name = "description", unique = true, nullable = false, length = 60)
    private String description;
    
}
