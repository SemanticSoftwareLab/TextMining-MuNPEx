/*
 * Multilingual Noun Phrase Extractor (MuNPEx)
 * http://www.semanticsoftware.info/munpex
 *
 * Test of the MuNPEx English demo pipeline
 *
 * Copyright (c) 2012, 2015 Rene Witte (http://rene-witte.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package info.semanticsoftware.munpex;

import gate.*;
import gate.util.GateException;
import gate.util.OffsetComparator;
import gate.util.ExtensionFileFilter;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import junit.framework.TestCase;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Rene Witte
 */
public class MuNPExTest_EN {
  private static CorpusController munpexApp;
  private Corpus testCorpus;

  @BeforeClass 
  public static void loadApp() throws MalformedURLException, IOException, GateException  {
    if (!Gate.isInitialised()) {
      Gate.init();
    }    

    Properties sysProps = System.getProperties();
    String appName = sysProps.getProperty("munpex.en.app.name");
    munpexApp = (CorpusController) PersistenceManager.loadObjectFromFile(
	        new File(appName));
  }

  @Before
  public void setUp() throws GateException  {
    testCorpus = Factory.newCorpus("Test corpus");
    munpexApp.setCorpus(testCorpus);
  }

  @After
  public void tearDown() throws Exception {
    Factory.deleteResource(testCorpus);
  }

  @AfterClass
  public static void unloadApp() throws Exception {
    Factory.deleteResource(munpexApp);
  }

  @Test
  public void testNP() throws Exception{
      Document testDoc = Factory.newDocument("This is a good test.");
      try {
        testCorpus.add(testDoc);
        try {
          munpexApp.execute();

          // Check the results
          AnnotationSet annots = testDoc.getAnnotations();
          assertNotNull("test document has no annotations!", annots);
          AnnotationSet NPs = annots.get("NP");
          assertNotNull("test document has no NP annotations!", NPs);
          List<Annotation> npList = new ArrayList<Annotation>(NPs);
          // sort in document order
          Collections.sort(npList, new OffsetComparator());
          assertEquals("Document should have one NP", npList.size(), 1);
	  Annotation np = npList.get(0);

          assertEquals("First NP's HEAD should be 'test'",
		       "test", getNPHeadString(np));

          assertEquals("First NP's MOD should be 'good'",
		       "good", getNPModString(np));

          assertEquals("First NP's DET should be 'a'",
		       "a", getNPDetString(np));

          assertEquals("First NP is not a pronoun",
		       "false", getNPPronounString(np));
        } finally {
          testCorpus.remove(testDoc);
        }
      } finally {
        Factory.deleteResource(testDoc);
      }
  } 

  @Test(timeout=40000)
  public void evilNP() throws Exception{
      Document testDoc = Factory.newDocument("The search terms used in the PubMed search were 'schizophrenia' AND 'risperidone' OR 'olanzapine' OR 'quetiapine' OR 'ziprasidone' OR 'aripiprazole'.");
      try {
        testCorpus.add(testDoc);
        try {
          munpexApp.execute();

          // Check the results
          AnnotationSet annots = testDoc.getAnnotations();
          assertNotNull("test document has no annotations!", annots);
          AnnotationSet NPs = annots.get("NP");
          assertNotNull("test document has no NP annotations!", NPs);
        } finally {
          testCorpus.remove(testDoc);
        }
      } finally {
        Factory.deleteResource(testDoc);
      }
  } 

  @Test
  public void testPronoun() throws Exception{
      Document testDoc = Factory.newDocument("He is astounding.");
      try {
        testCorpus.add(testDoc);
        try {
          munpexApp.execute();

          // Check the results
          AnnotationSet annots = testDoc.getAnnotations();
          assertNotNull("test document has no annotations!", annots);
          AnnotationSet NPs = annots.get("NP");
          assertNotNull("test document has no NP annotations!", NPs);
          List<Annotation> npList = new ArrayList<Annotation>(NPs);
          // sort in document order
          Collections.sort(npList, new OffsetComparator());
          assertEquals("Document should have one NP", npList.size(), 1);
	  Annotation np = npList.get(0);

          assertEquals("First NP's HEAD should be 'He'",
		       "He", getNPHeadString(np));

          assertEquals("First NP is a pronoun",
		       "true", getNPPronounString(np));
        } finally {
          testCorpus.remove(testDoc);
        }
      } finally {
        Factory.deleteResource(testDoc);
      }
  } 

  private static String getNPPronounString(Annotation ann) throws Exception {
    FeatureMap features = ann.getFeatures();
    return (String)features.get("Pronoun");
  }

  private static String getNPHeadString(Annotation ann) throws Exception {
    FeatureMap features = ann.getFeatures();
    return (String)features.get("HEAD");
  }

  private static String getNPModString(Annotation ann) throws Exception {
    FeatureMap features = ann.getFeatures();
    return (String)features.get("MOD");
  }

  private static String getNPDetString(Annotation ann) throws Exception {
    FeatureMap features = ann.getFeatures();
    return (String)features.get("DET");
  }
}
