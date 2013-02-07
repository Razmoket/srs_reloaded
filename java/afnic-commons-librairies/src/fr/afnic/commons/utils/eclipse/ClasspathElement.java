package fr.afnic.commons.utils.eclipse;

import org.jdom.Element;

public class ClasspathElement {

    private static final String KIND = "kind";
    private static final String COMBINE_ACCESS_RULES = "combineaccessrules";
    private static final String PATH = "path";

    private ClasspathKind kind;
    private boolean combineaccessrules = true;
    private String path;

    private Element element;

    public ClasspathElement(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("element cannot be null.");
        } else {
            this.element = element;
        }

        if (this.hasAttribute(ClasspathElement.KIND)) {
            this.kind = this.getAttributeValue(ClasspathElement.KIND, ClasspathKind.class);
        } else {
            throw new IllegalArgumentException("Element should contains " + ClasspathElement.KIND + " element.");
        }

        if (this.hasAttribute(ClasspathElement.PATH)) {
            this.path = this.getAttributeValueAsString(ClasspathElement.PATH);
        } else {
            throw new IllegalArgumentException("Element should contains " + ClasspathElement.PATH + " element.");
        }

        if (this.hasAttribute(ClasspathElement.COMBINE_ACCESS_RULES)) {
            this.combineaccessrules = this.getAttributeValue(ClasspathElement.COMBINE_ACCESS_RULES, Boolean.class);
        }
    }

    @SuppressWarnings("unchecked")
    private <E> E getAttributeValue(String name, Class<E> clazz) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null.");
        }

        if (clazz == null) {
            throw new IllegalArgumentException("clazz cannot be null.");
        }

        if (this.hasNotAttribute(name)) {
            return null;
        }

        try {
            if (clazz == Boolean.class) {
                return (E) new Boolean(this.getAttributeValueAsString(name));
            }

            if (clazz == String.class) {
                return (E) this.getAttributeValueAsString(name);
            }

            if (clazz.isEnum()) {
                for (E enumConstant : clazz.getEnumConstants()) {
                    if (enumConstant.toString().equals(this.getAttributeValueAsString(name))) {
                        return enumConstant;
                    }
                }
                throw new RuntimeException("no " + clazz.getSimpleName() + " defined for " + this.getAttributeValueAsString(name));
            }
        } catch (Exception e) {
            throw new RuntimeException("getAttributeValue( " + name + ", " + clazz.getSimpleName() + ") failed with element " + this.element.toString(), e);
        }

        throw new RuntimeException("getAttributeValue(" + clazz.getSimpleName());
    }

    public ClasspathKind getKind() {
        return this.kind;
    }

    public boolean isCombineaccessrules() {
        return this.combineaccessrules;
    }

    public String getPath() {
        if (this.isProjectDepedencyElement()) {
            return this.path.substring(1);
        }
        
        if (this.isLibrairyDepedencyElement()) {
        	if (this.path.contains("/lib/")){
            return this.path.split("/lib/")[1];
        	}
        }
        return this.path;
    }

    private String getAttributeValueAsString(String name) {
        if (this.hasAttribute(name)) {
            return this.element.getAttribute(name).getValue();
        } else {
            return null;
        }
    }

    private boolean hasNotAttribute(String name) {
        return !this.hasAttribute(name);
    }

    private boolean hasAttribute(String name) {
        return this.element.getAttribute(name) != null;
    }

    public boolean isProjectDepedencyElement() {
        return this.kind == ClasspathKind.src && !this.combineaccessrules;
    }

    public boolean isLibrairyDepedencyElement() {
        return this.kind == ClasspathKind.lib && this.path.endsWith(".jar");
    }

}
