/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md.invapp.data.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "item", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ITEMCODE")        
})
public class ItemEntity implements InvAppEntitiy, Serializable {
    
    private static final long serialVersionUID = -1798070786993154676L;
    
    public static final String ITEM_CODE = "itemCode";
    public static final String ITEM_DESCRIPTION = "description";
            
    @Id
    @Column(name = ITEM_CODE, unique = true, nullable = false, length = 20)
    private String itemCode;

    @Column(name = ITEM_DESCRIPTION, unique = false, nullable = true, length = 60)
    private String description;
    
    @Column(name = "barCode", unique = true, nullable = true)
    private String barCode;
    
    @Column(name = "comments", unique = false, nullable = true, columnDefinition="TEXT")
    private String comments;
    
    @Column(name = "categoryId", unique = false, nullable = false)
    private int categoryId;

    @Column(name = "groupId", unique = false, nullable = false)
    private int groupId;

    @Column(name = "costPrice", unique = false, nullable = false, columnDefinition = "decimal", precision = 6, scale = 2)
    private double costPrice;

    @Column(name = "sellPrice_1", unique = false, nullable = false, columnDefinition = "decimal", precision = 6, scale = 2)
    private double sellPrice_1;

    @Column(name = "sellPrice_2", unique = false, nullable = false, columnDefinition = "decimal", precision = 6, scale = 2)
    private double sellPrice_2;

    @Column(name = "qtyInStock", unique = false, nullable = false)
    private int qtyInStock;
        
    public final static int NEW_RECORD = 0;
    
    public final static int CANCELLED_RECORD = -1;
                        
}
