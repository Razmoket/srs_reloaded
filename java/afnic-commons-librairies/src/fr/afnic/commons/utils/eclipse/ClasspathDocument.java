package fr.afnic.commons.utils.eclipse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ClasspathDocument {

    private List<ClasspathElement> elements;

    public ClasspathDocument(String fileName) throws FileNotFoundException {
        this.initClasspathElements(fileName);
    }

    private void initClasspathElements(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException(fileName);
        }

        SAXBuilder sxb = new SAXBuilder();
        try {

            Document document = sxb.build(file);

            @SuppressWarnings("unchecked")
            List<Element> children = document.getRootElement().getChildren();

            this.elements = new ArrayList<ClasspathElement>();
            for (Element element : children) {
                this.elements.add(new ClasspathElement(element));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException("initClasspathElements failed", e);
        }
    }

    public List<String> getProjectsDepedencies() {
        List<String> ret = new ArrayList<String>();
        for (ClasspathElement element : this.elements) {
            if (element.isProjectDepedencyElement()) {
                ret.add(element.getPath());
            }
        }
        return ret;
    }

    public List<String> getLibrairiesDepedencies() {
        List<String> ret = new ArrayList<String>();
        for (ClasspathElement element : this.elements) {
            if (element.isLibrairyDepedencyElement()) {
                ret.add(element.getPath());
            }
        }
        return ret;
    }

}
