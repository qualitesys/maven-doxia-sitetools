package org.apache.maven.doxia.siterenderer.velocity;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Velocity component XXX
 */
@Component( role = VelocityComponent.class )
public class DefaultVelocityComponent implements VelocityComponent, Initializable
{
    private VelocityEngine engine;
    private static final Logger LOGGER = LoggerFactory.getLogger( DefaultVelocityComponent.class );

    @Override
    public VelocityEngine getEngine()
    {
        if ( engine == null )
        {
            try
            {
                initialize();
            }
            catch ( InitializationException e )
            {
                LOGGER.error( "Initialize problem: " + e.getMessage() );
            }
        }
        return engine;
    }

    @Override
    public void initialize() throws InitializationException
    {
        engine = new VelocityEngine();
        // avoid "unable to find resource 'VM_global_library.vm' in any resource loader."
        Properties p = new Properties();
        p.setProperty( "resource.loaders", "classpath,file" );
        p.setProperty( "resource.loader.classpath.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        p.setProperty( "resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader" );
        p.setProperty( "resource.loader.file.path", "" );
        p.setProperty( "runtime.log.log_invalid_references", "false" );
        p.setProperty( "velocimacro.messages.on", "false" );
        p.setProperty( "resource.manager.log_when_found", "false" );
        p.setProperty( "event_handler.include.class", "org.apache.velocity.app.event.implement.IncludeRelativePath" );
        p.setProperty( "velocimacro.inline.replace_global", "true" );

        p.setProperty( RuntimeConstants.VM_LIBRARY, "" );

        engine.setProperties( p );

        try
        {
            engine.init();
        }
        catch ( Exception e )
        {
            throw new InitializationException( "Cannot start the Velocity engine", e );
        }
    }
}
