/*
 * Multilingual Noun Phrase Extractor (MuNPEx) 
 * Java wrapper for CREOLE Plugin manager
 * http://www.semanticsoftware.info/munpex
 *
 * Copyright (c) 2005, 2006, 2010, 2012, 2015 Rene Witte (http://rene-witte.net)
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
 */
package info.semanticsoftware.munpex;

import gate.creole.Transducer;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.HiddenCreoleParameter;
import java.net.URL;

/**
 * The transducer for the Multi-lingual Noun Phrase Extractor (MuNPEx)
 * configuration for German (DE)
 * This is a JAPE transducer and this class is here to allow the specification
 * in creole.xml of a default grammar to be used in MuNPEx.
 */
@CreoleResource(name = "MuNPEx German (DE) NP Chunker",
  comment = "Multi-lingual Noun Phrase Extractor (MuNPEx) -- German",
  helpURL = "http://www.semanticsoftware.info/munpex/",
  icon = "jape"
  )
@SuppressWarnings("PMD")
public class MuNPExDETransducer extends Transducer {

  /**
   * The ontology parameter is not used for this PR and therefore hidden.
   * 
   * @param ontology
   */
  @HiddenCreoleParameter
  @Override
  public void setOntology(gate.creole.ontology.Ontology ontology) {
    super.setOntology(ontology);
  }

  /**
   * The binaryGrammarURL parameter is not used for this PR and therefore hidden.
   * 
   * @param url
   */
  @HiddenCreoleParameter
  @Override
  public void setBinaryGrammarURL(URL url) {
    super.setBinaryGrammarURL(url);
  }


  /**
   * The grammarURL parameter provides the MuNPEx-DE main.jape file as a default
   * for this PR.
   * 
   * @param newGrammarURL
   */
  @CreoleParameter(
    comment = "The URL to the grammar file.",
    suffixes = "jape",
    defaultValue = "resources/de-np_main.jape"
  )
  @Override
  public void setGrammarURL(java.net.URL newGrammarURL) {
    super.setGrammarURL(newGrammarURL);
  }
}
