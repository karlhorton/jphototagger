package org.jphototagger.program.module.exportimport.exporter;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jphototagger.domain.metadata.keywords.Keyword;
import org.jphototagger.domain.repository.KeywordsRepository;
import org.jphototagger.domain.repository.RepositoryDataExporter;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.resources.Icons;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * JPhotoTagger's own exportToFile format.
 *
 * @author Elmar Baumann
 */
@ServiceProvider(service = RepositoryDataExporter.class)
public final class KeywordsExporterJpt implements RepositoryDataExporter {

    public static final String DEFAULT_FILENAME = "JptKeywords.xml";
    /**
     * DTD of the exported file
     */
    public static final String DTD = "keywords.dtd";
    public static final int POSITION = 10;
    /**
     * Name of the root tag in the exported file. The root tag isn't a keyword,
     * but all it's children are keywords and the keyword's children are
     * keywords
     */
    public static final String TAGNAME_ROOT = "keywords";
    /**
     * Name of a keyword tag in the exported file
     */
    public static final String TAGNAME_KEYWORD = "keyword";
    /**
     * Name of the attribute containing the keyword type - real or helper -
     * within the keyword tag
     */
    public static final String ATTRIBUTE_TYPE = "type";
    /**
     * String values of the two possible values of {@code #ATTRIBUTE_TYPE},
     * <code>true</code> for a real keyword and <code>false</code> for a helper
     * keyword
     */
    public static final Map<Boolean, String> VALUE_OF_ATTRIBUTE_TYPE = new HashMap<>();
    /**
     * Name of the attribute containing the keyword name within the keyword tag
     */
    public static final String ATTRIBUTE_NAME = "name";
    public static final Icon ICON = Icons.getIcon("icon_app_small.png");
    public static final String DISPLAY_NAME = Bundle.getString(KeywordsExporterJpt.class, "KeywordExporterJpt.DisplayName");
    public static final FileFilter FILE_FILTER = new FileNameExtensionFilter(DISPLAY_NAME, "xml");
    private final KeywordsRepository repo = Lookup.getDefault().lookup(KeywordsRepository.class);


    static {
        VALUE_OF_ATTRIBUTE_TYPE.put(true, "real");
        VALUE_OF_ATTRIBUTE_TYPE.put(false, "helper");
    }

    @Override
    public void exportToFile(File file) {
        if (file == null) {
            throw new NullPointerException("file == null");
        }
        try {
            Document doc = getXml();
            DOMSource ds = new DOMSource(doc);
            StreamResult sr = new StreamResult(checkSuffix(file));
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            initTransformer(trans);
            trans.transform(ds, sr);
        } catch (Throwable t) {
            Logger.getLogger(KeywordsExporterJpt.class.getName()).log(Level.SEVERE, null, t);
        }
    }

    private File checkSuffix(File file) {
        String suffix = "." + FilenameSuffixes.JPT_KEYWORDS;
        if (!file.getName().endsWith(suffix)) {
            return new File(file.getAbsolutePath() + suffix);
        }
        return file;
    }

    private Document getXml() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();
        Document doc = impl.createDocument(null, null, null);
        Element rootElement = doc.createElement(TAGNAME_ROOT);
        doc.appendChild(rootElement);
        appendChildren(doc, rootElement, null);
        return doc;
    }

    private void appendChildren(Document doc, Element parentElement, Keyword  parentKeyword) {
        Collection<Keyword> children = parentKeyword == null
                ? repo.findRootKeywords()
                : repo.findChildKeywords(parentKeyword.getId());
        for (Keyword childKeyword : children) {
            Element childElement = appendChild(doc, parentElement, childKeyword);
            appendChildren(doc, childElement, childKeyword); // recursive
        }
    }

    private Element appendChild(Document doc, Element parentElement, Keyword childKeyword) {
        Element childElement = doc.createElement(TAGNAME_KEYWORD);
        setElementAttributes(childElement, childKeyword);
        parentElement.appendChild(childElement);
        return childElement;
    }

    private void setElementAttributes(Element el, Keyword keyword) throws DOMException {
        el.setAttribute(ATTRIBUTE_NAME, keyword.getName());
        el.setAttribute(ATTRIBUTE_TYPE, VALUE_OF_ATTRIBUTE_TYPE.get(keyword.isReal()));
    }

    private void initTransformer(Transformer trans) throws IllegalArgumentException {
        trans.setOutputProperty(OutputKeys.METHOD, "xml");
        trans.setOutputProperty(OutputKeys.ENCODING, "UTF8");
        trans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, DTD);
        trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.setOutputProperty(OutputKeys.STANDALONE, "no");
    }

    @Override
    public FileFilter getFileFilter() {
        return FILE_FILTER;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }

    @Override
    public String getDefaultFilename() {
        return DEFAULT_FILENAME;
    }

    @Override
    public boolean isJPhotoTaggerData() {
        return true;
    }

    @Override
    public int getPosition() {
        return POSITION;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    private static class FilenameSuffixes {

        /**
         * Filename suffix of exported Adobe Photoshop Lightroom keywords
         */
        public static final String LIGHTROOM_KEYWORDS = "txt";
        /**
         * Filename suffix of exported JPhotoTagger keywords
         */
        public static final String JPT_KEYWORDS = "xml";

        private FilenameSuffixes() {
        }
    }
}
