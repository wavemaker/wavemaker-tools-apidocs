/**
 * Copyright © 2013 - 2016 WaveMaker, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wavemaker.tools.apidocs.tools.spring;
// Generated 19 Nov, 2014 12:15:13 PM by Hibernate Tools 4.3.1


import java.util.Date;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * Vacation generated by hbm2java
 */
@Entity
@Table(name="VACATION"
    ,schema="PUBLIC"
    ,catalog="PUBLIC"
)
public class Vacation  implements java.io.Serializable {


     private Integer id;
     private Employee employee;
     private Date startdate;
     private Date enddate;
     private Integer tenantid;

    public Vacation() {
    }

	
    public Vacation(Employee employee) {
        this.employee = employee;
    }
    public Vacation(Employee employee, Date startdate, Date enddate, Integer tenantid) {
       this.employee = employee;
       this.startdate = startdate;
       this.enddate = enddate;
       this.tenantid = tenantid;
    }

     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="ID", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="EMPID", nullable=false)
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="STARTDATE", length=10)
    public Date getStartdate() {
        return this.startdate;
    }
    
    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="ENDDATE", length=10)
    public Date getEnddate() {
        return this.enddate;
    }
    
    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    
    @Column(name="TENANTID")
    public Integer getTenantid() {
        return this.tenantid;
    }
    
    public void setTenantid(Integer tenantid) {
        this.tenantid = tenantid;
    }




}
