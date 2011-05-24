/**
 * Sonatype Hudson Professional (TM)
 * Copyright (C) 2010-2011 Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at http://www.sonatype.com/products/hudson/attributions/.
 * "Sonatype" and "Sonatype Hudson Professional" are trademarks of Sonatype, Inc.
 * "Hudson" is a trademark of Oracle, Inc.
 */
package com.sonatype.matrix.maven.eventspy_30.handler;

import com.sonatype.matrix.maven.eventspy_30.EventSpyHandler;
import com.sonatype.matrix.maven.eventspy_30.MavenProjectConverter;
import org.apache.maven.execution.MavenExecutionResult;

import javax.inject.Named;

/**
 * Handles {@link MavenExecutionResult} events.
 * 
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 1.1
 */
@Named
public class MavenExecutionResultHandler
    extends EventSpyHandler<MavenExecutionResult>
{
    public void handle( final MavenExecutionResult event )
        throws Exception
    {
        log.debug( "Execution result: {}", event );

        if ( event.hasExceptions() )
        {
            log.info( "Build failed with exception(s)" );

            int i = 0;
            for ( Throwable cause : event.getExceptions() )
            {
                log.info( "[{}] {}", ++i, cause );
            }
        }

        log.debug( "Recording MavenProjects" );
        getBuildRecorder().recordSessionFinished( MavenProjectConverter.extractFrom( event ) );
    }
}