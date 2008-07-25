/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.studio.dsmlv2.request;


import org.apache.directory.shared.ldap.codec.modifyDn.ModifyDNRequest;
import org.apache.directory.shared.ldap.name.LdapDN;
import org.apache.directory.shared.ldap.name.Rdn;
import org.dom4j.Element;


/**
 * DSML Decorator for ModifyDNRequest
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ModifyDNRequestDsml extends AbstractRequestDsml
{
    /**
     * Creates a new instance of ModifyDNRequestDsml.
     */
    public ModifyDNRequestDsml()
    {
        super( new ModifyDNRequest() );
    }


    /**
     * Creates a new instance of ModifyDNRequestDsml.
     *
     * @param ldapMessage
     *      the message to decorate
     */
    public ModifyDNRequestDsml( ModifyDNRequest ldapMessage )
    {
        super( ldapMessage );
    }


    /**
     * {@inheritDoc}
     */
    public int getMessageType()
    {
        return instance.getMessageType();
    }


    /**
     * {@inheritDoc}
     */
    public Element toDsml( Element root )
    {
        Element element = super.toDsml( root );

        ModifyDNRequest request = ( ModifyDNRequest ) instance;

        // DN
        if ( request.getEntry() != null )
        {
            element.addAttribute( "dn", request.getEntry().toString() );
        }

        // NewRDN
        if ( request.getNewRDN() != null )
        {
            element.addAttribute( "newrdn", request.getNewRDN().toString() );
        }

        // DeleteOldRDN
        element.addAttribute( "deleteoldrdn", ( request.isDeleteOldRDN() ? "true" : "false" ) );

        // NewSuperior
        if ( request.getNewRDN() != null )
        {
            element.addAttribute( "newSuperior", request.getNewSuperior().toString() );
        }

        return element;
    }


    /**
     * Get the modification's DN
     * 
     * @return Returns the entry.
     */
    public LdapDN getEntry()
    {
        return ( ( ModifyDNRequest ) instance ).getEntry();
    }


    /**
     * Set the modification DN.
     * 
     * @param entry The entry to set.
     */
    public void setEntry( LdapDN entry )
    {
        ( ( ModifyDNRequest ) instance ).setEntry( entry );
    }


    /**
     * Tells if the old RDN is to be deleted
     * 
     * @return Returns the deleteOldRDN.
     */
    public boolean isDeleteOldRDN()
    {
        return ( ( ModifyDNRequest ) instance ).isDeleteOldRDN();
    }


    /**
     * Set the flag to delete the old RDN
     * 
     * @param deleteOldRDN The deleteOldRDN to set.
     */
    public void setDeleteOldRDN( boolean deleteOldRDN )
    {
        ( ( ModifyDNRequest ) instance ).setDeleteOldRDN( deleteOldRDN );
    }


    /**
     * Get the new RDN
     * 
     * @return Returns the newRDN.
     */
    public Rdn getNewRDN()
    {
        return ( ( ModifyDNRequest ) instance ).getNewRDN();
    }


    /**
     * Set the new RDN
     * 
     * @param newRDN The newRDN to set.
     */
    public void setNewRDN( Rdn newRDN )
    {
        ( ( ModifyDNRequest ) instance ).setNewRDN( newRDN );
    }


    /**
     * Get the newSuperior
     * 
     * @return Returns the newSuperior.
     */
    public LdapDN getNewSuperior()
    {
        return ( ( ModifyDNRequest ) instance ).getNewSuperior();
    }


    /**
     * Set the new superior
     * 
     * @param newSuperior The newSuperior to set.
     */
    public void setNewSuperior( LdapDN newSuperior )
    {
        ( ( ModifyDNRequest ) instance ).setNewSuperior( newSuperior );
    }
}
