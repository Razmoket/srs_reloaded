package fr.afnic.commons.utils.ant;

import java.io.FileNotFoundException;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.LogLevel;

import com.google.common.base.Joiner;

import fr.afnic.commons.utils.eclipse.ClasspathDocument;

public class ClasspathTask extends Task {

    private static final String CLASSPATH_FILE = ".classpath";
    private static final String PROJECT_DEPENDENCY_PROPERTY = "depedency.projects";
    private static final String LIB_DEPENDENCY_PROPERTY = "depedency.librairies";

    @Override
    public void execute() throws BuildException {

        try {
            ClasspathDocument document = new ClasspathDocument(this.getClasspathFilePathAndFileName());
            this.populateVar(ClasspathTask.LIB_DEPENDENCY_PROPERTY, document.getLibrairiesDepedencies());
            this.populateVar(ClasspathTask.PROJECT_DEPENDENCY_PROPERTY, document.getProjectsDepedencies());

        } catch (FileNotFoundException e) {
            this.log(e.getMessage(), LogLevel.WARN.getLevel());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException("ClasspathTask failed", e);
        }
    }

    private String getClasspathFilePathAndFileName() {
        return "../" + this.getProject().getName() + "/" + ClasspathTask.CLASSPATH_FILE;
    }

    private void populateVar(String name, List<String> values) {
        this.populateVar(name, Joiner.on(",").join(values));

    }

    private void populateVar(String name, String value) {
        Property property = new Property();
        property.setName(name);
        property.setValue(value);
        property.bindToOwner(this);
        property.init();
        property.execute();
        System.out.println("initialize " + name + " as " + value);
    }

}
