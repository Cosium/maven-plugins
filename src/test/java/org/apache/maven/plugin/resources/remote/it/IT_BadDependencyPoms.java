package org.apache.maven.plugin.resources.remote.it;

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

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.plugin.resources.remote.it.support.TestUtils;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Benjamin Bentmann
 */
public class IT_BadDependencyPoms
    extends AbstractIT
{

    public void test()
        throws IOException, URISyntaxException, VerificationException
    {
        File dir = TestUtils.getTestDir( "bad-dependency-poms" );

        Verifier verifier;

        verifier = new Verifier( dir.getAbsolutePath() );
        verifier.deleteArtifacts( "test" );
        verifier.getSystemProperties().setProperty( "it.dir", dir.getAbsolutePath() );
//        verifier.setLogFileName( "build.log" );
        
        try
        {
            verifier.executeGoal( "generate-resources" );
        }
        catch ( VerificationException e )
        {
            // We will get an exception from harness in case
            // of execution failure (return code non zero).
            // This is the case if we have missing artifacts
            // as in this test case.
            verifier.resetStreams();

            File output = new File( dir, "log.txt" );
            String content = FileUtils.fileRead( output );
            
            assertTrue(content.contains("mvn install:install-file -DgroupId=test -DartifactId=pom -Dversion=0.2 -Dpackaging=jar"));
            assertTrue(content.contains("mvn install:install-file -DgroupId=test -DartifactId=missing -Dversion=0.1 -Dpackaging=jar"));
            assertTrue(content.contains("mvn install:install-file -DgroupId=test -DartifactId=invalid -Dversion=0.1 -Dpackaging=jar"));
        }
        
    }

}