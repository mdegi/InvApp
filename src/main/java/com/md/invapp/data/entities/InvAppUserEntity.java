/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp.data.entities;

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
@Table(name = "invappuser", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ID")        
})
public class InvAppUserEntity implements Serializable {
    
    private static final long serialVersionUID = -1798070786993154676L;
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private int id;
    
    @Column(name = "userCode", unique = true, nullable = false, length = 10)
    private String userCode;
    
    @Column(name = "userPass", unique = false, nullable = false, length = 25)
    private String userPass;
            
    @Column(name = "userGrp", columnDefinition = "enum('ADMIN','GRP1','GRP2')", nullable = false)
    private String userGrp;

    @Column(name = "readOnly", unique = false, nullable = false)
    private boolean readOnly;
    
    public final static int NEW_RECORD = 0;
    
    public final static int CANCELLED_RECORD = -1;
                    
}
