package fr.afnic.commons.utils.ant;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import fr.afnic.commons.services.jooq.GenerationTool;

/**
 * Class generation facility that can be invoked from ant.
 *  
 * @author Lukas Eder
 */
public class JooqStubGenerationTask extends Task {

    Properties properties = new Properties();

    private String schema;

    @Override
    public void execute() throws BuildException {

        try {
            for (String schema : this.getSchemas()) {
                this.schema = schema;
                System.out.println("Execute with schema:" + schema);
                this.populateProperties();
                this.properties.setProperty("jdbc.Schema", schema);
                GenerationTool.main(this.properties);
            }
        } catch (Exception e) {

            e.printStackTrace();
            throw new BuildException(e);
        }
    }

    public List<String> getSchemas() {

        List<String> schemas = new ArrayList<String>();

        @SuppressWarnings("unchecked")
        Hashtable<String, String> map = this.getProject().getProperties();
        for (Entry<String, String> entry : map.entrySet()) {

            Pattern pattern = Pattern.compile("(.*)\\.jdbc\\.User");
            Matcher matcher = pattern.matcher(entry.getKey());
            if (matcher.find()) {
                schemas.add(matcher.group(1));
            }
        }
        return schemas;
    }

    @SuppressWarnings("unchecked")
    private void populateProperties() {
        this.properties.clear();

        Hashtable<String, String> map = this.getProject().getProperties();

        for (Entry<String, String> entry : map.entrySet()) {
            if (this.isValidPropertyName(entry.getKey())) {
                this.addProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    private void addProperty(String name, String value) {
        if (name.startsWith(this.schema + ".")) {
            name = name.replace(this.schema + ".", "");
        }

        if (name.equals("generator.target.package")) {
            value = value + "." + this.schema.toLowerCase();
        }

        System.out.println(name + ": " + value);
        this.properties.setProperty(name, value);
    }

    private boolean isValidPropertyName(String name) {
        return name != null
               && (name.startsWith("jdbc.")
                   || name.startsWith("generator")
                   || name.startsWith(this.schema + "."));

    }
}
